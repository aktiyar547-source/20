"""
CHECK 17 — methods chained onto a call must exist on that call's return type.

The gap this closes: `scan(...)` returns FrameReading, and the code chained
`.firstOrNull()` onto it — valid on a List, absent on FrameReading. Symbol
resolution passed (firstOrNull exists in the stdlib), the interface checks
passed, and the build failed. Only following the return type catches it.
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

# Members of every project type, and the declared return type of every function.
members, returns, sources = {}, {}, {}
for dp, _, fns in os.walk(SRC):
    for fn in fns:
        if not fn.endswith(".kt"):
            continue
        path = os.path.join(dp, fn)
        src = open(path, errors="ignore").read()
        sources[path] = src
        for m in re.finditer(
            r'(?:data class|class|interface|object|enum class)\s+(\w+)([\s\S]*?)(?=\n(?:data class|class|interface|object|enum class|@)|\Z)',
            src,
        ):
            name, body = m.group(1), m.group(2)
            found = set(re.findall(r'va[lr]\s+(\w+)\s*[:=]', body))
            found |= set(re.findall(r'fun\s+(\w+)\s*\(', body))
            members.setdefault(name, set()).update(found)
        # fun name(...): ReturnType
        for m in re.finditer(r'fun\s+(\w+)\s*\([^)]*\)\s*:\s*(\w+)', src):
            returns.setdefault(m.group(1), set()).add(m.group(2))

# Collection returns are chained with stdlib operators; only flag plain types.
COLLECTIONS = {"List", "Set", "Map", "MutableList", "MutableSet", "MutableMap",
               "Flow", "StateFlow", "Array", "Sequence", "Collection", "Iterable"}

problems = []
for path, src in sources.items():
    for m in re.finditer(r'(?<![\w.])(\w+)\([^()]*\)\s*\.\s*(\w+)\s*\(', src):
        fname, chained = m.group(1), m.group(2)
        rets = returns.get(fname)
        if not rets or len(rets) != 1:
            continue
        ret = next(iter(rets))
        if ret in COLLECTIONS or ret not in members:
            continue
        if chained in members[ret]:
            continue
        if chained in {"copy", "toString", "equals", "hashCode", "let", "also",
                       "apply", "run", "takeIf", "takeUnless"}:
            continue
        problems.append(
            f"{os.path.basename(path)}: {fname}(...) returns {ret}, "
            f"but .{chained}() is called on it")

print("CHECK 17 — chained calls match the return type")
if problems:
    for p in sorted(set(problems)):
        print(f"  *** {p}")
    sys.exit(1)
print(f"  [PASS] all chains resolve ({len(returns)} return types known)")
