<?php  

header('Content-Type: application/json');

include "DatabaseConfig.php";



if($_SERVER['REQUEST_METHOD'] == 'POST')
{
   
   
    $target_dir = "../images/";  
    $target_file_name = $target_dir .basename($_FILES["sectionImg"]["name"]);  
    $sections_name = $_POST["sectionName"];
    $sections_img = $_FILES["sectionImg"]["name"];
   


   if (move_uploaded_file($_FILES["sectionImg"]["tmp_name"], $target_file_name))   
   {  
       $fullPath = "https://onlinemarket288.000webhostapp.com/images/".$sections_img;

       $InsertSQL = "insert into sections (sectionName,sectionImg) values ('$sections_name','$fullPath')";
       
       if(mysqli_query($conn, $InsertSQL)){
        
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
}  

  
?>  