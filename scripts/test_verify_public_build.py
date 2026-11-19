"""Regression tests for the public reactor output gate."""
from pathlib import Path
import tempfile
import unittest
import zipfile
from verify_public_build import MODULES, validate


class PublicBuildValidationTest(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.addCleanup(self.temp.cleanup)
        self.root = Path(self.temp.name)
        for name in MODULES:
            target = self.root / name / 'target'
            reports = target / 'surefire-reports'
            reports.mkdir(parents=True)
            with zipfile.ZipFile(target / (name + '.war'), 'w') as archive:
                archive.writestr('WEB-INF/web.xml', '<web-app/>')
            (reports / 'TEST-example.xml').write_text(
                '<testsuite tests="1" failures="0" errors="0" skipped="0"/>', encoding='utf-8')

    def test_all_modules_have_war_and_passing_tests(self):
        self.assertEqual([], validate(self.root))

    def test_missing_test_report_is_rejected(self):
        (self.root / MODULES[1] / 'target' / 'surefire-reports' / 'TEST-example.xml').unlink()
        self.assertTrue(any('no Surefire' in problem for problem in validate(self.root)))

    def test_empty_war_and_test_failure_are_rejected(self):
        target = self.root / MODULES[0] / 'target'
        (target / (MODULES[0] + '.war')).write_bytes(b'')
        (target / 'surefire-reports' / 'TEST-example.xml').write_text(
            '<testsuite tests="1" failures="1" errors="0" skipped="0"/>', encoding='utf-8')
        problems = validate(self.root)
        self.assertTrue(any('nonempty WAR' in problem for problem in problems))
        self.assertTrue(any('passing automated tests' in problem for problem in problems))

    def test_all_skipped_is_rejected(self):
        report = self.root / MODULES[0] / 'target' / 'surefire-reports' / 'TEST-example.xml'
        report.write_text(
            '<testsuite tests="1" failures="0" errors="0" skipped="1"/>', encoding='utf-8')
        self.assertTrue(any('passing automated tests' in problem for problem in validate(self.root)))
    def test_malformed_report_is_rejected(self):
        (self.root / MODULES[2] / 'target' / 'surefire-reports' / 'TEST-example.xml').write_text(
            '<broken', encoding='utf-8')
        self.assertTrue(any('invalid report' in problem for problem in validate(self.root)))

    def test_non_archive_war_is_rejected(self):
        war = self.root / MODULES[0] / 'target' / (MODULES[0] + '.war')
        war.write_bytes(b'not a zip archive')
        self.assertTrue(any('invalid WAR archive' in problem for problem in validate(self.root)))

    def test_container_and_test_libraries_are_rejected(self):
        war = self.root / MODULES[0] / 'target' / (MODULES[0] + '.war')
        for library in ('servlet-api-2.3.jar', 'javax.servlet-api-3.1.0.jar',
                        'spring-test-4.0.8.RELEASE.jar', 'junit-4.11.jar',
                        'hamcrest-core-1.3.jar', 'jsp-api-2.1.jar'):
            with self.subTest(library=library):
                with zipfile.ZipFile(war, 'w') as archive:
                    archive.writestr('WEB-INF/web.xml', '<web-app/>')
                    archive.writestr('WEB-INF/lib/' + library, b'example')
                problems = validate(self.root)
                self.assertTrue(any('container/test libraries' in problem and library in problem
                                    for problem in problems))

    def test_real_runtime_libraries_are_not_rejected(self):
        war = self.root / MODULES[0] / 'target' / (MODULES[0] + '.war')
        with zipfile.ZipFile(war, 'a') as archive:
            archive.writestr('WEB-INF/lib/spring-webmvc-4.0.8.RELEASE.jar', b'example')
            archive.writestr('WEB-INF/lib/jedis-2.6.2.jar', b'example')
        self.assertEqual([], validate(self.root))


if __name__ == '__main__':
    unittest.main()
