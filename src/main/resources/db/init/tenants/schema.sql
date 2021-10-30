create table if not exists app_setting
(
    id   bigserial   not null
        constraint order_pkey
            primary key,
    key varchar(50) not null,
    value varchar(50) not null,
    type varchar(100) not null
);
