<?php
$host = "localhost";
$user = "root";
$pswd = "yg789";
$db = "Bus_Inquiry";

$mysqli = new mysqli($host, $user, $pswd, $db);
$mysqli->set_charset("utf8");
$mysqli->query("set names 'utf8'");

// if(!$mysqli){
//     echo 'can not connect!';
// }else{
//     echo 'success';
// }
// $mysqli->close();
?>