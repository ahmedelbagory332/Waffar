<?php

//Define your host here.
$HostName = "localhost";

//Define your database username here.
$HostUser = "id19495893_root";

//Define your database password here.
$HostPass = "R62ldGba0$=A0o7M";

//Define your database name here.
$DatabaseName = "id19495893_market";

$conn = new mysqli($HostName, $HostUser, $HostPass, $DatabaseName);

if($conn)
{
     //echo "connected";
}
else
{
      //  echo "Not connected";
 
}

?>