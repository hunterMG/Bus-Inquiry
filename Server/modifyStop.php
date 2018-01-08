<?php
error_reporting(0);
require 'db.php';

$oldStopName = $_POST["oldStopName"];
$newStopName = $_POST["newStopName"];
// $oldStopName = 's14';
// $newStopName = 's13';

$response = array();
$response["success"] = false;
$sql = "UPDATE `Stop` SET `StopName` = '$newStopName' where `StopName` = '$oldStopName';";
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
