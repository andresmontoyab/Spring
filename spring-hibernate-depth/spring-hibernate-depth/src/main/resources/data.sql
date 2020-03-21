INSERT INTO COURSE (ID, NAME, CREATED_DATE, LAST_UPDATED_DATE) VALUES (1001, 'JPA in 50 steps', sysdate(), sysdate());
INSERT INTO COURSE (ID, NAME, CREATED_DATE, LAST_UPDATED_DATE) VALUES (1002, 'Spring in 50 steps', sysdate(), sysdate());
INSERT INTO COURSE (ID, NAME, CREATED_DATE, LAST_UPDATED_DATE) VALUES (1003, 'Spring Boot in 50 steps', sysdate(), sysdate());

INSERT INTO PASSPORT (ID, NUMBER) VALUES (4001, 'EN11X');
INSERT INTO PASSPORT (ID, NUMBER) VALUES (4002, 'SS3VV');
INSERT INTO PASSPORT (ID, NUMBER) VALUES (4003, 'KK2P4');

INSERT INTO STUDENT (ID, NAME, PASSPORT_ID) VALUES (2001, 'Ranga', 4001);
INSERT INTO STUDENT (ID, NAME, PASSPORT_ID) VALUES (2002, 'Adam' , 4002);
INSERT INTO STUDENT (ID, NAME, PASSPORT_ID) VALUES (2003, 'Jane' , 4003);

INSERT INTO REVIEW (ID, RATING, DESCRIPTION, COURSE_ID) VALUES (3001, '5', 'Great Course', 1001);
INSERT INTO REVIEW (ID, RATING, DESCRIPTION, COURSE_ID) VALUES (3002, '4', 'Wonderfull Course', 1001);
INSERT INTO REVIEW (ID, RATING, DESCRIPTION, COURSE_ID) VALUES (3003, '3', 'Awesome Course', 1002);

-- For Testing Purposes
INSERT INTO COURSE (ID, NAME, CREATED_DATE, LAST_UPDATED_DATE) VALUES (1101, 'JPA in 50 steps', sysdate(), sysdate());
INSERT INTO COURSE (ID, NAME, CREATED_DATE, LAST_UPDATED_DATE) VALUES (1102, 'Spring in 50 steps', sysdate(), sysdate());
INSERT INTO COURSE (ID, NAME, CREATED_DATE, LAST_UPDATED_DATE) VALUES (1103, 'Spring Boot in 50 steps', sysdate(), sysdate());