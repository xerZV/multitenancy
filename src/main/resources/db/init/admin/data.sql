insert into tenant(name)
values ('tenant1'),
       ('tenant2');

insert into tenant_datasource(tenant_id, driver_class_name, url, username, password)
values ((select id from tenant where name = 'tenant1'), 'org.h2.Driver', 'jdbc:h2:mem:tenant1', 'tenant1', 'tenant1'),
       ((select id from tenant where name = 'tenant2'), 'org.h2.Driver', 'jdbc:h2:mem:tenant2', 'tenant2', 'tenant2');
