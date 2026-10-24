/**
 * Transport objects exchanged with clients: request beans, result envelopes and view
 * objects.
 *
 * <p>These classes define the wire format. They are deliberately separate from
 * {@code com.tecsun.sisp.iface.server.entity}, which holds classes shaped like database
 * rows — some of which carry internal identifiers that must not leave the service.
 *
 * <h2>Core types</h2>
 *
 * <ul>
 *   <li>{@link com.tecsun.sisp.iface.common.vo.Result} — the envelope returned by every
 *       endpoint. {@code statusCode} is {@code "200"} or {@code "0"}, as a String.</li>
 *   <li>{@link com.tecsun.sisp.iface.common.vo.faceverify.ResultVerify} — the envelope
 *       plus a {@code total} count, used by paged endpoints.</li>
 *   <li>{@link com.tecsun.sisp.iface.common.vo.Page} — paging metadata and rows.</li>
 *   <li>{@code SecQueryBean} — the common request bean for social-insurance queries
 *       (id number, name, channel code).</li>
 * </ul>
 *
 * <h2>Sub-packages</h2>
 *
 * <p>Grouped by domain: {@code archive}, {@code card}, {@code cssp},
 * {@code employment}, {@code faceverify}, {@code his}, {@code his/zx}, {@code mess},
 * {@code xgcard}.
 *
 * <p>Adding a field here changes the public API. Renaming or removing one is a breaking
 * change for every embedded client — the SDK jar is versioned separately from the WAR.
 */
package com.tecsun.sisp.iface.common.vo;
