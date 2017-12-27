<?php

// $routeName = $_POST["routeName"];
$routeName = "r1";
require 'db.php';   //create a mysql connection--'$mysqli'
if($mysqli){
    echo "conn success";
}
$stmt = $mysqli->stmt_init();
$response = array();
$sql = "select * from `Route` where `RouteName` = 'r1';";
$result = $mysqli->query($sql);
while($row = $result->fetch_assoc()) {
    echo "routeid: " . $row["RouteID"]. " - Name: " . $row["RouteName"]. "<br>";
}



if($stmt->prepare("select * from `route` where `routeName` = ?")){
    $stmt.bind_param("s", $routeName);
    // $result = $stmt->execute();
    $result = $mysqli->query($sql);
    if($result->num_rows > 0){
        // $row = $result->num_rows;
        while($row = $result->fetch_assoc()) {
            echo "id: " . $row["id"]. " - Name: " . $row["firstname"]. " " . $row["lastname"]. "<br>";
        }
    }else{
        echo "no data found";
    }
}




$mysqli->close();
?>