/**
 * MyBatis mapper interfaces for the core platform schema.
 *
 * <p><strong>Packages here are split by datasource, not by domain.</strong> Each
 * {@code dao*} package is bound to its own {@code MapperScannerConfigurer} and
 * {@code SqlSessionFactory} in {@code config/applicationContext.xml}. Putting a mapper in
 * the wrong package is the most common cause of a "no statement found" error at runtime,
 * because the scanner for the datasource you are actually querying never sees it.
 *
 * <table>
 *   <caption>Mapper packages and their datasources</caption>
 *   <tr><th>Package</th><th>Datasource bean</th><th>Schema</th></tr>
 *   <tr><td>{@code model.dao}</td><td>{@code dataSource}</td><td>Neusoft five-insurance</td></tr>
 *   <tr><td>{@code model.dao_cssp}</td><td>{@code csspDataSource}</td><td>Card replacement hub</td></tr>
 *   <tr><td>{@code model.dao_dr}</td><td>{@code drDataSource}</td><td>Neusoft face recognition</td></tr>
 *   <tr><td>{@code model.dao_net}</td><td>{@code netportalDataSource}</td><td>Core platform</td></tr>
 *   <tr><td>{@code model.dao_sisp_public}</td><td>{@code sispPublicDataSource}</td><td>Public queries</td></tr>
 *   <tr><td>{@code model.dao_xg}</td><td>{@code faceDataSource}</td><td>Face-verification base data</td></tr>
 *   <tr><td>{@code model.dao_yldygxhcs}</td><td>{@code dryldyDataSource}</td><td>Pension calculation</td></tr>
 *   <tr><td>{@code model.dao_zj}</td><td>{@code zhongjianDataSource}</td><td>Intermediate / staging</td></tr>
 * </table>
 *
 * <h2>Mapper rules</h2>
 *
 * <ul>
 *   <li>The XML {@code namespace} in {@code resources/mappings/*.xml} must equal the
 *       interface's fully-qualified name.</li>
 *   <li>Every statement {@code id} must match a method name.</li>
 *   <li>Use bound {@code #{}} parameters. MyBatis string substitution (dollar-brace) is
 *       not used anywhere in this workspace and must not be introduced — it is an
 *       injection vector.</li>
 *   <li>List columns explicitly. Several tables carry {@code BLOB} columns that will be
 *       pulled into memory by {@code SELECT *}.</li>
 *   <li>Transactions do not span datasources. A flow writing to two schemas is eventually
 *       consistent; make it idempotent.</li>
 * </ul>
 */
package com.tecsun.sisp.iface.server.model.dao;
