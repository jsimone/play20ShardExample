create table contacts (id integer, firstName varchar, lastName varchar, phone varchar, address varchar, zip varchar);
alter table contacts alter column id set not null;
CREATE SEQUENCE contactId START 1;
alter table contacts alter column id set default nextval('contactId');