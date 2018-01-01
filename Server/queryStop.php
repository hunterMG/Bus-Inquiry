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

function getStops($rowRoute, $mysqli){
    $stops = array();
    $i = 0;
    $sql = "SELECT `StopID` from `Route_Stop` WHERE `RouteID` = ".$rowRoute["RouteID"]." ORDER BY `Position` ASC";
    $resultStopIDs = $mysqli->query($sql);
    while($row = $resultStopIDs->fetch_assoc()){
        $sql1 = "SELECT `StopName` from `Stop` WHERE `StopID` = ".$row["StopID"];
        $resStop = $mysqli->query($sql1);
        $rowStop = $resStop->fetch_assoc();
        $stops[$i++] = $rowStop["StopName"];
    }
    return $stops;
}

function getStopIDs($mysqli, $stopName){
    $stopIDs = array();
    $sql = "SELECT `StopID` from `Stop` where `StopName` like '%".$stopName."%';";
    $res = $mysqli->query($sql);

    $i = 0;
    while($row = $res->fetch_assoc()){
        $stopIDs[$i++] = $row["StopID"];
    }
    return $stopIDs;
}
function getRouteIDs($mysqli, $stopIDs){
    $routeIDs = array();
    $index = 0;
    for($i=0; $i<count($stopIDs); $i++){
        $sql = "SELECT `RouteID` from `Route_Stop` where `StopID` = ".$stopIDs[$i];
        $res = $mysqli->query($sql);
        while($row = $res->fetch_assoc()){
            if(!in_array($row["RouteID"], $routeIDs)){
                $routeIDs[$index++] = $row["RouteID"];
            }
        }
    }
    return $routeIDs;
}

$stopName = $_POST["stopName"];
if($stopName == ""){
    exit(0);
}
// $stopName = "s1";
require 'db.php';   //create a mysql connection--'$mysqli'
$response = array();
$stopIDs = getStopIDs($mysqli, $stopName);
$routeIDs = getRouteIDs($mysqli, $stopIDs);

$resultRoute = new ResultRoute();
if(count($routeIDs)==0){
    $resultRoute->success = false;
}else{
    $resultRoute->success = true;
    $resultRoute->num_route = count($routeIDs);
    for($i=0; $i < count($routeIDs); $i++){
        $sql = "select * from `Route` where `RouteID` = ".$routeIDs[$i].";";
        $result = $mysqli->query($sql);
        $row = $result->fetch_assoc();
        $sqlStartStop = "SELECT `StopName` from `Stop` WHERE `StopID` = (select `StopID` from `Route_Stop` WHERE `RouteID` = ".$row["RouteID"]." ORDER BY `Position` ASC LIMIT 1)";
        $resultStartStop = $mysqli->query($sqlStartStop);
        $rowStartStop = $resultStartStop->fetch_assoc();
        $sqlEndStop = "SELECT `StopName` from `Stop` WHERE `StopID` = (select `StopID` from `Route_Stop` WHERE `RouteID` = ".$row["RouteID"]." ORDER BY `Position` DESC LIMIT 1)";
        $resultEndStop = $mysqli->query($sqlEndStop);
        $rowEndStop = $resultEndStop->fetch_assoc();
        $stops = getStops($row, $mysqli);
        $route = new Route($row["RouteID"], $row["RouteName"], $row["Price"], $row["StartTime"], $row["EndTime"], $rowStartStop["StopName"], $rowEndStop["StopName"], $row["StopNum"], $stops);
        $resultRoute->route[$i] = $route;
    }
}

echo json_encode($resultRoute);

$mysqli->close();
?>