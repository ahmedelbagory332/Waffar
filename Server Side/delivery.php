<?php
header('Content-Type: application/json');

include "DatabaseConfig.php";

if($_SERVER['REQUEST_METHOD'] == 'POST')
{
   
   
    $productUserEmail = $_POST["productUserEmail"]; 
    $productNumber = $_POST["productNumber"]; 
  

    
    
        
                 $to      = $productUserEmail; // Send email to our user
                $subject = 'توصيل الطلب'; // Give the email a subject 
                $message =
                            '
                            شكرا لاستخدامك  وفر  جارى توصيل طلبك رقم 
                                 
                                ------------------------
                                رقم الطلب = '.$productNumber.'
                                ------------------------
                            ';
              $headers = 'From:noreply@waffar.com' . "\r\n"; // Set from headers
                mail($to, $subject, $message, $headers); // Send our email
                

       $status = "تم التاكيد";
  	    echo json_encode(["status"=>$status]);
       
   
}  

  
?>  