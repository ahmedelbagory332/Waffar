<?php
header('Content-Type: application/json');

include "DatabaseConfig.php";

     $userOrders = array();


    $sql = "SELECT user_mail,user_address,user_phone,user_name, COUNT(*) FROM user_orders GROUP BY user_mail";
    
    
    $result = $conn->query($sql);
    
    
    if ($result->num_rows > 0) {
    // output data of each row
	while($row = $result->fetch_assoc()) {
	
	    $COUNT = $row["COUNT(*)"];
    	$userMaile = $row["user_mail"];
    	$userAddress = $row["user_address"];
    	$userPhone = $row["user_phone"];
    	$userName = $row["user_name"];
	    
	    $items = array("mail"=>$userMaile,"orderNumbers"=>$COUNT,"userAddress"=>$userAddress,"userPhone"=>$userPhone,"userName"=>$userName);
	
	     array_push($userOrders, $items);

 	
        }
    
	    // show data in json format
	    echo json_encode(["UsersOrders"=>$userOrders]);
	    
    
  }
    else
    {
 	    echo json_encode(["UsersOrders"=>$userOrders]);
    }
 
?>