<?php
header('Content-Type: application/json');

include "DatabaseConfig.php";

     $report = array();

    $sql = "SELECT * FROM report";
    
    
    $result = $conn->query($sql);
    
    
    if ($result->num_rows > 0) {
    // output data of each row
	while($row = $result->fetch_assoc()) {
	
	    $id = $row["id"];
    	$userReport = $row["userReport"];
        $textReport = $row["textReport"];
       
         
	    
	    $items = array("id"=>$id,"userReport"=>$userReport ,"textReport"=>$textReport);
	
	     array_push($report, $items);

 	
        }
    
	    // show data in json format
	    echo json_encode(["reports"=>$report]);
	    
    
  }
    else
    {
 	    echo json_encode(["reports"=>$report]);
    }
 
?>