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
    public function __construct($RouteID, $RouteName, $Price, $StartTime, $EndTime){
        $this->RouteID = $RouteID;
        $this->RouteName = $RouteName;
        $this->Price = $Price;
        $this->StartTime = $StartTime;
        $this->EndTime = $EndTime;
    }
}

$routeName = $_POST["routeName"];
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
        $route = new Route($row["RouteID"], $row["RouteName"], $row["Price"], $row["StartTime"], $row["EndTime"]);
        $resultRoute->route[$i++] = $route;
    }
}else{
    $resultRoute->success = false;
}

echo json_encode($resultRoute);

$mysqli->close();
?>