package com.tecsun.sisp.iface.server.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Process-wide accessor for {@code /config.properties}.
 *
 * <p>This is the non-Spring path to configuration, used by utilities that are not managed
 * beans. Spring-managed beans should use {@code ${...}} placeholders instead, which fail
 * fast at startup when a key is missing.
 *
 * <p><strong>This class does not fail on a missing key.</strong> {@link #get(String)}
 * returns {@code null}, and {@code null} concatenated into a URL silently produces a
 * broken address. If a configuration value looks empty at runtime, grep the key name
 * before suspecting the properties file.
 *
 * <p>The singleton is initialised lazily and is not synchronised; concurrent first access
 * may construct two instances. In practice the class is first touched during servlet
 * initialisation, before request handling begins.
 *
 * <p>Usage:
 * <pre>{@code
 * String host = Config.getInstance().get("inner_proxy_ip");
 * }</pre>
 */
public class Config {
    private Log log = LogFactory.getLog(Config.class);
    private static final String CONIFG_FILE = "/config.properties";
    private static Config instance = null;
    private Properties props = new Properties();

    private Config() {
        this.init();
    }

    private void init() {
        InputStream in = null;

        try {
            in = Config.class.getResourceAsStream("/config.properties");
            this.props.load(in);
        } catch (IOException var11) {
            if(this.log.isErrorEnabled()) {
                this.log.error("loading /config.properties fail!", var11);
            }
        } finally {
            if(in != null) {
                try {
                    in.close();
                } catch (IOException var10) {
                    var10.printStackTrace();
                }
            }

        }

    }

    public static Config getInstance() {
        if(instance == null) {
            instance = new Config();
        }

        return instance;
    }

    public String get(String key) {
        return this.get(key, (String)null);
    }

    public String get(String key, String defaultValue) {
        return this.props.getProperty(key, defaultValue);
    }

}
