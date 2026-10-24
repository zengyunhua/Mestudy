/**
 * HTTP entry points for the online government-service portal.
 *
 * <h2>Groups</h2>
 *
 * <table>
 *   <caption>Controller groups</caption>
 *   <tr><th>Package</th><th>Base path</th><th>Domain</th></tr>
 *   <tr><td>{@code business}</td><td>{@code /net/business}</td><td>Business handling</td></tr>
 *   <tr><td>{@code common}</td><td>—</td><td>{@code CommonExceptionHandler}, shared params</td></tr>
 *   <tr><td>{@code gov}</td><td>{@code /adapter/notice}</td><td>Published government notices</td></tr>
 *   <tr><td>{@code sign}</td><td>{@code /adapter/sign}</td><td>Electronic signature (PDF stamping)</td></tr>
 *   <tr><td>{@code userNet}</td><td>{@code /userNet/drafts}, {@code /userNet/audit}</td><td>Application drafts and back-office audit</td></tr>
 *   <tr><td>(this package)</td><td>{@code /net/query}</td><td>{@code QueryController}, portal queries</td></tr>
 * </table>
 *
 * <h2>Draft lifecycle</h2>
 *
 * <p>The core flow is create → edit → attach → submit → audit → signed PDF, with reject
 * looping back to edit. There is no workflow engine: the state machine is a status column
 * plus guards in the service layer. When changing a transition, change it in
 * {@code service/business} and verify the guard, not just the controller mapping.
 *
 * <h2>Error handling</h2>
 *
 * <p>{@code CommonExceptionHandler} converts unhandled exceptions into a {@code Result}
 * with a failure code, so a 500 from this module usually means the handler itself failed.
 *
 * <h2>Conventions</h2>
 *
 * <p>Same as the rest of the workspace: extend {@code BaseController}, return the
 * envelope, never a raw object.
 */
package com.tecsun.sisp.net.modules.controller;
