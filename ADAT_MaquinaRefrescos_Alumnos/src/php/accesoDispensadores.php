<?php

$metodo = $_SERVER['REQUEST_METHOD'];

switch ( $metodo ) {
    case 'PUT':
    guardar();
    break;


    case 'GET':
    leer();
    break;


    default:
    # code...
    break;
}

function guardar(){

    require 'bbdd.php'; 
    require 'jsonEsperadoDispensadores.php';
    
    $arrMensaje = array();  // Este array es el que codificaremos como JSON tanto si hay resultado como si hay error
    $parameters = file_get_contents("php://input");

if(isset($parameters)){

	// Parseamos el string json y lo convertimos a objeto JSON
	$mensajeRecibido = json_decode($parameters, true);
	// Comprobamos que están todos los datos en el json que hemos recibido
	// Funcion declarada en jsonEsperado.php
	if(JSONCorrectoAnnadir($mensajeRecibido)){

		$dispensador = $mensajeRecibido["DispensadorAnnadir"]; 
        
        $clave = $dispensador["clave"];
		$cantidad = $dispensador["cantidad"];
		$precio = $dispensador["precio"];
		$cantidad = $dispensador["cantidad"];
		
        $query  = "UPDATE Dispensadores SET cantidad = '$cantidad'";
        $query  .= " WHERE clave = '$clave'";
		
		$result = $conn->query ( $query );
		
		if (isset ( $result ) && $result) { // Si pasa por este if, la query está está bien y se ha insertado correctamente
			
			$arrMensaje["estado"] = "ok";
			$arrMensaje["mensaje"] = "dispensador $dispensador modificado correctamente";
			
		}else{ // Se ha producido algún error al ejecutar la query
			
			$arrMensaje["estado"] = "error";
			$arrMensaje["mensaje"] = "SE HA PRODUCIDO UN ERROR AL ACCEDER A LA BASE DE DATOS";
			$arrMensaje["error"] = $conn->error;
			$arrMensaje["query"] = $query;
			
		}

		
	}else{ // Nos ha llegado un json no tiene los campos necesarios
		
		$arrMensaje["estado"] = "error";
		$arrMensaje["mensaje"] = "EL JSON NO CONTIENE LOS CAMPOS ESPERADOS";
		$arrMensaje["recibido"] = $mensajeRecibido;
		$arrMensaje["esperado"] = $arrEsperado;
	}

}else{	// No nos han enviado el json correctamente
	
	$arrMensaje["estado"] = "error";
	$arrMensaje["mensaje"] = "EL JSON NO SE HA ENVIADO CORRECTAMENTE";
	
}

$mensajeJSON = json_encode($arrMensaje,JSON_PRETTY_PRINT);

//echo "<pre>";  // Descomentar si se quiere ver resultado "bonito" en navegador. Solo para pruebas
echo $mensajeJSON;
//echo "</pre>"; // Descomentar si se quiere ver resultado "bonito" en navegador

$conn->close ();
}

function leer(){

    require 'bbdd.php'; 
    require 'jsonEsperadoDispensadores.php';

    $arrMensaje = array();  // Este array es el codificaremos como JSON tanto si hay resultado como si hay error

$query = "SELECT * FROM Dispensadores";

$result = $conn->query ( $query );

if (isset ( $result ) && $result) { // Si pasa por este if, la query está está bien y se obtiene resultado
	
	if ($result->num_rows > 0) { // Aunque la query esté bien puede no obtenerse resultado (tabla vacía). Comprobamos antes de recorrer
		
		$arrDispensadores = array();
		
		while ( $row = $result->fetch_assoc () ) {
			
			// Por cada vuelta del bucle creamos un jugador. Como es un objeto hacemos un array asociativo
			$arrDispensador = array();
            // Por cada columna de la tabla creamos una propiedad para el objeto
            $arrDispensador["clave"] = $row["clave"];
			$arrDispensador["nombre"] = $row["nombre"];
			$arrDispensador["precio"] = $row["precio"];
			$arrDispensador["cantidad"] = $row["cantidad"];
			// Por último, añadimos el nuevo jugador al array de Dispensadores
			$arrDispensadores[] = $arrDispensador;
			
		}
		
		// Añadimos al $arrMensaje el array de Dispensadores y añadimos un campo para indicar que todo ha ido OK
		$arrMensaje["estado"] = "ok";
		$arrMensaje["Dispensadores"] = $arrDispensadores;
		
		
	} else {
		
		$arrMensaje["estado"] = "ok";
		$arrMensaje["Dispensadores"] = []; // Array vacío si no hay resultados
	}
	
} else {
	
	$arrMensaje["estado"] = "error";
	$arrMensaje["mensaje"] = "SE HA PRODUCIDO UN ERROR AL ACCEDER A LA BASE DE DATOS";
	$arrMensaje["error"] = $conn->error;
	$arrMensaje["query"] = $query;
	
}

$mensajeJSON = json_encode($arrMensaje,JSON_PRETTY_PRINT);

//echo "<pre>";  // Descomentar si se quiere ver resultado "bonito" en navegador. Solo para pruebas 
echo $mensajeJSON;
//echo "</pre>"; // Descomentar si se quiere ver resultado "bonito" en navegador

$conn->close ();

}
?>