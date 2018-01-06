<?php
error_reporting(0);
require 'db.php';
$email = $_POST["email"];
$pswd = $_POST["pswd"];
// $email = "yg@yg";
// $pswd = md5(md5("11112222"));

$response = array();

if($res = $mysqli->query("INSERT INTO `User`(`email`, `pswd`, `isAdmin`) VALUES ('$email', '$pswd', 0)")){
    $response["success"] = true;
}else{
    $response["success"] = false;
}
echo json_encode($response);
$mysqli->close();
?>
