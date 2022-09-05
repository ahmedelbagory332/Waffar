<?php
header('Content-Type: application/json');

include "DatabaseConfig.php";

     $products = array();

    $productUserEmail	 = $_GET["product_user_email"]; 
    $sql = "SELECT * FROM orders WHERE product_user_email= '$productUserEmail'";
    
    
    $result = $conn->query($sql);
    
    
    if ($result->num_rows > 0) {
    // output data of each row
	while($row = $result->fetch_assoc()) {
	
	    $id = $row["id"];
    	$product_name = $row["product_name"];
        $product_img = $row["product_img"];
        $productTotalPrice = $row["product_total_price"]; 
        $productNumber = $row["product_number"]; 
        $productUserEmail = $row["product_user_email"]; 
	        $product_quantity =  $row["product_quantity"]; 

	    $items = array("id"=>$id,"name"=>$product_name ,"image"=>$product_img ,"productUserEmail"=>$productUserEmail,"price"=>$productTotalPrice ,"productNumber"=>$productNumber,"productQuantity"=>$product_quantity);
	
	     array_push($products, $items);

 	
        }
    
	    // show data in json format
	    echo json_encode(["UserOrders"=>$products]);
	    
    
  }
    else
    {
 	    echo json_encode(["UserOrders"=>$products]);
    }
 
?>