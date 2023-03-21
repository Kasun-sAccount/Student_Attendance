create table if not exists Student(
                                      id varchar(20) primary key ,
                                      name varchar(100) not null
);
create table if not exists Picture(
                                      student_id varchar(20) primary key ,
                                      picure MEDIUMBLOB not null ,
                                      constraint fk_picture foreign key (student_id) references Student(id)
);
create table if not exists Attendence(
                                         id int primary key auto_increment ,
                                         status ENUM('IN','OUT') not null ,
                                         stamp DATETIME not null ,
                                         student_id varchar(20) not null ,
                                         Constraint fk_attendance foreign key (student_id) references Student(id)

);
create table if not exists User(
                                   user_name varchar(50) primary key ,
                                   fill_name varchar(100) not null,
                                   password varchar(100) not null


);
