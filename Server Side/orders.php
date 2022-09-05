<?php
header('Content-Type: application/json');

include "DatabaseConfig.php";

if($_SERVER['REQUEST_METHOD'] == 'POST')
{
   
   
    $product_name = $_POST["product_name"]; 
    $product_img = $_POST["product_img"]; 
    $product_total_price= $_POST["product_total_price"];
    $product_user_email = $_POST["product_user_email"];
    $product_user_phone = $_POST["user_phone"];
    $product_user_name = $_POST["user_name"];
    $user_address = $_POST["user_address"];
    $product_quantity =  $_POST["product_quantity"]; 
   $product_number = rand(5000,10000);

    
    
 
        $InsertSQL = "INSERT INTO `orders`(`product_name`, `product_img`, `product_total_price`, `product_user_email`, `product_number`,`product_quantity`) VALUES ('$product_name','$product_img','$product_total_price','$product_user_email','$product_number','$product_quantity')";
        
        $InsertUserOrders = "INSERT INTO `user_orders`(`user_mail`,`user_address`,`user_phone`,`user_name`) VALUES ('$product_user_email','$user_address','$product_user_phone','$product_user_name')";
       
       if(mysqli_query($conn, $InsertSQL)or die(mysqli_error($conn)." update failed")){
              
                mysqli_query($conn, $InsertUserOrders);
               
                $to      = 'belbiesmarket@gmail.com'; // Send email to our user
                $subject = 'طلبيه جديدة';
                $message ='برجاء فتح التطبيق يوجد طلبيه جديدة';
                $headers = 'From:noreply@waffar.com' . "\r\n"; // Set from headers
                mail($to, $subject, $message, $headers); // Send our email
                echo json_encode(array("response"=>$product_number));

       }
       else{
        
              $status = "حدث خطأ";
            echo json_encode(array("response"=>$status));

       }
    
      
   
}  

  
?>  