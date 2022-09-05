<?php
header('Content-Type: application/json');

include "DatabaseConfig.php";

     $sectionName	 = $_GET["sectionName"]; 


    $sql = "DELETE FROM sections WHERE sectionName= '$sectionName'";
    
    if ($conn->query($sql) === TRUE) {
              $status = "تم الحذف بنجاح";
  	    echo json_encode(["status"=>$status]);

} else {
           $status = "حدث خطا";
  	    echo json_encode(["status"=>$status]);}
    
?>