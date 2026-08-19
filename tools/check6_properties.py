"""
CHECK 16 — property access on project data classes must resolve.

The gap this closes: DetectedNumber changed from flat coordinates to a nested
box, and the overlay kept reading `.left` and `.hasBox`. Symbol resolution passed
(both names existed elsewhere), interface checks passed, and the build failed on
an unresolved reference. Only a type-aware check catches it.

Scope is deliberately narrow — locals whose type is written down, either as an
explicit annotation or a constructor call — because guessing at inferred types
produces false positives, and a checker that cries wolf gets ignored.
"""
import os, re, sys

def _find_app_dir():
    here = os.path.dirname(os.path.abspath(__file__))
    for base in (os.path.dirname(here), here, os.getcwd()):
        for candidate in ("app", os.path.join("android", "app")):
            path = os.path.join(base, candidate)
            if os.path.isfile(os.path.join(path, "build.gradle.kts")):
                return path
    raise SystemExit("check: could not locate the app module")

APP = _find_app_dir()
SRC = os.path.join(APP, "src", "main", "java", "com", "middleeastcontainer")

# Public members of every project data class.
data_members, sources = {}, {}
for dp, _, fns in os.walk(SRC):
    for fn in fns:
        if not fn.endswith(".kt"):
            continue
        path = os.path.join(dp, fn)
        src = open(path, errors="ignore").read()
        sources[path] = src
        for m in re.finditer(r'data class (\w+)\(([\s\S]*?)\n\)([\s\S]{0,400}?)(?=\ndata class|\nclass|\ninterface|\nobject|\Z)', src):
            name, ctor, body = m.group(1), m.group(2), m.group(3)
            members = set(re.findall(r'va[lr]\s+(\w+)\s*:', ctor))
            members |= set(re.findall(r'\bva[lr]\s+(\w+)\s*(?::|get\()', body))
            members |= set(re.findall(r'fun\s+(\w+)\s*\(', body))
            data_members[name] = members

STDLIB = {"copy", "toString", "equals", "hashCode", "let", "also", "apply", "run",
          "takeIf", "takeUnless", "component1", "component2"}

problems = []
for path, src in sources.items():
    # Locals with a written-down type: `val x: Type =` or `val x = Type(`
    typed = dict(re.findall(r'\bval (\w+)\s*:\s*(\w+)\s*=', src))
    typed.update(dict(re.findall(r'\bval (\w+)\s*=\s*(\w+)\(', src)))
    # Lambda params from forEach on a typed list are not tracked — too speculative.
    for var, typ in typed.items():
        if typ not in data_members:
            continue
        for m in re.finditer(rf'\b{var}\.(\w+)\b', src):
            prop = m.group(1)
            if prop in data_members[typ] or prop in STDLIB:
                continue
            problems.append(f"{os.path.basename(path)}: {var}.{prop} not on {typ}")

print("CHECK 16 — property access on project data classes")
if problems:
    for p in sorted(set(problems)):
        print(f"  *** {p}")
    sys.exit(1)
print(f"  [PASS] all resolve ({len(data_members)} data classes known)")
