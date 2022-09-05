<?php
header('Content-Type: application/json');

include "DatabaseConfig.php";

     $productNumber	 = $_GET["product_number"]; 


    $sql = "DELETE  FROM orders WHERE product_number= '$productNumber'";
    
    if ($conn->query($sql) === TRUE) {
              $status = "تم الحذف بنجاح";
  	    echo json_encode(["status"=>$status]);

} else {
           $status = "حدث خطا";
  	    echo json_encode(["status"=>$status]);}
    
?>