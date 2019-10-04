create table PERSON
(
    ID INTEGER not null,
    NAME VARCHAR(255) not null,
    LOCATION VARCHAR(255),
    BIRTH_DATE timestamp,
    primary key(id)
);

INSERT INTO PERSON (ID, NAME, LOCATION, BIRTH_DATE) VALUES (10001, 'Andres', 'Medellin', sysdate());
INSERT INTO PERSON (ID, NAME, LOCATION, BIRTH_DATE) VALUES (10002, 'Felipe', 'Medellin', sysdate());
INSERT INTO PERSON (ID, NAME, LOCATION, BIRTH_DATE) VALUES (10003, 'Pablo', 'Medellin', sysdate());