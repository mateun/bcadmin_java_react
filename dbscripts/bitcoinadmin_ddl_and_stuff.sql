CREATE TABLE "customer" (
	Id INT NOT NULL identity(1, 1) primary key,
	first_name varchar(50)  NOT NULL,
	last_name varchar(50) NOT NULL,
	user_name varchar(50) NOT NULL,
	password varchar(256) NOT NULL,
	creation_date datetime NOT NULL default CURRENT_TIMESTAMP,
)
;

delete from customer;

alter table customer add password varchar(256) not null;
alter table customer alter column password varchar(256) not null;