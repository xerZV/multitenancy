# About The Project

Sample Multi-tenant application with a multi-tenancy strategy implemented as a separate database per tenant and 
one main database which keeps the general tenants information.

After adding a new tenant - a database and a datasource will be created automatically (at runtime - no app/server restart needed) 

# Sample requests

## Admin actions

### Get all tenants

```
curl --location --request GET 'http://localhost:8080/admin/tenants'
```

### Create a tenant

```
curl --location --request POST 'http://localhost:8080/admin/tenants' \
--header 'Content-Type: application/json' \
--data-raw '{
"name": "tenant3"
}'
```

## Tenant actions

### Get settings

```
curl --location --request GET 'http://localhost:8080/tenant/app/settings' \
--header 'X-TenantID: tenant3'
```

### Create a setting

```
curl --location --request POST 'http://localhost:8080/tenant/app/settings' \
--header 'X-TenantID: tenant3' \
--header 'Content-Type: application/json' \
--data-raw '{
"key": "theme",
"value": "light",
"type": "String"
}'
```


# TODOs

- Add Liquibase migrations
- Exception handling
- Security
  - `/admin/*` should be accessible only for users with a role/group `ADMIN` and the requests should not have `X-TenantID` header
  - `/tenant/*` should be accessible only for users with a role/group `TENANT` and the requests should have `X-TenantID` header
- Tenants DB connections management
- Tenant removal - drop db/ remove DataSource
