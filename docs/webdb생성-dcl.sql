
create database webdb;
create user 'webdb'@'localhost' identified by 'webdb';
grant all privileges on webdb.* to 'webdb'@'localhost';