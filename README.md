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
