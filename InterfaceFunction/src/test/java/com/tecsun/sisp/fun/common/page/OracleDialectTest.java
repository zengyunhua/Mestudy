package com.tecsun.sisp.fun.common.page;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OracleDialectTest {
    private final OracleDialect dialect = new OracleDialect();

    @Test
    public void firstPageUsesRownumLimit() {
        assertTrue(dialect.supportsLimit());
        assertEquals("select * from ( select * from records ) where rownum <= 10",
                dialect.getLimitString("  select * from records  ", 0, 10));
    }

    @Test
    public void laterPageUsesUpperAndLowerBounds() {
        assertEquals("select * from ( select row_.*, rownum rownum_ from ( select * from records ) row_ where rownum <= 5+10) where rownum_ > 5",
                dialect.getLimitString("select * from records", 5, 10));
    }

    @Test
    public void placeholdersArePreserved() {
        assertEquals("select * from ( select row_.*, rownum rownum_ from ( select * from records ) row_ where rownum <= :offset+:limit) where rownum_ > :offset",
                dialect.getLimitString("select * from records", 5, ":offset", ":limit"));
    }

    @Test
    public void forUpdateRemainsAtEndOfFirstPage() {
        assertEquals("select * from ( select * from records ) where rownum <= 10 for update",
                dialect.getLimitString("select * from records FOR UPDATE  ", 0, 10));
    }
}
