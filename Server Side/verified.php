<?php  

header('Content-Type: application/json');

include "DatabaseConfig.php";

if($_SERVER['REQUEST_METHOD'] == 'POST')
{
  
    $email = $_POST["email"]; 
    $code = $_POST["code"];

$sql = "SELECT * FROM users where email = '$email' and code = '$code'";
$result = mysqli_query($conn, $sql);

if(!mysqli_num_rows($result)>0)
{
    $status = "تأكد من الكود";
    echo json_encode(array("response"=>$status));

}
else
{
    

    $sql = "UPDATE users SET verified = '1' where email = '$email'";
    
    if (mysqli_query($conn, $sql)) {
      $status = "تم التحقق بنجاح";
    echo json_encode(array("response"=>$status));
    } else {
      $status = "برجاء اعد المحاوله";
    echo json_encode(array("response"=>$status));
    }


}
mysqli_close($conn);
}

?>