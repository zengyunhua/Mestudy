"""Regression tests for the current-checkout sensitive-literal scan."""
import unittest
from check_sensitive import findings


class CheckSensitiveTests(unittest.TestCase):
    def test_detects_secrets_and_ids_without_echoing_values(self):
        cases = [
            ('module/src/main/resources/config.properties', 'password=fictional-value', 'local configuration is tracked'),
            ('module/src/main/java/Foo.java', 'String apiKey = "fictional-value";', 'hardcoded sensitive assignment'),
            ('module/src/main/java/Foo.java', 'String tokenname = "fictional-value";', 'hardcoded sensitive assignment'),
            ('module/src/main/java/Foo.java', 'String secretwk = "fictional-value";', 'hardcoded sensitive assignment'),
            ('module/src/main/resources/ds.xml.example', '<store pwd="fictional-value"/>', 'hardcoded sensitive assignment'),
            ('module/src/main/resources/config.properties.example', 'token: fictional-value', 'hardcoded sensitive assignment'),
            ('module/src/main/java/Foo.java', 'String sample = "110101199001010016";', 'ID-shaped value'),
            ('module/src/main/java/Foo.java', '-----BEGIN PRIVATE KEY-----', 'private-key marker'),
        ]
        for name, text, reason in cases:
            with self.subTest(name=name, reason=reason):
                result = findings(name, text)
                self.assertTrue(any(item[2] == reason for item in result))
                self.assertNotIn('fictional-value', str(result))

    def test_accepts_placeholders_and_normal_code(self):
        cases = [
            ('module/src/main/resources/config.properties.example', 'password=CHANGE_ME'),
            ('module/src/main/resources/ds.xml.example', '<store pwd="CHANGE_ME"/>'),
            ('module/src/main/java/Foo.java', 'String token = request.getToken();'),
            ('module/src/main/java/Foo.java', 'String TOKENNAME = "sisp_iface:token:";'),
            ('module/src/main/java/Foo.java', 'String msg = "expired token: " + token;'),
            ('module/src/main/java/Foo.java', 'String sample = "110101199001010015";'),
        ]
        for name, text in cases:
            with self.subTest(name=name):
                self.assertEqual(findings(name, text), [])


if __name__ == '__main__':
    unittest.main()
