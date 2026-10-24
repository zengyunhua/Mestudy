/**
 * Platform security administration endpoints: interface users, roles and the function
 * permission tree.
 *
 * <h2>Groups</h2>
 *
 * <table>
 *   <caption>Controller groups</caption>
 *   <tr><th>Package</th><th>Base path</th><th>Domain</th></tr>
 *   <tr><td>{@code user}</td><td>{@code /interfacefunction/user}</td><td>Interface users, role assignment</td></tr>
 *   <tr><td>{@code role}</td><td>{@code /interfacefunction/role}</td><td>Roles, function assignment</td></tr>
 *   <tr><td>{@code func}</td><td>{@code /interfacefunction/app}</td><td>Function / permission tree</td></tr>
 *   <tr><td>{@code common}</td><td>{@code /ans/common}</td><td>Shared lookups</td></tr>
 * </table>
 *
 * <h2>Authorisation</h2>
 *
 * <p>Authorisation is data-driven, not enforced by an interceptor. The model can answer
 * "may this interface user call this function?" via
 * {@code UserServiceImpl.checkInterfaceUserRoleFunc(...)}, but no module-wide filter
 * applies it. Each endpoint that needs the check must call it explicitly.
 *
 * <h2>Deletion semantics</h2>
 *
 * <p>Deleting a role removes the role row and its role→function links, but not the
 * user→role links. Orphan {@code RoleUserIfaceVO} rows accumulate unless the caller
 * cleans them up first.
 *
 * <h2>Naming</h2>
 *
 * <p>{@code FuntionController} is spelled with one {@code n}. Grep both spellings.
 */
package com.tecsun.sisp.fun.modules.controller;
