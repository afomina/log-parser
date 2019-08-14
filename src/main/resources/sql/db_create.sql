create database parser;

create table log
(
    id         bigint not null auto_increment primary key,
    date       datetime,
    ip_address varchar(50) not null,
    request    text,
    status     integer,
    user_agent text
);

create table blocked_entity
(
    id         bigint not null auto_increment primary key,
    ip_address varchar(50) not null unique,
    reason     text
);