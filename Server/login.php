<?php
error_reporting(0);
require 'db.php';
$email = $_POST["email"];
$pswd = $_POST["pswd"];
// $email = "wml@1.com";
// $pswd = md5(md5("11112222"));

$response = array();
$response["authenticated"] = false;
$sql = "SELECT * FROM `User` WHERE `email` = '$email' AND `pswd` = '$pswd'";
if($res = $mysqli->query($sql)){
    if($res->num_rows){
        $response["authenticated"] = true;
        $row = $res->fetch_assoc();
        if($row["isAdmin"]=="1"){
            $response["isAdmin"] = true;
        }else{
            $response["isAdmin"] = false;
        }
    }else{
        $response["authenticated"] = false;
        $sql = "SELECT * FROM `User` WHERE `email` = '$email'";
        $res = $mysqli->query($sql);
        if($res){
            if($res->num_rows){
                $response["error"] = "wrong_pswd";
            }else{
                $response["error"] = "no_such_user";
            }
        }
    }
}else{

}

echo json_encode($response);

if($mysqli){
    $mysqli->close();
}
?>
