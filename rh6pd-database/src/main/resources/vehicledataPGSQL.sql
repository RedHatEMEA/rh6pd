DROP DATABASE IF EXISTS rh6pd;

CREATE DATABASE rh6pd;

\connect rh6pd

CREATE  TABLE vehicledata (
  id INT NOT NULL ,
  numberplate VARCHAR(10) NULL ,
  make VARCHAR(45) NULL ,
  model VARCHAR(45) NULL ,
  dateregistered DATE NULL ,
  fueltype VARCHAR(15) NULL ,
  notes VARCHAR(90) NULL ,
  PRIMARY KEY (`id`) 
);


INSERT INTO vehicledata VALUES 
(1, 'jb05sss', 'fiat', 'punto', '12/02/2005', 'diesel', 'no accident history'),
(2, 'rh60pdd', 'mercedes', 'e class', '31/01/2011', 'diesel', 'no accident history'),
(3, 'eap6', 'jaguar', 'xjf', '03/10/2008', 'petrol', 'no accident history'),
(4, 'my03bmw', 'bmw', 'm3', '22/08/2003', 'petrol', '27 accidents on record'),
(5, 'b530rms', 'landrover', 'defender', '17/12/1985', 'diesel', '4 accidents on record'),
(6, 'j408bpm', 'alfa romeo', 'brerra', '29/07/1999', 'petrol', 'no accident history'),
(7, 'ga06epp', 'ford', 'fiesta', '21/11/2006', 'diesel', '1 accident on record');

COMMIT;
