<?php  

header('Content-Type: application/json');

include "DatabaseConfig.php";



if($_SERVER['REQUEST_METHOD'] == 'POST')
{
   
    $product_Id = $_POST["id"];
    $product_name = $_POST["product_name"];
    $product_des = $_POST["product_des"]; 
    $product_price = $_POST["product_price"]; 
    $product_section = $_POST["product_section"]; 
    $productOfferPrice = $_POST["product_offer_price"]; 
    $productOfferPercentage = $_POST["product_offer_percentage"]; 




       $InsertSQL = "UPDATE product SET product_name = '$product_name'  ,product_des = '$product_des' ,product_price = '$product_price' ,product_section = '$product_section' ,product_offer_price = '$productOfferPrice' ,product_offer_percentage = '$productOfferPercentage' WHERE id='$product_Id'";
       
       
       
       if(mysqli_multi_query($conn, $InsertSQL)){

        $success = true;  
        $message = "Successfully Uploaded";
        
        $_item = array( "success"=>$success ,"message"=>$message);	 
         echo json_encode($_item);     
         }
         
        

       }
     
   else   
   {  
     
        $success = false;  
        $message = "Error while uploading";
        
        $_item = array( "success"=>$success ,"message"=>$message);	 
         echo json_encode($_item);
    
}  

  
?>  