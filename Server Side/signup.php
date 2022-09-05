<?php
header('Content-Type: application/json');

include "DatabaseConfig.php";

if($_SERVER['REQUEST_METHOD'] == 'POST')
{
   
   
    $name = $_POST["name"]; 
    $email = $_POST["email"]; 
    $pass = $_POST["password"];
   $code = rand(500,1000);

    
    $sql = "SELECT * FROM `users` WHERE `email` = '$email'";
    $result = mysqli_query($conn, $sql);
    if(mysqli_num_rows($result)>0)
        {
            $status = "هذا البريد مسجل من قبل";
            echo json_encode(array("response"=>$status));

        }

      else
      {
 
        $InsertSQL = "INSERT INTO `users`(`name`, `email`, `password`, `code`, `verified`) VALUES ('$name','$email','$pass','$code','0')";
       
       if(mysqli_query($conn, $InsertSQL)or die(mysqli_error($conn)." update failed")){
        
                $status = "تم إرسال رمز التحقق";
                $to      = $email; // Send email to our user
                $subject = 'رمز التحقق'; // Give the email a subject 
                $message =
                            '
                            شكرا لتسجيلك معانا فى وفر  رمز التحقق الخاص بك هو
                                 
                                ------------------------
                                code = '.$code.'
                                ------------------------
                            ';
              $headers = 'From:noreply@waffar.com' . "\r\n"; // Set from headers
                mail($to, $subject, $message, $headers); // Send our email
                echo json_encode(array("response"=>$status));

       }
       else{
        
              $status = "حدث خطأ";
            echo json_encode(array("response"=>$status));

       }
    
      }
   
}  

  
?>  