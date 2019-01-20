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

//This function takes a string returned from the DB and parses to XML
function parseToXML($htmlStr)
{
  $xmlStr = str_replace('<', '&lt;', $htmlStr);
  $xmlStr = str_replace('>', '&gt;', $xmlStr);
  $xmlStr = str_replace('"', '&quot;', $xmlStr);
  $xmlStr = str_replace("'", '&#39;', $xmlStr);
  $xmlStr = str_replace("&", '&amp;', $xmlStr);
  return $xmlStr;
}

$conn = connectToDB();

if (defined('STDIN')) {
  $ownerOption = $argv[1];
  $fullnessOption = $argv[2];
} else {
  $ownerOption = $_GET['ownerOption'];
  $fullnessOption = $_GET['fullnessOption'];
}

$sql = "";
if ($ownerOption == 'All') {
  $sql = "SELECT UnitId, OwnerId, Latitude, Longitude, registration.Address 
  FROM registration";
} else {
  $sql = "SELECT UnitId, OwnerId, Latitude, Longitude, registration.Address 
 FROM registration
 WHERE OwnerId='" . $ownerOption . "' ";
}

$result = mysqli_query($conn, $sql);

if (!$result) {
  die('Invalid query: ' . mysql_error());
}

header("Content-type: text/xml");
echo "<?xml version='1.0' ?>";
echo '<markers>';

  // Iterate through the rows, printing XML nodes for each
while ($row = @mysqli_fetch_assoc($result)) {

  $sql2 = "SELECT EventType FROM eventsLog
    WHERE (EventType = 0 OR EventType = 1) AND UnitID=" . $row['UnitId'] . "
	ORDER BY Id DESC
    LIMIT 1";
  $result2 = mysqli_query($conn, $sql2);
  $eventtype = mysqli_fetch_object($result2);

  if ($fullnessOption == "1") {
    if (!($eventtype->EventType == 1)) {
      continue;
    }
  } elseif ($fullnessOption == "0") {
    if (!($eventtype->EventType == 0)) {
      continue;
    }
  }

    // Add to XML document node
  echo '<marker ';
  echo 'UnitId="' . $row['UnitId'] . '" ';
  echo 'OwnerId="' . parseToXML($row['OwnerId']) . '" ';
  echo 'Address="' . parseToXML($row['Address']) . '" ';
  echo 'Latitude="' . $row['Latitude'] . '" ';
  echo 'Longitude="' . $row['Longitude'] . '" ';
  if (!($eventtype == null)) {
    echo 'Fullness="' . $eventtype->EventType . '" ';
  } else {
    echo 'Fullness="0" ';
  }
  echo '/>';
}
echo '</markers>';

$result->free();
$result2->free();
mysqli_close($conn);
?>
