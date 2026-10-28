package com.tecsun.sisp.net;

import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Startup check for applicationContext.xml.
 *
 * <p>The root context is the part of this module that used to fail only after
 * deployment: a typo in a ${jdbc.*} key or a mapper XML missing from
 * mapperLocations both package cleanly and then break at runtime. This test
 * loads the real context and asserts the two things that cannot be seen in a
 * successful {@code mvn package}.
 *
 * <p>No database is required. The beans are {@code DriverManagerDataSource},
 * which holds the url but connects lazily, and MyBatis parses mapper XML while
 * building its Configuration - neither needs a live server. Values come from
 * src/test/resources/config.properties, which shadows the production file.
 */
public class SpringContextLoadTest {

    private static ClassPathXmlApplicationContext ctx;

    @BeforeClass
    public static void loadContext() {
        ctx = new ClassPathXmlApplicationContext("classpath:/config/applicationContext.xml");
    }

    @AfterClass
    public static void closeContext() {
        if (ctx != null) {
            ctx.close();
        }
    }

    /**
     * Each datasource must read its own jdbc.&lt;schema&gt;.url. sispYthdataSource
     * used to read ${jdbc.sisp_public_yth.url}, so it silently queried the
     * wrong schema - the kind of bug that surfaces as missing rows, not as an
     * error.
     */
    @Test
    public void eachDataSourceReadsItsOwnUrlKey() {
        assertUrl("sispDataSource", "jdbc:oracle:thin:@127.0.0.1:1521:SISP");
        assertUrl("sisppublicDataSource", "jdbc:oracle:thin:@127.0.0.1:1521:SISPPUBLIC");
        assertUrl("sispYthdataSource", "jdbc:oracle:thin:@127.0.0.1:1521:SISPYTH");
        assertUrl("sispPublicYthDataSource", "jdbc:oracle:thin:@127.0.0.1:1521:SISPPUBLICYTH");
        assertUrl("drDataSource", "jdbc:oracle:thin:@127.0.0.1:1521:DRFACE");
    }

    /**
     * Every @MyBatisDao interface in a scanned package needs its mapper XML in
     * the matching factory's mapperLocations. PersonDAO and BusinessHandleDao
     * live in the package sispSqlSessionFactory's scanner covers, but their XML
     * was not loaded, so calls failed with "Invalid bound statement (not found)".
     */
    @Test
    public void scannedMapperStatementsAreBound() {
        assertStatement("sispSqlSessionFactory",
                "com.tecsun.sisp.net.modules.dao.drafts_sisp.DraftsDAO.findbySuccessMessage");
        assertStatement("sispSqlSessionFactory",
                "com.tecsun.sisp.net.modules.dao.drafts_sisp.PersonDAO.updatePersonMessage");
        assertStatement("sispSqlSessionFactory",
                "com.tecsun.sisp.net.modules.dao.drafts_sisp.BusinessHandleDao.getBusinessHandlePageByCondition");
        assertStatement("sispPublicSqlSessionFactory",
                "com.tecsun.sisp.net.modules.dao.area_sisppublic.AreaDAO.queryByArea");
        assertStatement("sispYthSqlSessionFactory",
                "com.tecsun.sisp.net.modules.dao.gov.GovQueryDao.getNoticeList");
        assertStatement("sispPublicYthSqlSessionFactory",
                "com.tecsun.sisp.net.modules.dao.common.CommQueryDao.getDictionaryById");
        assertStatement("drSqlSessionFactory",
                "com.tecsun.sisp.net.modules.dao.dr.QueryDao.getGrsjjl");
    }

    /** The scanners create one mapper bean per @MyBatisDao interface. */
    @Test
    public void mapperBeansAreRegistered() {
        assertTrue("DraftsDAO mapper bean missing",
                ctx.containsBean("draftsDAO"));
        assertTrue("PersonDAO mapper bean missing",
                ctx.containsBean("personDAO"));
        assertTrue("BusinessHandleDao mapper bean missing",
                ctx.containsBean("businessHandleDao"));
    }

    private void assertUrl(String beanName, String expectedUrl) {
        DriverManagerDataSource ds = ctx.getBean(beanName, DriverManagerDataSource.class);
        assertEquals(beanName + " is wired to the wrong jdbc.*.url key",
                expectedUrl, ds.getUrl());
    }

    private void assertStatement(String factoryBeanName, String statementId) {
        SqlSessionFactory factory = ctx.getBean(factoryBeanName, SqlSessionFactory.class);
        assertTrue(statementId + " is not bound in " + factoryBeanName,
                factory.getConfiguration().hasStatement(statementId));
    }
}
