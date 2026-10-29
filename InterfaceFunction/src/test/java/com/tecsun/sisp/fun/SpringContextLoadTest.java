package com.tecsun.sisp.fun;

import org.apache.ibatis.session.Configuration;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Startup check for applicationContext.xml.
 *
 * <p>mapperLocations is a {@code classpath:/mappings/*.xml} glob, so a mapper
 * file is never left out here. What can still go wrong is a DAO method with no
 * statement, or a mapper whose namespace does not match its interface. Both
 * package cleanly and fail on the first call with
 * {@code Invalid bound statement (not found)}. This test catches them at build
 * time.
 *
 * <p>No database is required. The datasource connects lazily and MyBatis
 * parses mapper XML while building its Configuration. Nothing here runs a
 * query.
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

    @Test
    public void dataSourceReadsJdbcUrl() {
        DriverManagerDataSource ds = ctx.getBean("dataSource", DriverManagerDataSource.class);
        assertEquals("jdbc:oracle:thin:@127.0.0.1:1521:FUNC", ds.getUrl());
    }

    /**
     * Every method of every scanned @MyBatisDao interface must resolve to a
     * statement. Checked by reflection, so adding a DAO method without its XML
     * fails here, not in production.
     */
    @Test
    public void everyScannedMapperMethodIsBound() {
        List<String> unbound = unboundMapperMethods();
        assertTrue("Mapper methods with no bound statement: " + unbound, unbound.isEmpty());
    }

    /** Guards the check above: if the scan found nothing it would pass vacuously. */
    @Test
    public void scannerProducesMapperBeans() {
        List<String> mappers = mapperInterfaces();
        assertTrue("UserDAO not scanned: " + mappers,
                mappers.contains("com.tecsun.sisp.fun.modules.dao.impl.UserDAO"));
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
