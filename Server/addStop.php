<?php
error_reporting(0);
require 'db.php';

$stopName = $_POST["stopName"];
// $stopName = 's11';

$response = array();
$response["success"] = false;
if($stopName != ""){
    
    $sql = "INSERT into `Stop`(`StopName`) values('$stopName')";
    $res = $mysqli->query($sql);
    if($res){
        $response["success"] = true;
    }else{
        $response["success"] = false;
    }
}
echo json_encode($response);
if($mysqli){
    $mysqli->close();
}
?>