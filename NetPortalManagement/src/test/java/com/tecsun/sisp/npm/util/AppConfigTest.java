package com.tecsun.sisp.npm.util;

import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Covers the resolution order and the fail-fast contract of {@link AppConfig}.
 *
 * <p>The property file itself is not exercised here: it is git-ignored, so a
 * checked-out tree has none on the test classpath. What matters for the rewrite is
 * that a system property wins, that a blank value is treated as absent, and that a
 * required key throws instead of returning null the way the old Bee loader did.
 */
public class AppConfigTest {

    private static final String KEY = "npm.test.key";

    @After
    public void clearProperty() {
        System.clearProperty(KEY);
    }

    @Test
    public void defaultValueIsUsedWhenTheKeyIsAbsent() {
        assertEquals("fallback", AppConfig.get(KEY, "fallback"));
    }

    @Test
    public void systemPropertyOverridesTheDefault() {
        System.setProperty(KEY, "from-system");
        assertEquals("from-system", AppConfig.get(KEY, "fallback"));
    }

    @Test
    public void blankValueIsTreatedAsAbsent() {
        System.setProperty(KEY, "   ");
        assertEquals("fallback", AppConfig.get(KEY, "fallback"));
    }

    @Test
    public void surroundingWhitespaceIsTrimmed() {
        System.setProperty(KEY, "  trimmed  ");
        assertEquals("trimmed", AppConfig.get(KEY, null));
    }

    @Test
    public void requiredReturnsTheValueWhenPresent() {
        System.setProperty(KEY, "present");
        assertEquals("present", AppConfig.required(KEY));
    }

    @Test
    public void requiredThrowsWhenTheKeyIsMissing() {
        try {
            AppConfig.required(KEY);
            fail("expected IllegalStateException for a missing required key");
        } catch (IllegalStateException expected) {
            assertTrue("message should name the key, was: " + expected.getMessage(),
                    expected.getMessage().contains(KEY));
        }
    }
}
