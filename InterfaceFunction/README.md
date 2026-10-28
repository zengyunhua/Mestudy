# InterfaceFunction

Platform security administration for the interface layer: which *channel user* may call
which *function*, under which *role*.

This is the smallest self-contained module in the workspace and the easiest one to build
and read. If you want to understand the conventions used everywhere else, start here.

| | |
| --- | --- |
| Artifact | `com.tecsun.sisp:InterfaceFunction` |
| Packaging | `war` |
| Version | `V1.0.0.150630` |
| Java files | 49 |
| Endpoints | 27 |
| Datasources | 1 |

---

## Responsibility

- **Interface users** — the accounts that external channels (terminals, portals) use when
  calling the platform. Distinct from citizen identities.
- **Roles** — groups of function permissions, assignable to users.
- **Functions** — the permission tree: each callable interface is a node.
- **Common lookups** — shared dropdown/validation data.

## Domain model

```
UserIfaceVO ──< RoleUserIfaceVO >── RoleIfaceVO ──< AppRoleIfaceVO >── AppIfaceVO
   (user)                              (role)                            (function)
```

| Entity | Table concept | Notes |
| --- | --- | --- |
| `UserIfaceVO` | interface user | Has credentials and a channel code |
| `InterfaceUserBean` | user, request shape | Used for create/update payloads |
| `RoleIfaceVO` | role | Name, description |
| `RoleUserIfaceVO` | user↔role link | Many-to-many join |
| `AppIfaceVO` | function / permission node | Tree node with parent id |
| `AppRoleIfaceVO` | role↔function link | Many-to-many join |

The `App*` prefix is historical: these were "application interfaces" before the model
was generalised to functions.

## Layout

```
com/tecsun/sisp/fun/
├── bean/                        # request/response beans
├── common/
│   ├── page/                    # paging
│   └── CommUtil.java, DefualtUseData.java
└── modules/
    ├── controller/
    │   ├── common/              # CommonController (+ param/)
    │   ├── func/                # FuntionController  (note: single 'n')
    │   ├── role/                # RoleController
    │   └── user/                # UserController
    ├── dao/ + dao/impl/         # no MyBatis mappers — see note below
    ├── entity/func, role, user
    ├── exception/
    └── service/ + service/impl/
```

> `FuntionController` really is spelled with one *n*. Grep both spellings when searching.

### Persistence note

The DAO layer has an unusual shape worth knowing before you open a file:

```
modules/dao/
├── BaseDAO.java                 # generic CRUD contract: get / findList / insert / update / delete
└── impl/
    ├── BaseDAOImpl.java         # abstract base, mostly no-op defaults to override
    ├── FuncDAO.java             # @MyBatisDao mapper interface
    ├── RoleDAO.java             # @MyBatisDao mapper interface
    └── UserDAO.java             # @MyBatisDao mapper interface
```

Mapper **interfaces** live in `impl/` alongside the base implementation — they are not in
`dao/`. They are marked with the project's own `@MyBatisDao` annotation
(`fun/common/MyBatisDao.java`) rather than Spring's `@Mapper`, and are bound to the
mapper XML in `src/main/resources/mappings/` (`funcDAO.xml`, `RoleDAO.xml`,
`UserDAO.xml`). The XML `namespace` must match the interface's fully-qualified name,
which is `...modules.dao.impl.RoleDAO`, not `...modules.dao.RoleDAO`.

## Endpoints

| Base path | Controller | Endpoints | Domain |
| --- | --- | :---: | --- |
| `/interfacefunction/user` | `UserController` | 11 | User CRUD, role assignment |
| `/interfacefunction/role` | `RoleController` | 7 | Role CRUD, function assignment |
| `/interfacefunction/app` | `FuntionController` | 6 | Function tree CRUD |
| `/ans/common` | `CommonController` | 3 | Shared lookups |

Path-variable style is used for deletes:

```
DELETE /interfacefunction/role/{id}
DELETE /interfacefunction/user/{userids}
```

`{userids}` accepts a comma-separated list — a deliberate convenience that also means the
path can become very long. Keep batches modest.

## Configuration

Single datasource, unprefixed keys:

```properties
jdbc.driverClassName=oracle.jdbc.driver.OracleDriver
jdbc.url=jdbc:oracle:thin:@127.0.0.1:1521:orcl
jdbc.username=CHANGE_ME
jdbc.password=CHANGE_ME
```

Plus the common platform keys (`platform_context_path`, `inner_proxy_ip`,
`inner_proxy_port`, `redis_core_database`).

## Build

```bash
cd InterfaceFunction
cp src/main/resources/config.properties.example src/main/resources/config.properties
mvn clean package
```

```bash
cp target/InterfaceFunction-*.war $CATALINA_HOME/webapps/sisp.war
```

## Notes for contributors

- **This module is the best entry point for a first contribution.** One datasource, no
  external gateways, 49 Java files.
- **`javax.servlet:servlet-api` and `spring-test` must stay out of the WAR.** Both were at
  compile scope, so both were packaged into `WEB-INF/lib`; Tomcat warns and skips the
  servlet jar, and other containers can fail at startup with a `ClassCastException` on
  `javax.servlet` types. They are `provided` and `test` respectively. If you add a
  dependency that a container supplies, scope it.
- **Never point `<mvc:resources>` at `WEB-INF/` itself.** The `/views/**` mapping used
  `location="WEB-INF/"`, which serves any path beneath it — `/views/classes/config.properties`
  returned the datasource credentials and `/views/lib/*.jar` returned the module's jars.
  It is scoped to `/WEB-INF/views/`, matching the view resolver's prefix, and the duplicate
  `WEB-INF/jquery.js` that only existed to satisfy the old location is gone. A resource
  handler bypasses the container's own `WEB-INF` protection, so the location must name a
  directory, never the parent.
- **Authorisation is data-driven, not enforced by an interceptor.** The module can answer
  "may this interface user call this function?" via
  `UserServiceImpl.checkInterfaceUserRoleFunc(...)`, but there is no module-wide
  interceptor applying it. Each endpoint that needs the check must call it.
- **Role deletion is only half-cascading.** `deleteRole` clears the role row and its
  role→function links (`RoleService.delete` + `RoleService.deleteAppRole`), but **not**
  the user→role links in `RoleUserIfaceVO`. Orphan rows accumulate unless the caller
  cleans them up first. Fixing this is a good first issue.
