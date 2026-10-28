"""Regression tests for the public reactor output gate."""
from pathlib import Path
import tempfile
import unittest
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
            (target / (name + '.war')).write_bytes(b'war')
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


if __name__ == '__main__':
    unittest.main()
