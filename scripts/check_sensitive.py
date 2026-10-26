#!/usr/bin/env python3
"""Reject common secret and ID leaks in the tracked working tree without printing values."""
import pathlib
import re
import subprocess
import sys

ROOT = pathlib.Path(__file__).resolve().parents[1]
FILES = subprocess.check_output(['git', 'ls-files', '-z'], cwd=ROOT).decode('utf-8', 'surrogateescape').split('\0')
ID = re.compile(r'(?<!\d)[1-9]\d{5}(?:19|20)\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\d|3[01])\d{3}[0-9Xx](?!\d)')
SECRET = re.compile(r'-----BEGIN (?:RSA |EC |OPENSSH |DSA )?PRIVATE KEY-----')
ASSIGN = re.compile(r'^\s*([^#!\s=:]+)\s*[=:]\s*(.*?)\s*$')
KEY = re.compile(r'(?:password|passwd|private.?key|api.?key|appkey|secret|token|pwd)', re.I)
PLACEHOLDER = re.compile(r'^(?:CHANGE_ME(?:\b|_.*)|your_.*|example.*|placeholder.*|\$\{.*\}|\{\{.*\}\}|<.*>)$', re.I)
ALLOWED_ID = '110101199001010015'  # documented synthetic placeholder only
TEXT_SUFFIXES = {'.java', '.xml', '.properties', '.example', '.txt', '.yml', '.yaml', '.json'}
errors = []
for name in filter(None, FILES):
    path = pathlib.PurePosixPath(name)
    if path.name in {'config.properties', 'ds.xml'} and 'src/main/resources' in name:
        errors.append((name, 0, 'local configuration is tracked'))
        continue
    if path.suffix not in TEXT_SUFFIXES or not (ROOT / name).is_file():
        continue
    text = (ROOT / name).read_text(encoding='utf-8', errors='replace')
    for line_number, line in enumerate(text.splitlines(), 1):
        if SECRET.search(line):
            errors.append((name, line_number, 'private-key marker'))
        if path.suffix in {'.java', '.xml', '.properties', '.example', '.txt'}:
            if any(match.group() != ALLOWED_ID for match in ID.finditer(line)):
                errors.append((name, line_number, 'ID-shaped value'))
        if path.name.endswith('.example'):
            match = ASSIGN.match(line)
            if match and KEY.search(match.group(1)):
                value = match.group(2).strip().strip('"\'')
                if value and not PLACEHOLDER.fullmatch(value):
                    errors.append((name, line_number, 'non-placeholder sensitive template value'))
for name, line_number, reason in errors:
    print(f'{name}:{line_number}: {reason}', file=sys.stderr)
print(f'Scanned {len(FILES) - 1} tracked paths; {len(errors)} potential leak(s).')
sys.exit(bool(errors))
