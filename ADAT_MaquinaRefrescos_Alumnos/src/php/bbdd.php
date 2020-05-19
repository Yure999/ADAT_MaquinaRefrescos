<?php

/* Conexión con la BBDD*/

$servername = "localhost";
$user = "root";
$password = "";
$dbname = "adat_maquinarefrescos_jdbc";
$conn  =  new  mysqli($servername,  $user,$password, $dbname);
// Check connection
if ($conn->connect_error) {
	die("Error: " . $conn->connect_error);
}
if (!$conn->set_charset("utf8")) {
    die("Error cargando el conjunto de caracteres utf8: ". $conn->error);
}