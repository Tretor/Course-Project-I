CREATE TABLE pcpart (
    "ID" INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    "PCPART_NAME" VARCHAR(200),
    "COMPANY" VARCHAR(200),
    "MASTER_CODE" VARCHAR(200)
);
CREATE TABLE pcpart_users (
    "ID" INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    "USERNAME" VARCHAR(200),
    "PASSWORD" VARCHAR(200)
);
