<?php
error_reporting(0);
class ResultRoute{
    public $success;
    public $num_route;
    public $route;
}
class Route{
    public $RouteID;
    public $RouteName;
    public $Price;
    public $StartTime;
    public $EndTime;
    public $StartStop;
    public $EndStop;
    public $StopNum;
    public $Stops;
    public function __construct($RouteID, $RouteName, $Price, $StartTime, $EndTime, $StartStop, $EndStop, $StopNum, $Stops){
        $this->RouteID = $RouteID;
        $this->RouteName = $RouteName;
        $this->Price = $Price;
        $this->StartTime = $StartTime;
        $this->EndTime = $EndTime;
        $this->StartStop = $StartStop;
        $this->EndStop = $EndStop;
        $this->StopNum = $StopNum;
        $this->Stops = $Stops;
    }
}

function getStops($routeID, $mysqli){
    $stops = array();
    $i = 0;
    $sql = "SELECT `StopID` from `Route_Stop` WHERE `RouteID` = ".$routeID." ORDER BY `Position` ASC";
    $resultStopIDs = $mysqli->query($sql);
    while($row = $resultStopIDs->fetch_assoc()){
        $sql1 = "SELECT `StopName` from `Stop` WHERE `StopID` = ".$row["StopID"];
        $resStop = $mysqli->query($sql1);
        $rowStop = $resStop->fetch_assoc();
        $stops[$i++] = $rowStop["StopName"];
    }
    return $stops;
}

$routeName = $_POST["routeName"];
// if($routeName == ""){
//     exit(0);
// }
// $routeName = "r1";
require 'db.php';   //create a mysql connection--'$mysqli'
$response = array();
$resultRoute = new ResultRoute();
$sql = "select * from `Route` where `RouteName` like '%".$routeName."%';";
if($result = $mysqli->query($sql)){
    $resultRoute->success = true;
    $resultRoute->num_route = $result->num_rows;
    $resultRoute->route = array();
    $i = 0;
    while($row = $result->fetch_assoc()) {
        $sqlStartStop = "SELECT `StopName` from `Stop` WHERE `StopID` = (select `StopID` from `Route_Stop` WHERE `RouteID` = ".$row["RouteID"]." ORDER BY `Position` ASC LIMIT 1)";
        $resultStartStop = $mysqli->query($sqlStartStop);
        $rowStartStop = $resultStartStop->fetch_assoc();
        $sqlEndStop = "SELECT `StopName` from `Stop` WHERE `StopID` = (select `StopID` from `Route_Stop` WHERE `RouteID` = ".$row["RouteID"]." ORDER BY `Position` DESC LIMIT 1)";
        $resultEndStop = $mysqli->query($sqlEndStop);
        $rowEndStop = $resultEndStop->fetch_assoc();
        $stops = getStops($row["RouteID"], $mysqli);
        $route = new Route($row["RouteID"], $row["RouteName"], $row["Price"], $row["StartTime"], $row["EndTime"], $rowStartStop["StopName"], $rowEndStop["StopName"], $row["StopNum"], $stops);
        $resultRoute->route[$i++] = $route;
    }
}else{
    $resultRoute->success = false;
}

echo json_encode($resultRoute);

$mysqli->close();
?>