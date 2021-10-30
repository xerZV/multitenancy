create table if not exists tenant
(
    id   bigserial   not null
        constraint tenant_pkey
            primary key,
    name varchar(50) not null
        constraint tenant_name_unique
            unique
);

create table if not exists tenant_datasource
(
    id        bigserial not null
        constraint tenant_datasource_pkey
            primary key,
    tenant_id bigint    not null
        constraint tenant_ds_fk
            references tenant (id),
    driver_class_name varchar not null,
    url varchar not null,
    username varchar not null,
    password varchar not null
);
