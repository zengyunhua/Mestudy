package com.tecsun.sisp.iface;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Startup check for applicationContext.xml.
 *
 * <p>Two mistakes in this file package cleanly and only surface after
 * deployment: a datasource wired to another schema's {@code ${jdbc.*}} key, and
 * a mapper XML missing from a factory's {@code mapperLocations}. The first
 * shows up as missing rows, the second as
 * {@code Invalid bound statement (not found)} on the first call. This test
 * catches both at build time.
 *
 * <p>No database is required. c3p0 validates the driver class but acquires
 * connections lazily - src/test/resources/config.properties sets
 * initialPoolSize=0 - and MyBatis parses mapper XML while building its
 * Configuration. Nothing here runs a query.
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

    /** Each pool must read its own jdbc.&lt;schema&gt;.url, not a neighbour's. */
    @Test
    public void eachDataSourceReadsItsOwnUrlKey() {
        assertUrl("dataSource", "jdbc:oracle:thin:@127.0.0.1:1521:SISP");
        assertUrl("drDataSource", "jdbc:oracle:thin:@127.0.0.1:1521:NCCOLLECT");
    }

    /**
     * Every method of every scanned @MyBatisDao interface must resolve to a
     * statement in the factory its scanner is bound to. Checked by reflection,
     * so adding a DAO method without its XML fails here, not in production.
     */
    @Test
    public void everyScannedMapperMethodIsBound() {
        List<String> unbound = unboundMapperMethods();
        assertTrue("Mapper methods with no bound statement: " + unbound, unbound.isEmpty());
    }

    /** Guards the check above: if the scan found nothing it would pass vacuously. */
    @Test
    public void scannerProducesMapperBeans() {
        assertTrue("no MapperFactoryBean found - the binding check is not checking anything",
                mapperInterfaces().contains("com.tecsun.sisp.iface.server.model.dao.SecQueryDAO"));
    }

    /**
     * authenticationDAO.xml is namespaced to a service impl rather than a DAO
     * interface; AuthenticationServiceImpl reaches it through the
     * SqlSessionTemplate. No scanner covers it, so check it directly.
     */
    @Test
    public void templateOnlyStatementsAreBound() {
        assertStatement("drSqlSessionFactory",
                "com.tecsun.sisp.iface.server.model.service.AuthenticationServiceImpl.saveAuthenticationDatas");
    }

    private void assertUrl(String beanName, String expectedUrl) {
        ComboPooledDataSource ds = ctx.getBean(beanName, ComboPooledDataSource.class);
        assertEquals(beanName + " is wired to the wrong jdbc.*.url key",
                expectedUrl, ds.getJdbcUrl());
    }

    private void assertStatement(String factoryBeanName, String statementId) {
        SqlSessionFactory factory = ctx.getBean(factoryBeanName, SqlSessionFactory.class);
        assertTrue(statementId + " is not bound in " + factoryBeanName,
                factory.getConfiguration().hasStatement(statementId));
    }

    /**
     * Mirrors MyBatis' own lookup in MapperMethod.SqlCommand: the statement id
     * is the mapper interface name plus the method name, falling back to the
     * declaring interface for inherited methods.
     */
    private static List<String> unboundMapperMethods() {
        List<String> unbound = new ArrayList<String>();
        for (MapperFactoryBean<?> factoryBean : mapperFactoryBeans()) {
            // getObjectType() is the mapper interface; mybatis-spring 1.2.x has no getMapperInterface().
            Class<?> mapper = factoryBean.getObjectType();
            Configuration configuration = factoryBean.getSqlSession().getConfiguration();
            for (Method method : mapper.getMethods()) {
                String id = mapper.getName() + "." + method.getName();
                String inherited = method.getDeclaringClass().getName() + "." + method.getName();
                if (!configuration.hasStatement(id) && !configuration.hasStatement(inherited)) {
                    unbound.add(id);
                }
            }
        }
        return unbound;
    }

    private static List<String> mapperInterfaces() {
        List<String> names = new ArrayList<String>();
        for (MapperFactoryBean<?> factoryBean : mapperFactoryBeans()) {
            names.add(factoryBean.getObjectType().getName());
        }
        return names;
    }

    private static List<MapperFactoryBean<?>> mapperFactoryBeans() {
        List<MapperFactoryBean<?>> beans = new ArrayList<MapperFactoryBean<?>>();
        for (String name : ctx.getBeanNamesForType(MapperFactoryBean.class)) {
            // "&" asks for the FactoryBean itself rather than the mapper proxy it produces.
            String factoryName = name.startsWith("&") ? name : "&" + name;
            beans.add((MapperFactoryBean<?>) ctx.getBean(factoryName));
        }
        return beans;
    }
}
