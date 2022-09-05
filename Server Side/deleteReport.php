<?php
header('Content-Type: application/json');

include "DatabaseConfig.php";

     $id = $_GET["id"]; 


    $sql = "DELETE  FROM report WHERE id= '$id'";
    
    if ($conn->query($sql) === TRUE) {
              $status = "تم الحذف بنجاح";
  	    echo json_encode(["status"=>$status]);

} else {
           $status = "حدث خطا اثناء الحذف";
  	    echo json_encode(["status"=>$status]);}
    
?>