<?php
error_reporting(0);
class ResultTransfer{
    public $success;
    public $num_route;
    public $routes;
}

class Route{
    public $routeName;
    public $num_stop;
    public $stops; // array
}

function getRoute($mysqli, $routeName, $startStop, $endStop){
    $sql = "SELECT `RouteID` from `Route` where `RouteName` = '$routeName'";
    $res = $mysqli->query($sql);
    $row = $res->fetch_assoc();
    $routeID = $row["RouteID"]; //
    
    $sql = "SELECT `StopID` from `Stop` where `StopName` = '$startStop';";
    $res = $mysqli->query($sql);
    $row = $res->fetch_assoc();
    $startID = $row["StopID"];  //
    
    $sql = "SELECT `StopID` from `Stop` where `StopName` = '$endStop';";
    $res = $mysqli->query($sql);
    $row = $res->fetch_assoc();
    $endID = $row["StopID"];    //
    
    $sql = "SELECT `Position` from `Route_Stop` where `RouteID` = '$routeID' and `StopID` = '$startID';";
    $res = $mysqli->query($sql);
    $row = $res->fetch_assoc();
    $startPosition = $row["Position"];

    $sql = "SELECT `Position` from `Route_Stop` where `RouteID` = '$routeID' and `StopID` = '$endID';";
    $res = $mysqli->query($sql);
    $row = $res->fetch_assoc();
    $endPosition = $row["Position"];

    $route = new Route();
    $route->routeName = $routeName;
    $route->num_stop = $endPosition - $startPosition;
    $stops = array();
    $i = 0;
    $sql = "SELECT `StopID` from `Route_Stop` where `RouteID` = '$routeID' and `Position` >= $startPosition and `Position` <= $endPosition;";
    $res = $mysqli->query($sql);
    while($row = $res->fetch_assoc()){
        $sql1 = "SELECT `StopName` from `Stop` WHERE `StopID` = ".$row["StopID"];
        $resStop = $mysqli->query($sql1);
        $rowStop = $resStop->fetch_assoc();
        $stops[$i++] = $rowStop["StopName"];
    }
    $route->stops = $stops;
    return $route;
}

function solveResult($mysqli){
    do {
        if ($result = $mysqli->store_result()) {
            while ($row = $result->fetch_row()) {
                // printf("%s\n", $row[0]);
             }
            $result->close();
         }
     } while ($mysqli->next_result());
}

require 'db.php';

$startStop = $_POST["startStop"];
$endStop = $_POST["endStop"];
// $startStop = "s1";
// $endStop = "s10";
$resultTrans = new ResultTransfer();
$sql = "call transferThrough('$startStop', '$endStop', @routeName1);";
$res = $mysqli->query($sql);
if($res){
    $row = $res->fetch_assoc();
    $res->close();
    solveResult($mysqli); // for procedure unneeded result
    if($row["routeName1"]){        
        $resultTrans->success = true;        
        $resultTrans->num_route = 1;
        $routes = array();
        $routeName1 = $row["routeName1"];
        $routes[0] = getRoute($mysqli, $routeName1, $startStop, $endStop);
        $resultTrans->routes = $routes;
    }else{ // can't go by a through route
        $sql = "call transfer1('$startStop', '$endStop', @routeName1, @transStop1, @routeName2);";
        $res = $mysqli->query($sql);
        if($res){
            $row = $res->fetch_assoc();
            $res->close();
            solveResult($mysqli);            
            if($row["routeName1"]){
                $resultTrans->success = true;
                $resultTrans->num_route = 2;
                $routeName1 = $row["routeName1"];
                $transStop1 = $row["transStop1"];
                $routeName2 = $row["routeName2"];
                $routes = array();
                $routes[0] = getRoute($mysqli, $routeName1, $startStop, $transStop1);
                $routes[1] = getRoute($mysqli, $routeName2, $transStop1, $endStop);
                $resultTrans->routes = $routes;
            }else{// can't go by 1 tansfer route
                $sql = "call transfer2('$startStop', '$endStop', @routeName1, @transStop1, @routeName2, @transStop2, @routeName3);";
                $res = $mysqli->query($sql);
                if($res){
                    $row = $res->fetch_assoc();
                    $res->close();
                    solveResult($mysqli);
                    if($row["routeName1"]){
                        $resultTrans->success = true;
                        $resultTrans->num_route = 2;
                        $routeName1 = $row["routeName1"];
                        $transStop1 = $row["transStop1"];
                        $routeName2 = $row["routeName2"];
                        $transStop2 = $row["transStop2"];
                        $routeName3 = $row["routeName3"];
                        $routes = array();
                        $routes[0] = getRoute($mysqli, $routeName1, $startStop, $transStop1);
                        $routes[1] = getRoute($mysqli, $routeName2, $transStop1, $transStop2);
                        $routes[2] = getRoute($mysqli, $routeName3, $transStop2, $endStop);
                        $resultTrans->routes = $routes;
                    }else{// can't go by 2 transfer route
                        $resultTrans->success = false;
                    }
                }else{
                    $resultTrans->success = false;
                }
            }
        }else{
            $resultTrans->success = false;
        }
    }
}else{
    $resultTrans->success = false;
}
echo json_encode($resultTrans);

if($mysqli){
    $mysqli->close();
}
?>