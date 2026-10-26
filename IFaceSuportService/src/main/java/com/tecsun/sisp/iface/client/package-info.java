/**
 * Client SDK for the interface service.
 *
 * <p>Everything here is designed to be embedded in a downstream application — a kiosk, a
 * channel gateway, a batch job. Nothing in this package depends on Spring, MyBatis or the
 * servlet API, and the Maven build packages it into a separate
 * {@code classifier=client} jar containing only this package and
 * {@code com.tecsun.sisp.iface.common}.
 *
 * <h2>Entry point</h2>
 *
 * <p>{@link com.tecsun.sisp.iface.client.IFaceHttpsClient} — initialise once, then call
 * the typed static operations.
 *
 * <h2>Supporting classes</h2>
 *
 * <ul>
 *   <li>{@code LoginUtil} — obtains and refreshes the {@code tokenId}</li>
 *   <li>{@code HttpClientUtil} — executes requests, marshals JSON</li>
 *   <li>{@code SSLClient} — HTTPS client using the default trust store and strict hostname verification</li>
 *   <li>{@code WebClient} — low-level HTTP helper</li>
 * </ul>
 *
 * <h2>When you add a class here</h2>
 *
 * <p>Make sure it lives under {@code client/} or {@code common/}. The client jar is
 * assembled by path include filters; a class outside those trees compiles fine but is
 * missing at runtime for SDK consumers.
 */
package com.tecsun.sisp.iface.client;
