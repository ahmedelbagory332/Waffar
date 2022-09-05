<?php  

header('Content-Type: application/json');

include "DatabaseConfig.php";



if($_SERVER['REQUEST_METHOD'] == 'POST')
{
   
   
    $target_dir = "../images/";  
    $target_file_name = $target_dir .basename($_FILES["product_img"]["name"]);  
    $product_name = $_POST["product_name"];
    $product_img = $_FILES["product_img"]["name"];
    $product_des = $_POST["product_des"]; 
    $product_price = $_POST["product_price"]; 
    $product_section = $_POST["product_section"]; 
    $productOfferPrice = $_POST["product_offer_price"]; 
    $productOfferPercentage = $_POST["product_offer_percentage"]; 
   // $productCode = rand(50000,100000);



   if (move_uploaded_file($_FILES["product_img"]["tmp_name"], $target_file_name))   
   {  
       $fullPath = "https://onlinemarket288.000webhostapp.com/images/".$product_img;

       $InsertSQL = "insert into product (product_name,product_img,product_des,product_price,product_section,product_offer_price,product_offer_percentage) values ('$product_name','$fullPath','$product_des','$product_price','$product_section','$productOfferPrice','$productOfferPercentage')";
       
       
       
       if(mysqli_multi_query($conn, $InsertSQL)){

        $success = true;  
        $message = "Successfully Uploaded";
        
        $_item = array( "success"=>$success ,"message"=>$message);	 
         echo json_encode($_item);     
         }
         
        

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