-- v1.0.0 INITIAL CREATE DATABASE
CREATE TABLE USERS (
   ID BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
   USERNAME VARCHAR(256) NOT NULL UNIQUE,
   EMAIL VARCHAR(256) NOT NULL UNIQUE,
   PASSWORD VARCHAR(256) NOT NULL
);

-- ADD CONSTRAINTS AFTER TABLE CREATIONS
