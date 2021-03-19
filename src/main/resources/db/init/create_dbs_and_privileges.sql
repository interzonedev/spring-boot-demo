DROP DATABASE IF EXISTS springbootdemo_main;
DROP DATABASE IF EXISTS springbootdemo_test;
DROP ROLE IF EXISTS springbootdemouser;

CREATE ROLE springbootdemouser WITH CREATEDB LOGIN ENCRYPTED PASSWORD 'springbootdemopass';
CREATE DATABASE springbootdemo_main WITH OWNER=springbootdemouser TEMPLATE=template0 ENCODING='UTF8' LC_COLLATE='en_US.UTF-8' LC_CTYPE='en_US.UTF-8';
CREATE DATABASE springbootdemo_test WITH OWNER=springbootdemouser TEMPLATE=template0 ENCODING='UTF8' LC_COLLATE='en_US.UTF-8' LC_CTYPE='en_US.UTF-8';
