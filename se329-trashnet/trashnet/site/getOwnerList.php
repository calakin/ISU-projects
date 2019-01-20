<?php

function connectToDB()
{

  $host = "localhost";
  //$host = "trashnet.ece.iastate.edu";
  $port = 3306;
  $user = "logan";
  $password = "ROFLdb!789";
  $dbname = "trashnet_db";

  $conn = mysqli_connect($host, $user, $password, $dbname, $port);

  if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
  }
  if ($conn) {
      //echo "Connection successful";
  }

  return $conn;
}

$conn = connectToDB();

$sql = "SELECT DISTINCT registration.OwnerID
FROM registration";

$result = mysqli_query($conn, $sql);

if (!$result) {
    die('Invalid query: ' . mysql_error());
}

$data = array();
while($row = mysqli_fetch_assoc($result)) {
    $data[] = $row;
}

echo json_encode($data);

?>
