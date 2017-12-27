## Stop
|Column Name    |Type   |   |
| -   | - | - |
|StopID |SMALLINT    |_**Primary Key**_    |
|StopName   |varchar(20)  | |

## Route
|Column Name    |Type   |   |
| - | - | - |
|RouteID    |SMALLINT    |_**Primary Key**_   |
|RouteName  |varchar(20) ||
|Price      |float       ||
|StartTime  |time        ||
|EndTime    |time        ||

## Route_Stop
|Column Name    |Type   |   |
| - | - | - |
|RouteID    |SMALLINT    |   |
|StopID     |SMALLINT    |   |
|position   |SMALLINT    |   |
_Primary Key_： **RouteID StopID**

## User
|Column Name    |Type   |   |
| - | - | - |
|UserID |Int    |_**PK**_|
|UserName|varchar(20) ||
|email   |varchar(50) ||
|pswd    |char(32)||
|isAdmin |bit||
