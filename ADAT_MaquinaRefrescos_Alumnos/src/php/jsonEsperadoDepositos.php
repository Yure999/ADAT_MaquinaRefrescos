<?php

/*  Formato JSON esperado */

$arrEsperado = array();
$arrDepositoEsperado = array();

$arrEsperado["peticion"] = "add";

$arrDepositoEsperado["nombre"] = "CINCO CENTIMOS (Un string)";
$arrDepositoEsperado["valor"] = "50 (Un int)";
$arrDepositoEsperado["cantidad"] = "5 (Un int)";

$arrEsperado["nuevoDeposito"] = $arrDepositoEsperado;


/* Funcion para comprobar si el recibido es igual al esperado */

function JSONCorrectoAnnadir($recibido){
	
	$auxCorrecto = false;
	
	if(isset($recibido["peticion"]) && $recibido["peticion"] ="add" && isset($recibido["nuevoDeposito"])){
		
		$auxJugador = $recibido["nuevoDeposito"];
		if(isset($auxJugador["nombre"]) && isset($auxJugador["valor"]) && isset($auxJugador["cantidad"])){
			$auxCorrecto = true;
		}
		
	}
	return $auxCorrecto;
	
}
