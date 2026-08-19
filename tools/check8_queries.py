"""
CHECK 18 — every @Query column exists on the entity it selects from.

Room validates this itself, but only during the Gradle build, as an annotation
processing failure with a Gradle stack trace rather than a Kotlin error. Catching
it here turns a ten-minute CI round trip into an instant answer — and it caught a
real one the day it was written (ContainerType, where the column is Type).
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
DB = os.path.join(APP, "src/main/java/com/middleeastcontainer/data/database")

tables = {}
for dp, _, fns in os.walk(os.path.join(DB, "entity")):
    for fn in fns:
        if not fn.endswith(".kt"):
            continue
        s = open(os.path.join(dp, fn)).read()
        for m in re.finditer(
            r'@Entity\(\s*tableName = "(\w+)"([\s\S]*?)data class (\w+)\(([\s\S]*?)\n\)', s
        ):
            tables[m.group(1)] = set(re.findall(r'val (\w+)\s*:', m.group(4)))

KEYWORDS = {
    "SELECT","FROM","WHERE","AND","OR","ORDER","BY","LIMIT","UPDATE","SET","DELETE",
    "INSERT","INTO","VALUES","COUNT","DISTINCT","IS","NULL","NOT","ASC","DESC","AS",
    "ON","JOIN","LEFT","INNER","GROUP","HAVING","IN","LIKE","OFFSET","EXISTS","CASE",
    "WHEN","THEN","ELSE","END","MAX","MIN","SUM","AVG",
}

problems, checked = [], 0
for dp, _, fns in os.walk(os.path.join(DB, "dao")):
    for fn in sorted(fns):
        if not fn.endswith(".kt"):
            continue
        src = open(os.path.join(dp, fn)).read()
        for m in re.finditer(r'@Query\(\s*"([\s\S]*?)"\s*\)', src):
            # Join concatenated string pieces, and blank out SQL literals so a
            # value like 'Done' is not mistaken for a column.
            sql = re.sub(r'"\s*\+\s*"', ' ', m.group(1))
            sql = re.sub(r"'[^']*'", "''", sql)
            checked += 1
            for table in re.findall(r'(?:FROM|INTO|UPDATE)\s+(\w+)', sql, re.I):
                if table not in tables:
                    problems.append(f"{fn}: no entity maps to table '{table}'")
                    continue
                binds = set(re.findall(r':(\w+)', sql))
                for ident in set(re.findall(r'(?<![:\w.])([A-Za-z_]\w*)', sql)):
                    if ident.upper() in KEYWORDS or ident in tables or ident in binds:
                        continue
                    if ident not in tables[table]:
                        problems.append(f"{fn}: '{ident}' is not a column of {table}")

print(f"CHECK 18 — @Query columns exist ({checked} queries, {len(tables)} tables)")
if problems:
    for p in sorted(set(problems)):
        print(f"  *** {p}")
    sys.exit(1)
print("  [PASS] every query references real tables and columns")
