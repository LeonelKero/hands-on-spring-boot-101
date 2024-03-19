create table if not exists customer (
    id bigserial primary key,
    name text not null,
    email text not null,
    age int not null
);
-- BIGSERIAL is the same as BIGINT but back with a sequence managed by DBMS
