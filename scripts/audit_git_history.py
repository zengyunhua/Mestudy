#!/usr/bin/env python3
"""Inventory historical risk categories without emitting historical file contents.

Run locally from a full clone. Findings are heuristic, not proof of real credentials
or identity data. This tool never rewrites history or contacts credential providers.
"""
from collections import Counter
from pathlib import PurePosixPath
import subprocess
import sys

from check_sensitive import ASSIGN_SUFFIXES, SOURCE_SUFFIXES, findings

MAX_BLOB_BYTES = 2 * 1024 * 1024


def candidates(lines):
    """Return unique blob/path pairs, excluding binary and irrelevant extensions."""
    seen = set()
    for line in lines.splitlines():
        oid, separator, raw_path = line.partition(b' ')
        if not separator or not raw_path:
            continue
        path = raw_path.decode('utf-8', 'surrogateescape')
        if PurePosixPath(path).suffix not in SOURCE_SUFFIXES and PurePosixPath(path).name not in {'config.properties', 'ds.xml'}:
            continue
        pair = (oid, path)
        if pair not in seen:
            seen.add(pair)
            yield pair


def classify(path, text):
    """Return only reason labels; never return snippets or matched values."""
    return {reason for _, _, reason in findings(path, text)}


def audit(root):
    listing = subprocess.check_output(['git', 'rev-list', '--objects', '--all'], cwd=root)
    checked = 0
    omitted = 0
    counts = Counter()
    with subprocess.Popen(['git', 'cat-file', '--batch'], cwd=root, stdin=subprocess.PIPE,
                          stdout=subprocess.PIPE, stderr=subprocess.DEVNULL) as process:
        for oid, path in candidates(listing):
            process.stdin.write(oid + b'\n')
            process.stdin.flush()
            header = process.stdout.readline().strip().split()
            if len(header) != 3 or header[1] != b'blob':
                raise RuntimeError('Could not inspect historical Git object')
            size = int(header[2])
            if size > MAX_BLOB_BYTES:
                omitted += 1
                # Consume oversized objects to keep the batch stream aligned.
                remaining = size + 1
                while remaining:
                    block = process.stdout.read(min(65536, remaining))
                    if not block:
                        raise RuntimeError('Incomplete historical Git object')
                    remaining -= len(block)
                continue
            data = process.stdout.read(size)
            if len(data) != size or process.stdout.read(1) != b'\n':
                raise RuntimeError('Incomplete historical Git object')
            checked += 1
            if b'\x00' in data:
                omitted += 1
                continue
            # A path may be renamed over time; counts describe blob/path occurrences,
            # not unique people, credentials or commits.
            for reason in classify(path, data.decode('utf-8', 'replace')):
                counts[reason] += 1
        process.stdin.close()
    return checked, omitted, counts


if __name__ == '__main__':
    try:
        checked, omitted, counts = audit(__import__('pathlib').Path(__file__).resolve().parent.parent)
    except (OSError, subprocess.CalledProcessError, RuntimeError) as exc:
        print('History inventory failed (%s)' % type(exc).__name__, file=sys.stderr)
        sys.exit(2)
    print('Historical blob/path pairs inspected: %d; skipped oversized/binary: %d' % (checked, omitted))
    for reason, count in sorted(counts.items()):
        print('%s: %d blob/path pair(s)' % (reason, count))
    print('Heuristic inventory only; verify provenance privately. No historical values printed.')
