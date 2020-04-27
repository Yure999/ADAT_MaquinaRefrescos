package accesoDatos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

import logicaRefrescos.Deposito;
import logicaRefrescos.Dispensador;
/*
 * Todas los accesos a datos implementan la interfaz de Datos
 */

public class FicherosTexto implements I_Acceso_Datos {

	File fDis; // FicheroDispensadores
	File fDep; // FicheroDepositos

	public FicherosTexto() {
		System.out.println("ACCESO A DATOS - FICHEROS DE TEXTO");
	}

	@Override
	public HashMap<Integer, Deposito> obtenerDepositos() {
		HashMap<Integer, Deposito> depositosCreados = new HashMap<Integer, Deposito>();

		try {
			File file = new File("Ficheros/datos/depositos.txt");
			BufferedReader br = new BufferedReader(new FileReader(file));
			String st;
			while ((st = br.readLine()) != null) {
				String[] readedDataArray = st.split(";");
				Deposito miDeposito = new Deposito(readedDataArray[0], Integer.parseInt(readedDataArray[1]),
						Integer.parseInt(readedDataArray[2]));
				Integer ID = Integer.parseInt(readedDataArray[1]);
				depositosCreados.put(ID, miDeposito);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return depositosCreados;
	}

	@Override
	public HashMap<String, Dispensador> obtenerDispensadores() {
		HashMap<String, Dispensador> dispensadoresCreados = new HashMap<String, Dispensador>();
		try {
			File file = new File("Ficheros/datos/dispensadores.txt");
			BufferedReader br = new BufferedReader(new FileReader(file));
			String st;
			while ((st = br.readLine()) != null) {
				String[] readedDataArray = st.split(";");
				Dispensador miDispensador = new Dispensador(readedDataArray[0], readedDataArray[1],
						Integer.parseInt(readedDataArray[2]), Integer.parseInt(readedDataArray[3]));
				String ID = readedDataArray[0];
				dispensadoresCreados.put(ID, miDispensador);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dispensadoresCreados;
	}

	@Override
	public boolean guardarDepositos(HashMap<Integer, Deposito> depositos) {
		boolean todoOK = true;
		File file = new File("Ficheros/datos/depositos.txt");
		BufferedWriter out;

		try {
			out = new BufferedWriter(new FileWriter(file, false));
			for (Integer ID : depositos.keySet()) {
				Deposito miDeposito = depositos.get(ID);
				out.write(miDeposito.getNombreMoneda() + ";" + miDeposito.getValor() + ";" + miDeposito.getCantidad());
				out.newLine();
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			todoOK = false;
		}
		return todoOK;
	}

	@Override
	public boolean guardarDispensadores(HashMap<String, Dispensador> dispensadores) {
		boolean todoOK = true;
		File file = new File("Ficheros/datos/dispensadores.txt");
		BufferedWriter out;

		try {
			out = new BufferedWriter(new FileWriter(file, false));
			for (String clave : dispensadores.keySet()) {
				Dispensador miDispensador = dispensadores.get(clave);
				out.write(miDispensador.getClave() + ";" + miDispensador.getNombreProducto() + ";"
						+ miDispensador.getPrecio() + ";" + miDispensador.getCantidad());
				out.newLine();
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			todoOK = false;
		}
		return todoOK;
	}
} // Fin de la clase