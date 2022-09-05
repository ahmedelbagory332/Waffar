<?php  

header('Content-Type: application/json');

include "DatabaseConfig.php";

if($_SERVER['REQUEST_METHOD'] == 'POST')
{
  
    $email = $_POST["email"]; 
    $password = $_POST["password"];

$sql = "SELECT * FROM users where email = '$email' and password = '$password'";
$result = mysqli_query($conn, $sql);

if(!mysqli_num_rows($result)>0)
{
    $status = "تأكد من البريد او الرقم السرى";
    echo json_encode(array("response"=>$status));

}
else
{
    	while($row = $result->fetch_assoc()) {

	    $status = $row["verified"];
    	}
        echo json_encode(array("response"=>$status));


}
mysqli_close($conn);
}

?>