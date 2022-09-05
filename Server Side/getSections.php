<?php
header('Content-Type: application/json');

include "DatabaseConfig.php";

     $sections = array();

    $sql = "SELECT * FROM sections";
    
    
    $result = $conn->query($sql);
    
    
    if ($result->num_rows > 0) {
    // output data of each row
	while($row = $result->fetch_assoc()) {
	
	    $id = $row["id"];
    	$sectionName = $row["sectionName"];
        $sectionImg = $row["sectionImg"];
        
	    
	    $items = array("id"=>$id,"name"=>$sectionName ,"image"=>$sectionImg );
	
	     array_push($sections, $items);

 	
        }
    
	    // show data in json format
	    echo json_encode(["sections"=>$sections]);
	    
    
  }
    else
    {
 	    echo json_encode(["sections"=>$sections]);
    }
 
?>