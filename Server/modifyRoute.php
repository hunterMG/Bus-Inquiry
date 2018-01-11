<?php
error_reporting(0);
require 'db.php';

$routeID = $_POST["routeID"];
$routeName = $_POST["routeName"];
$price = $_POST["price"];
$startTime = $_POST["startTime"];
$endTime = $_POST["endTime"];
$stopNum = $_POST["stopNum"];

$response = array();
$response["success"] = false;

$sql = "UPDATE `Route` set `RouteName`='$routeName', `Price`=$price, `StartTime`='$startTime', `EndTime`='$endTime', `StopNum`= $stopNum where `RouteID`=$routeID";
$res = $mysqli->query($sql);
if($res){
    if($mysqli->affected_rows == 0){
        $response["success"] = false;    
    }else{
        $response["success"] = true;
    }
}else{
    $response["success"] = false;
}

echo json_encode($response);
if($mysqli){
    $mysqli->close();
}
?>