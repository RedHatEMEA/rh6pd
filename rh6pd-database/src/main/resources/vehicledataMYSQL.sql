SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `rh6pd` ;
CREATE SCHEMA IF NOT EXISTS `rh6pd` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ;
USE `rh6pd` ;

-- -----------------------------------------------------
-- Table `rh6pd`.`vehicledata`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `rh6pd`.`vehicledata` ;

CREATE  TABLE IF NOT EXISTS `rh6pd`.`vehicledata` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `numberplate` VARCHAR(10) NULL ,
  `make` VARCHAR(45) NULL ,
  `model` VARCHAR(45) NULL ,
  `dateregistered` DATE NULL ,
  `fueltype` VARCHAR(15) NULL ,
  `notes` TINYTEXT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `numberplate_UNIQUE` (`numberplate` ASC) )
ENGINE = InnoDB;

USE `rh6pd` ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `rh6pd`.`vehicledata`
-- -----------------------------------------------------
START TRANSACTION;
USE `rh6pd`;
INSERT INTO `rh6pd`.`vehicledata` (`id`, `numberplate`, `make`, `model`, `dateregistered`, `fueltype`, `notes`) VALUES (1, 'jb05sss', 'fiat', 'punto', '12/02/2005', 'diesel', 'no accident history');
INSERT INTO `rh6pd`.`vehicledata` (`id`, `numberplate`, `make`, `model`, `dateregistered`, `fueltype`, `notes`) VALUES (2, 'rh60pdd', 'mercedes', 'e class', '31/01/2011', 'diesel', 'no accident history');
INSERT INTO `rh6pd`.`vehicledata` (`id`, `numberplate`, `make`, `model`, `dateregistered`, `fueltype`, `notes`) VALUES (3, 'eap6', 'jaguar', 'xjf', '03/10/2008', 'petrol', 'no accident history');
INSERT INTO `rh6pd`.`vehicledata` (`id`, `numberplate`, `make`, `model`, `dateregistered`, `fueltype`, `notes`) VALUES (4, 'my03bmw', 'bmw', 'm3', '22/08/2003', 'petrol', '27 accidents on record');
INSERT INTO `rh6pd`.`vehicledata` (`id`, `numberplate`, `make`, `model`, `dateregistered`, `fueltype`, `notes`) VALUES (5, 'b530rms', 'landrover', 'defender', '17/12/1985', 'diesel', '4 accidents on record');
INSERT INTO `rh6pd`.`vehicledata` (`id`, `numberplate`, `make`, `model`, `dateregistered`, `fueltype`, `notes`) VALUES (6, 'j408bpm', 'alfa romeo', 'brerra', '29/07/1999', 'petrol', 'no accident history');
INSERT INTO `rh6pd`.`vehicledata` (`id`, `numberplate`, `make`, `model`, `dateregistered`, `fueltype`, `notes`) VALUES (7, 'ga06epp', 'ford', 'fiesta', '21/11/2006', 'diesel', '1 accident on record');

COMMIT;
