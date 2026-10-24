package com.tecsun.sisp.iface.client;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
/**
 * ClassName: SSLClient
 * Description:
 * @since 2015-06-09
 */
public class SSLClient extends DefaultHttpClient {
    /**
     * Creates an HTTPS client backed by the JVM's default trust store.
     *
     * <p>Certificate-chain and hostname validation are intentionally left to
     * the platform defaults. Private certificate authorities must therefore be
     * imported into the runtime trust store instead of being trusted blindly.</p>
     *
     * @param port HTTPS port to register
     * @throws Exception if the default SSL socket factory cannot be created
     */
    public SSLClient(int port) throws Exception {
        super();
        SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
        socketFactory.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);

        ClientConnectionManager connectionManager = getConnectionManager();
        SchemeRegistry schemeRegistry = connectionManager.getSchemeRegistry();
        schemeRegistry.register(new Scheme("https", port, socketFactory));
    }
}