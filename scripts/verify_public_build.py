"""Check public Maven reactor WAR outputs and per-module JUnit reports."""
from pathlib import Path
import sys
import xml.etree.ElementTree as ET
import zipfile

MODULES = ('IFaceSuportService', 'IFaceSuperService_hn', 'InterfaceFunction', 'NetworkOfficeService',
           'NetPortalManagement')

# Neither compile-time container APIs nor test libraries belong in deployable WARs.
NON_RUNTIME_LIBRARY_PREFIXES = (
    'servlet-api-', 'javax.servlet-api-', 'jsp-api-', 'javax.servlet.jsp-api-',
    'spring-test-', 'junit-', 'junit-jupiter-', 'junit-platform-',
    'hamcrest-', 'hamcrest-core-', 'hamcrest-library-',
)


def war_problems(module, war):
    try:
        with zipfile.ZipFile(war) as archive:
            names = archive.namelist()
            forbidden = [name for name in names if name.startswith('WEB-INF/lib/')
                         and name.endswith('.jar')
                         and name.rsplit('/', 1)[-1].startswith(NON_RUNTIME_LIBRARY_PREFIXES)]
            if forbidden:
                return ['%s: container/test libraries packaged in WAR: %s' %
                        (module, ', '.join(sorted(forbidden)))]
    except (OSError, zipfile.BadZipFile) as exc:
        return ['%s: invalid WAR archive: %s' % (module, exc)]
    return []


def validate(root):
    problems = []
    for name in MODULES:
        target = root / name / 'target'
        wars = list(target.glob('*.war'))
        if len(wars) != 1 or not wars[0].is_file() or not wars[0].stat().st_size:
            problems.append('%s: expected one nonempty WAR' % name)
        else:
            problems.extend(war_problems(name, wars[0]))
        reports = list((target / 'surefire-reports').glob('TEST-*.xml'))
        if not reports:
            problems.append('%s: no Surefire XML reports (tests skipped?)' % name)
            continue
        tests = failures = errors = skipped = 0
        for report in reports:
            try:
                suite = ET.parse(report).getroot()
                if suite.tag != 'testsuite':
                    raise ValueError('expected testsuite root')
                tests += int(suite.attrib['tests'])
                failures += int(suite.attrib['failures'])
                errors += int(suite.attrib['errors'])
                skipped += int(suite.attrib.get('skipped', '0'))
            except (ET.ParseError, OSError, KeyError, ValueError) as exc:
                problems.append('%s: invalid report %s: %s' % (name, report.name, exc))
        print('%s: %d tests, %d skipped, %d failures, %d errors, %d WAR' % (name, tests, skipped, failures, errors, len(wars)))
        if tests - skipped < 1 or failures or errors:
            problems.append('%s: expected passing automated tests' % name)
    return problems

if __name__ == '__main__':
    problems = validate(Path(__file__).resolve().parent.parent)
    for problem in problems:
        print('ERROR: ' + problem, file=sys.stderr)
    sys.exit(bool(problems))
