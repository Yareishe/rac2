create table Note
(
    id serial primary key ,
    title varchar(255),
    content varchar(2000)

);
create table users
(
    id int primary key,
    username varchar(255) not null,
    password varchar(255) not null
);