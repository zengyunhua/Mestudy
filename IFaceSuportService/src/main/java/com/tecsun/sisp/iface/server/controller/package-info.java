/**
 * HTTP entry points, one controller per business domain.
 *
 * <h2>Conventions</h2>
 *
 * <ul>
 *   <li>Every controller extends {@link com.tecsun.sisp.iface.server.controller.BaseController}
 *       and returns {@code Result} or {@code ResultVerify} built by its helpers.</li>
 *   <li>All controllers are registered by the servlet context
 *       ({@code springMVC-servlet.xml}) through a component scan restricted to
 *       {@code @Controller}. Services and DAOs are <em>not</em> picked up here — they come
 *       from the root context.</li>
 *   <li>URLs are lower camel case and verb-first: {@code /iface/cardInfo},
 *       {@code /iface/rest}, {@code /iface/his/zhongxinhis}.</li>
 * </ul>
 *
 * <h2>Sub-packages</h2>
 *
 * <table>
 *   <caption>Controller groups</caption>
 *   <tr><th>Package</th><th>Domain</th></tr>
 *   <tr><td>{@code archive}</td><td>Archive lookup</td></tr>
 *   <tr><td>{@code bank}</td><td>Bank channel, card binding, payment</td></tr>
 *   <tr><td>{@code card}</td><td>Social-security card</td></tr>
 *   <tr><td>{@code cssp}</td><td>Card replacement hub</td></tr>
 *   <tr><td>{@code employment}</td><td>Job seekers, vacancies, employment queries</td></tr>
 *   <tr><td>{@code faceManager}</td><td>Face enrolment and verification</td></tr>
 *   <tr><td>{@code fee}</td><td>Fee payment and reconciliation</td></tr>
 *   <tr><td>{@code his}</td><td>Hospital registration, per HIS vendor</td></tr>
 *   <tr><td>{@code security}</td><td>Login, token, captcha, policy documents</td></tr>
 *   <tr><td>{@code sms}</td><td>SMS dispatch</td></tr>
 *   <tr><td>{@code so}</td><td>Social-insurance queries (the largest controller)</td></tr>
 *   <tr><td>{@code wechat}</td><td>WeChat channel</td></tr>
 *   <tr><td>{@code yldy}</td><td>Pension benefit calculation</td></tr>
 * </table>
 *
 * <h2>Adding an endpoint</h2>
 *
 * <ol>
 *   <li>Return the envelope, never a raw object or a {@code Map}.</li>
 *   <li>Wrap outbound calls so a downstream failure becomes a failure code rather than an
 *       unhandled exception.</li>
 *   <li>Add the mapping to {@code docs/API_OVERVIEW.md} if you introduce a new base
 *       path.</li>
 * </ol>
 */
package com.tecsun.sisp.iface.server.controller;
