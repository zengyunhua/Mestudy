"""Keep historical risk inventory output free of matched values."""
import unittest
from audit_git_history import candidates, classify


class HistoricalAuditTest(unittest.TestCase):
    def test_candidates_filter_non_source_paths(self):
        lines = b'aaa module/src/main/resources/config.properties\nbbb assets/logo.png\naaa module/src/main/resources/config.properties\n'
        self.assertEqual([(b'aaa', 'module/src/main/resources/config.properties')], list(candidates(lines)))

    def test_classify_does_not_return_secret_or_id(self):
        secret = 'fictional-private-key-value'
        result = classify('module/src/main/resources/config.properties', 'password=' + secret)
        self.assertIn('local configuration is tracked', result)
        self.assertNotIn(secret, str(result))
        identity = classify('module/src/main/java/Foo.java', 'String id="110101199001010016";')
        self.assertIn('ID-shaped value', identity)
        self.assertNotIn('110101199001010016', str(identity))


if __name__ == '__main__':
    unittest.main()
