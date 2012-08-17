-------Car database----------------------------
DROP DATABASE IF EXISTS car;

CREATE DATABASE car;

\connect car;


CREATE TABLE cardata (
  CarID int NOT NULL,
  Make varchar(20) NOT NULL,
  Model varchar(20) NOT NULL,
  Age int NOT NULL,
  PRIMARY KEY (CarID)
);

INSERT INTO cardata VALUES ('1', 'Ford', 'Fiesta', '0'), ('2', 'Ford', 'Fiesta', '3'), ('3', 'Ford', 'Fiesta', '7'), ('4', 'Ford', 'Fiesta', '10');


-------Person database----------------------------
DROP DATABASE IF EXISTS person;

CREATE DATABASE person;

\connect person;


CREATE TABLE persondata (
  Name varchar(20) NOT NULL,
  Age int NOT NULL,
  PersonCarID varchar(20) NOT NULL,
  PRIMARY KEY (Name)
);

INSERT INTO persondata VALUES ('Alice', '30', '1'), ('John', '18', '4'), ('Peter', '20', '2'), ('Mary', '18', '3');

