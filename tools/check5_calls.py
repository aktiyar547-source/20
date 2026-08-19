"""
CHECK 15 — every method called on an injected dependency actually exists.

The gap this closes: interface-to-implementation was already checked, but not
caller-to-interface. Removing a method from a repository left a ViewModel calling
it, which compiles nowhere and was invisible to every other check — the build
failed at 'Assemble debug APK' with an unresolved reference.
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

# Public surface of every interface, class and object in the project.
members, files = {}, {}
for dp, _, fns in os.walk(SRC):
    for fn in fns:
        if not fn.endswith(".kt"):
            continue
        path = os.path.join(dp, fn)
        src = open(path, errors="ignore").read()
        files[path] = src
        for m in re.finditer(
            r'(?:interface|class|object)\s+(\w+)[\s\S]*?\{([\s\S]*)', src
        ):
            name = m.group(1)
            body = m.group(2)
            found = set(re.findall(r'fun\s+(\w+)\s*\(', body))
            found |= set(re.findall(r'\b(?:val|var)\s+(\w+)\s*[:=]', body))
            members.setdefault(name, set()).update(found)

problems = []
for path, src in files.items():
    # Constructor-injected properties: name -> declared type.
    injected = dict(re.findall(r'private val (\w+):\s*(\w+)', src))
    for prop, typ in injected.items():
        if typ not in members:
            continue                       # framework or generic type
        for m in re.finditer(rf'\b{prop}\.(\w+)\s*\(', src):
            called = m.group(1)
            if called in members[typ]:
                continue
            # Kotlin stdlib and flow operators are not ours to police.
            if called in {"map", "first", "firstOrNull", "collect", "value", "copy",
                          "toString", "equals", "hashCode", "let", "also", "apply",
                          "invoke", "getOrNull", "getOrPut", "orEmpty", "isEmpty",
                          "isNotEmpty", "filter", "filterNot", "forEach", "toList",
                          "stateIn", "distinct", "associate", "sortedBy", "take",
                          "mapNotNull", "joinToString", "contains", "plus", "size"}:
                continue
            problems.append(f"{os.path.basename(path)}: {prop}.{called}() not on {typ}")

print("CHECK 15 — calls on injected dependencies resolve")
if problems:
    for p in sorted(set(problems)):
        print(f"  *** {p}")
    sys.exit(1)
print(f"  [PASS] every call resolves ({len(members)} types known)")
