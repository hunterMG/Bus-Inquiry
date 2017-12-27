Drop DATABASE if EXISTS Bus_Inquiry;
CREATE DATABASE if not EXISTS Bus_Inquiry DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE Bus_Inquiry;
CREATE TABLE if not EXISTS `Stop`(
	`StopID` SMALLINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	`StopName` VARCHAR(20) NOT NULL
);
CREATE TABLE if not EXISTS `Route`(
	`RouteID` SMALLINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	`RouteName` VARCHAR(20) NOT NULL,
	`Price` FLOAT not null,
	`StartTime` time NOT null,
	`EndTime` time NOT null
);
CREATE table if not EXISTS `Route_Stop`(
	`RouteID` SMALLINT UNSIGNED,
	`Position` SMALLINT UNSIGNED,
	`StopID` SMALLINT UNSIGNED,
	PRIMARY KEY (`RouteID`, `Position`),
	FOREIGN KEY (`RouteID`) REFERENCES `Route`(`RouteID`),
	FOREIGN KEY (`StopID`) REFERENCES `Stop`(`StopID`)
);
CREATE TABLE if not EXISTS 	`User`(
	`UserID` Int UNSIGNED AUTO_INCREMENT PRIMARY key,
	`UserName` VARCHAR(20),
	`email` VARCHAR(50) UNIQUE,
	`pswd` char(32),
	`isAdmin` bit not null
);

use Bus_Inquiry;
insert into `Stop`(`StopName`) VALUES ('s1');
insert into `Stop`(`StopName`) VALUES ('s2');
insert into `Stop`(`StopName`) VALUES ('s3');
insert into `Stop`(`StopName`) VALUES ('s4');
insert into `Stop`(`StopName`) VALUES ('s5');
insert into `Stop`(`StopName`) VALUES ('s6');
insert into `Stop`(`StopName`) VALUES ('s7');
insert into `Stop`(`StopName`) VALUES ('s8');
insert into `Stop`(`StopName`) VALUES ('s9');
insert into `Stop`(`StopName`) VALUES ('s10');

INSERT into `Route`(`RouteName`, `Price`, `StartTime`, `EndTime`) 
	VALUES ('r1', 1, '6:00:00', '22:00:00');
INSERT into `Route`(`RouteName`, `Price`, `StartTime`, `EndTime`) 
	VALUES ('r2', 1, '6:00:00', '22:00:00');
INSERT into `Route`(`RouteName`, `Price`, `StartTime`, `EndTime`) 
	VALUES ('r3', 2, '6:30:00', '22:30:00');

insert into `Route_Stop`(`RouteID`, `Position`, `StopID`) VALUES(1, 1, 1);
insert into `Route_Stop`(`RouteID`, `Position`, `StopID`) VALUES(1, 2, 2);
insert into `Route_Stop`(`RouteID`, `Position`, `StopID`) VALUES(1, 3, 3);
insert into `Route_Stop`(`RouteID`, `Position`, `StopID`) VALUES(1, 4, 4);
insert into `Route_Stop`(`RouteID`, `Position`, `StopID`) VALUES(1, 5, 5);
insert into `Route_Stop`(`RouteID`, `Position`, `StopID`) VALUES(2, 1, 6);
insert into `Route_Stop`(`RouteID`, `Position`, `StopID`) VALUES(2, 2, 7);
insert into `Route_Stop`(`RouteID`, `Position`, `StopID`) VALUES(2, 3, 2);
insert into `Route_Stop`(`RouteID`, `Position`, `StopID`) VALUES(2, 4, 8);
insert into `Route_Stop`(`RouteID`, `Position`, `StopID`) VALUES(3, 1, 8);
insert into `Route_Stop`(`RouteID`, `Position`, `StopID`) VALUES(3, 2, 9);
insert into `Route_Stop`(`RouteID`, `Position`, `StopID`) VALUES(3, 3, 10);
