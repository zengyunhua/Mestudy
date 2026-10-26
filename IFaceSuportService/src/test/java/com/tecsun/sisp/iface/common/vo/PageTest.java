package com.tecsun.sisp.iface.common.vo;

import java.util.Arrays;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PageTest {
    @Test
    public void nonPositivePageSizeFallsBackToTen() {
        Page<String> page = new Page<String>();
        page.setPageSize(0);
        assertEquals(10, page.getPageSize());
        page.setPageSize(-1);
        assertEquals(10, page.getPageSize());
        page.setPageSize(25);
        assertEquals(25, page.getPageSize());
    }

    @Test
    public void countWithinPageResetsPageNumber() {
        Page<String> page = new Page<String>(4, 10);
        page.setCount(7);
        assertEquals(1, page.getPageNo());
        assertEquals(7L, page.getCount());
    }

    @Test
    public void constructorRetainsRowsAndPagingValues() {
        Page<String> page = new Page<String>(2, 5, 12, Arrays.asList("a", "b"));
        assertEquals(2, page.getPageNo());
        assertEquals(5, page.getPageSize());
        assertEquals(12L, page.getCount());
        assertEquals(Arrays.asList("a", "b"), page.getData());
    }
}