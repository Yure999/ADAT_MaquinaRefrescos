<?php

/*  Formato JSON esperado */

$arrEsperado = array();
$arrDispensadorEsperado = array();

$arrEsperado["peticion"] = "add";

$arrDispensadorEsperado["clave"] = "coca (Un string)";
$arrDispensadorEsperado["nombre"] = "coca-cola (Un string)";
$arrDispensadorEsperado["precio"] = "150 (Un int)";
$arrDispensadorEsperado["cantidad"] = "1 (Un int)";

$arrEsperado["nuevoDispensador"] = $arrDispensadorEsperado;


/* Funcion para comprobar si el recibido es igual al esperado */

function JSONCorrectoAnnadir($recibido){
	
	$auxCorrecto = false;
	
	if(isset($recibido["peticion"]) && $recibido["peticion"] ="add" && isset($recibido["nuevoDispensador"])){
		
		$auxJugador = $recibido["nuevoDispensador"];
		if( isset($auxJugador["clave"]) && isset($auxJugador["nombre"]) && isset($auxJugador["precio"]) && isset($auxJugador["cantidad"])){
			$auxCorrecto = true;
		}
		
	}
	return $auxCorrecto;
	
}
