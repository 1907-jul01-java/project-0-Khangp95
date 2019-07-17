drop table if exists users;
drop table if exists account;
drop table if exists userAccounts;
drop table if exists application;

create table users(
    username text primary key,
    password text,
    userType text
);

create table account(
    id serial primary key,
    number integer,
    balance integer
);

create table userAccounts(
    id serial primary key,
    username text,
    accountnumber integer
);

create table application(
    id serial primary key,
    username text
);



insert into users(username,password,usertype) values('employee','password','employee');
insert into users(username,password,usertype) values('admin','password','admin');