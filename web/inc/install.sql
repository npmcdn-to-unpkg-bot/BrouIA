drop table if exists messages;
drop table if exists users;
drop table if exists diesAmbPes;

create table users
(
    user_name varchar(100) primary key,
    creation_time int(10),
    hashed_pass varchar(128)
);

create table messages 
(
    user_to varchar(100),
    user_from varchar(100),
    message varchar(10000),
    is_readed boolean,
    creation_time int(10),
    primary key(user_to, user_from, creation_time),
    foreign key(user_to) references users(user_name),
    foreign key(user_from) references users(user_name)
);

create table diesAmbPes 
(
    dia varchar(10), 
    pes int
);

insert into diesAmbPes values 
    ('Lunes', 1),
    ('Martes', 2),
    ('Miercoles', 3),
    ('Jueves', 4), 
    ('Viernes', 5),     
    ('Sabado', 6), 
    ('Domingo', 7);