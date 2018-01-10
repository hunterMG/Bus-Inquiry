<?php
error_reporting(0);
require 'db.php';

$routeName = $_POST["routeName"];
$price = $_POST["price"];
$startTime = $_POST["startTime"];
$endTime = $_POST["endTime"];
$stopNum = $_POST["stopNum"];

$response = array();
$response["success"] = false;
    
$sql = "INSERT into `Route`(`RouteName`, `Price`, `StartTime`, `EndTime`, `StopNum`) values('$routeName', $price, '$startTime', '$endTime', $stopNum)";
$res = $mysqli->query($sql);
if($res){
    $response["success"] = true;
}else{
    $response["success"] = false;
}

echo json_encode($response);
if($mysqli){
    $mysqli->close();
}
?>