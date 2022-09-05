<?php
header('Content-Type: application/json');

include "DatabaseConfig.php";

     $products = array();

    $sql = "SELECT * FROM product where product_offer_price != '0' and product_offer_percentage != '0'";
    
    
    $result = $conn->query($sql);
    
    
    if ($result->num_rows > 0) {
    // output data of each row
	while($row = $result->fetch_assoc()) {
	
	    $id = $row["id"];
    	$product_name = $row["product_name"];
        $product_img = $row["product_img"];
        $product_des = $row["product_des"];
    	$product_price = $row["product_price"];
        $product_section = $row["product_section"];
    	$product_offer_price = $row["product_offer_price"];
        $product_offer_percentage = $row["product_offer_percentage"];
         
	    
	    $items = array("id"=>$id,"name"=>$product_name ,"image"=>$product_img,"description"=>$product_des,"price"=>$product_price ,"section"=>$product_section,"productOfferPrice"=>$product_offer_price,"productOfferPercentage"=>$product_offer_percentage );
	
	     array_push($products, $items);

 	
        }
    
	    // show data in json format
	    echo json_encode(["offers"=>$products]);
	    
    
  }
    else
    {
	 echo json_encode(["offers"=>$products]);
    }
 
?>