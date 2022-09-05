<?php  

header('Content-Type: application/json');
include "DatabaseConfig.php";


if($_SERVER['REQUEST_METHOD'] == 'POST')
{
   
   
    $userReport = $_POST["userReport"];
    $textReport = $_POST["textReport"];


       $InsertSQL = "insert into report (userReport,textReport) values ('$userReport','$textReport')";
       
       if(mysqli_query($conn, $InsertSQL)){
        
        $message = "تم توصيل مقترحك";
        $_item = array("message"=>$message);	 
         echo json_encode($_item);

       }
     
   else   
   {  
     
        $message = "حاول مرة اخرى";
        $_item = array("message"=>$message);	 
         echo json_encode($_item);
   }  
}  

  
?>  