<?php
header('Content-Type: application/json');

include "DatabaseConfig.php";

     $productUserEmail	 = $_GET["user_mail"]; 


    $sql = "DELETE  FROM user_orders WHERE user_mail= '$productUserEmail'";
    
    if ($conn->query($sql) === TRUE) {
        $sql_ = "DELETE  FROM orders WHERE product_user_email= '$productUserEmail'";
        if ($conn->query($sql_) === TRUE) {
             $status = "تم الحذف بنجاح";
  	    echo json_encode(["status"=>$status]);
        }
        else{
             $status = "حدث خطا";
  	    echo json_encode(["status"=>$status]);
        }
             

} else {
           $status = "حدث خطا";
  	    echo json_encode(["status"=>$status]);
    
}
    
?>