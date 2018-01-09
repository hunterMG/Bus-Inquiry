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
	`EndTime` time NOT null,
	`StopNum` int NOT NULL
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

INSERT into `Route`(`RouteName`, `Price`, `StartTime`, `EndTime`, `StopNum`) 
	VALUES ('r1', 1, '6:00:00', '22:00:00', 5);
INSERT into `Route`(`RouteName`, `Price`, `StartTime`, `EndTime`, `StopNum`) 
	VALUES ('r2', 1, '6:00:00', '22:00:00', 4);
INSERT into `Route`(`RouteName`, `Price`, `StartTime`, `EndTime`, `StopNum`) 
	VALUES ('r3', 2, '6:30:00', '22:30:00', 3);

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

CREATE VIEW Through
as 
	SELECT 
		RS1.`RouteID` as `RouteID`,
		RS1.`StopID` as `StartID`, 
		RS2.`StopID` `EndID`,
		RS2.`Position` - RS1.`Position` as `StopNum`
	from `Route_Stop` RS1, `Route_Stop` RS2
	WHERE RS1.`RouteID` = RS2.`RouteID` and RS1.`Position` < RS2.`Position`;

SELECT * from Through;
-- show PROCEDURE STATUS;
show CREATE PROCEDURE transferThrough;

DROP PROCEDURE if EXISTS transferThrough;
DELIMITER ;;
CREATE PROCEDURE transferThrough(in startstop VARCHAR(20), in endstop VARCHAR(20), out routeName1 VARCHAR(20))
begin
	DECLARE startID int;
	DECLARE endID int;
	DECLARE routeID1 int;
	SELECT `StopID` INTO startID from `Stop` WHERE `StopName` = startstop;
	SELECT `StopID` INTO endID from `Stop` WHERE `StopName` = endstop;
	SELECT RS1.`RouteID` into routeID1 from `Route_Stop` RS1,`Route_Stop` RS2 WHERE RS1.`RouteID` = RS2.`RouteID` and RS1.`StopID` = startID and RS2.`StopID` = endID and RS1.`Position`<RS2.`Position`;
	SELECT `RouteName` into routeName1 from `Route` WHERE `RouteID` = routeID1;
	SELECT routeName1;
END
;;
DELIMITER ;

call transferThrough('s1', 's4', @routeName1);

DROP PROCEDURE if EXISTS transfer1;
DELIMITER ;;
CREATE PROCEDURE transfer1(in startstop VARCHAR(32), in endstop VARCHAR(32), 
	out routeName1 VARCHAR(32), out transStop1 VARCHAR(32), out routeName2 VARCHAR(32))
BEGIN
	DECLARE startID int;
	DECLARE endID int;
	DECLARE routeID1 int;
	DECLARE transID1 int;
	DECLARE routeID2 int;
	SELECT `StopID` INTO startID from `Stop` WHERE `StopName` = startstop;
	SELECT `StopID` INTO endID from `Stop` WHERE `StopName` = endstop;
	SELECT t1.`RouteID`, t1.`EndID`, t2.`RouteID` into routeID1,transID1,routeID2
		from `Through` t1, `Through` t2 
		WHERE t1.`StartID` = startID and t2.`EndID` = endID AND
			t1.`EndID` = t2.`StartID`;
	SELECT `RouteName` into routeName1 from `Route` where `RouteID` = routeID1;
	SELECT `StopName` into transStop1 from `Stop` where `StopID` = transID1;
	SELECT `RouteName` INTO routeName2 from `Route` where `RouteID` = routeID2;
	SELECT routeName1,transStop1,routeName2;
END
;;
DELIMITER ;

call transfer1('s1', 's8', @routeName1, @transStop1, @routeName2);

SELECT `RouteID` from `Route` where `RouteName` = 'r1';

DROP PROCEDURE if EXISTS transfer2;
DELIMITER ;;
CREATE PROCEDURE transfer2(in startstop VARCHAR(32), in endstop VARCHAR(32), 
	out routeName1 VARCHAR(32), out transStop1 VARCHAR(32), out routeName2 VARCHAR(32), 
	out transStop2 VARCHAR(32), out routeName3 VARCHAR(32) )
BEGIN
	DECLARE startID int;
	DECLARE endID int;
	DECLARE routeID1 int;
	DECLARE transID1 int;
	DECLARE routeID2 int;
	DECLARE transID2 int;
	DECLARE routeID3 int;
	SELECT `StopID` INTO startID from `Stop` WHERE `StopName` = startstop;
	SELECT `StopID` INTO endID from `Stop` WHERE `StopName` = endstop;
	SELECT t1.`RouteID`, t1.`EndID`, t2.`RouteID`, t2.`EndID`,  t3.`RouteID` into routeID1,transID1,routeID2,transID2,routeID3
		from `Through` t1, `Through` t2, `Through` t3
		WHERE t1.`StartID` = startID and t3.`EndID` = endID AND
			t1.`EndID` = t2.`StartID` and t2.`EndID` = t3.`StartID`;
	SELECT `RouteName` into routeName1 from `Route` where `RouteID` = routeID1;
	SELECT `StopName` into transStop1 from `Stop` where `StopID` = transID1;
	SELECT `RouteName` INTO routeName2 from `Route` where `RouteID` = routeID2;
	SELECT `StopName` into transStop2 from `Stop` where `StopID` = transID2;
	SELECT `RouteName` INTO routeName3 from `Route` where `RouteID` = routeID3;

	SELECT routeName1,transStop1,routeName2,transStop2,routeName3;
END
;;
DELIMITER ;

call transfer2('s1', 's9', @routeName1, @transStop1, @routeName2, @transStop2, @routeName3);

insert into `User` (`email`, `pswd`, `isAdmin`) VALUES('wml@1.com', MD5(MD5(11112222)), 1);

ALTER TABLE `Stop` MODIFY COLUMN `StopName` VARCHAR(20) UNIQUE;

DROP TRIGGER if EXISTS deleteStop;
DELIMITER $
CREATE TRIGGER deleteStop BEFORE DELETE on `Stop` for each row
BEGIN
DECLARE stopID1 int;
DECLARE routeID1 int;
DECLARE stopID2 int;
DECLARE position2 int;
DECLARE newPosition int DEFAULT 1;
DECLARE s int default 0;
DECLARE cursorRoute CURSOR FOR SELECT `StopID`,`Position` from `Route_Stop` where `RouteID` = routeID1 ORDER BY `Position` ASC;
DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET s=1;  
SELECT `StopID` into stopID1 from `Stop` where `StopName` = old.`StopName`;
SELECT `RouteID` into routeID1 from `Route_Stop` where `StopID` = stopID1;
DELETE from `Route_Stop` where `StopID` = stopID1;
OPEN cursorRoute;
FETCH cursorRoute into stopID2,position2;
WHILE s <> 1 DO
	UPDATE `Route_Stop` set `Position` = newPosition 
		where `RouteID` = routeID1 and `StopID` = stopID2 
				and `Position` = position2;
	set newPosition = newPosition + 1;
	FETCH cursorRoute into stopID2,position2;
end WHILE;
close cursorRoute;
end
$
DELIMITER ;
