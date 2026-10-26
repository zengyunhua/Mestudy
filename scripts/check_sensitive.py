#!/usr/bin/env python3
"""Check tracked sources for common sensitive literals without printing their values.

This is a heuristic for the current checkout, not a historical or provider audit.
"""
import pathlib
import re
import subprocess
import sys

ROOT = pathlib.Path(__file__).resolve().parents[1]
ID = re.compile(r'(?<!\d)[1-9]\d{5}(?:19|20)\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\d|3[01])\d{3}[0-9Xx](?!\d)')
PRIVATE_KEY = re.compile(r'-----BEGIN (?:RSA |EC |OPENSSH |DSA )?PRIVATE KEY-----')
SENSITIVE_KEY = r'[\w.-]*(?:password|passwd|secret|token|appkey|apikey|private.?key|pwd)[\w.-]*'
# Java, properties, JSON and YAML assignment syntax; XML attributes are checked separately.
ASSIGNMENT = re.compile(r'(?<![\w.-])(' + SENSITIVE_KEY + r')\s*(?:=|:)\s*(?:"([^"\r\n]*)"|\x27([^\x27\r\n]*)\x27|([^\s,;#]+))', re.I)
XML_ATTRIBUTE = re.compile(r'\b(' + SENSITIVE_KEY + r')\s*=\s*(["\x27])([^"\x27]*)\2', re.I)
PLACEHOLDER = re.compile(r'^(?:CHANGE_ME(?:\b|_.*)|your_.*|example.*|placeholder.*|\$\{.*\}|\{\{.*\}\}|<.*>|null|none|false|true)$', re.I)
ALLOWED_ID = '110101199001010015'  # documented synthetic fixture
SOURCE_SUFFIXES = {'.java', '.xml', '.properties', '.example', '.txt', '.yml', '.yaml', '.json', '.jsp'}
ASSIGN_SUFFIXES = {'.java', '.xml', '.properties', '.example', '.yml', '.yaml', '.json'}


def sensitive_literal(value):
    value = value.strip().strip('"\x27')
    return bool(value and not PLACEHOLDER.fullmatch(value) and not value.startswith(('#{', '//', '/*')))


def findings(name, text):
    path = pathlib.PurePosixPath(name)
    issues = []
    if path.name in {'config.properties', 'ds.xml'} and '/src/main/resources/' in '/' + name:
        issues.append((name, 0, 'local configuration is tracked'))
        return issues
    if path.suffix not in SOURCE_SUFFIXES:
        return issues
    for number, line in enumerate(text.splitlines(), 1):
        if PRIVATE_KEY.search(line):
            issues.append((name, number, 'private-key marker'))
        if any(m.group() != ALLOWED_ID for m in ID.finditer(line)):
            issues.append((name, number, 'ID-shaped value'))
        if path.suffix not in ASSIGN_SUFFIXES or line.lstrip().startswith(('//', '*', '#', '<!--')):
            continue
        if path.suffix == '.xml':
            values = [(m.group(1), m.group(3)) for m in XML_ATTRIBUTE.finditer(line)]
        else:
            # Match Java declarations and property assignments, not quoted message fragments.
            candidate = line
            declaration = re.search(r'\bString\s+(\w+)\s*=', candidate) if path.suffix == '.java' else None
            matches = list(ASSIGNMENT.finditer(candidate))
            if path.suffix == '.java':
                matches = [m for m in matches if declaration and m.group(1) == declaration.group(1)]
            # Java expressions are runtime values, not hardcoded secrets.
            values = [(m.group(1), m.group(2) or m.group(3) or (m.group(4) if path.suffix != '.java' else '') or '') for m in matches]
        if any(sensitive_literal(v) and not (k.lower() == 'tokenname' and v.endswith(':') or k.lower() == 'net_password' and v == 'net_password' or k.lower() == 'secretwk' and all(t in v for t in ('bank_code', 'zdh', 'shh'))) for k, v in values):
            issues.append((name, number, 'hardcoded sensitive assignment'))
    return issues


def main():
    names = subprocess.check_output(['git', 'ls-files', '-z'], cwd=ROOT).decode('utf-8', 'surrogateescape').split('\0')
    issues = []
    for name in filter(None, names):
        file_path = ROOT / name
        if not file_path.is_file():
            continue
        # Do not read unrelated assets or large binary blobs.
        if file_path.suffix not in SOURCE_SUFFIXES and file_path.name not in {'config.properties', 'ds.xml'}:
            continue
        text = file_path.read_text(encoding='utf-8', errors='replace')
        issues.extend(findings(name, text))
    for name, number, reason in issues:
        print(f'{name}:{number}: {reason}', file=sys.stderr)
    print(f'Scanned {len(names) - 1} tracked paths; {len(issues)} potential leak(s).')
    return bool(issues)


if __name__ == '__main__':
    sys.exit(main())
