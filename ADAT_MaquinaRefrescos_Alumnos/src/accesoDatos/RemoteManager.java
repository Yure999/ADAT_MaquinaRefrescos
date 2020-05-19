package accesoDatos;

import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import logicaRefrescos.Deposito;
import logicaRefrescos.Dispensador;

public class RemoteManager implements I_Acceso_Datos {

	ApiRequests encargadoPeticiones;
	String urlDepositos, urlDispensadores;

	public RemoteManager() {

		this.encargadoPeticiones = new ApiRequests();

		this.urlDepositos = "http://localhost/yure/MaquinaRefrescos/accesoDeposito.php";
		this.urlDispensadores = "http://localhost/yure/MaquinaRefrescos/accesoDispensadores.php";
	}

	public HashMap<Integer, Deposito> obtenerDepositos() {

		HashMap<Integer, Deposito> depositosCreados = new HashMap<Integer, Deposito>();

		try {
			String response = encargadoPeticiones.getRequest(urlDepositos);
			JSONObject respuesta = (JSONObject) JSONValue.parse(response.toString());

			if (respuesta == null) {

				System.out.println("El json recibido no es correcto. Finaliza la ejecución");
				System.exit(-1);
			} else {
				String estado = (String) respuesta.get("estado");
				if (estado.equals("ok")) {
					JSONArray array = (JSONArray) respuesta.get("depositos");

					if (array.size() > 0) {

						// Declaramos variables
						Deposito miDeposito;
						String nombre;
						int valor;
						int cantidad;

						for (int i = 0; i < array.size(); i++) {
							JSONObject row = (JSONObject) array.get(i);

							nombre = row.get("nombre").toString();
							valor = Integer.parseInt(row.get("valor").toString());
							cantidad = Integer.parseInt(row.get("cantidad").toString());
							miDeposito = new Deposito(nombre, valor, cantidad);

							depositosCreados.put(valor, miDeposito);
						}

						System.out.println("Acceso JSON Remoto - Leidos datos correctamente y generado hashmap");
						System.out.println();

					} else {
						System.out.println("Acceso JSON Remoto - No hay datos que tratar");
						System.out.println();
					}

				} else {

					System.out.println("Ha ocurrido un error en la busqueda de datos");
					System.out.println("Error: " + (String) respuesta.get("error"));
					System.out.println("Consulta: " + (String) respuesta.get("query"));

					System.exit(-1);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return depositosCreados;
	}

	public HashMap<String, Dispensador> obtenerDispensadores() {
		HashMap<String, Dispensador> dispensadoresCreados = new HashMap<String, Dispensador>();

		try {
			String response = encargadoPeticiones.getRequest(urlDispensadores);
			JSONObject respuesta = (JSONObject) JSONValue.parse(response.toString());

			if (respuesta == null) {

				System.out.println("El json recibido no es correcto. Finaliza la ejecución");
				System.exit(-1);
			} else {
				String estado = (String) respuesta.get("estado");
				if (estado.equals("ok")) {
					JSONArray array = (JSONArray) respuesta.get("Dispensadores");

					if (array.size() > 0) {

						// Declaramos variables
						Dispensador miDispensador;
						String clave;
						String nombre;
						int precio;
						int cantidad;

						for (int i = 0; i < array.size(); i++) {
							JSONObject row = (JSONObject) array.get(i);

							clave = row.get("clave").toString();
							nombre = row.get("nombre").toString();
							precio = Integer.parseInt(row.get("precio").toString());
							cantidad = Integer.parseInt(row.get("cantidad").toString());

							miDispensador = new Dispensador(clave, nombre, precio, cantidad);

							dispensadoresCreados.put(clave, miDispensador);
						}

						System.out.println("Acceso JSON Remoto - Leidos datos correctamente y generado hashmap");
						System.out.println();

					} else {
						System.out.println("Acceso JSON Remoto - No hay datos que tratar");
						System.out.println();
					}

				} else {

					System.out.println("Ha ocurrido un error en la busqueda de datos");
					System.out.println("Error: " + (String) respuesta.get("error"));
					System.out.println("Consulta: " + (String) respuesta.get("query"));

					System.exit(-1);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dispensadoresCreados;
	}

	public boolean guardarDepositos(HashMap<Integer, Deposito> depositos) {
		try {
			JSONObject objDeposito = new JSONObject();
			JSONObject objPeticion = new JSONObject();

			for (Integer ID : depositos.keySet()) {
				Deposito miDeposito = depositos.get(ID);

				objDeposito.put("nombre", miDeposito.getNombreMoneda());
				objDeposito.put("valor", miDeposito.getValor());
				objDeposito.put("cantidad", miDeposito.getCantidad());

				objPeticion.put("peticion", "modificar");
				objPeticion.put("nuevoDeposito", objDeposito);

				String json = objPeticion.toJSONString();
				String url = urlDepositos;
				String response = encargadoPeticiones.putRequest(url, json);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean guardarDispensadores(HashMap<String, Dispensador> dispensadores) {
		try {
			JSONObject objDispensador = new JSONObject();
			JSONObject objPeticion = new JSONObject();

			for (String clave : dispensadores.keySet()) {
				Dispensador miDispensador = dispensadores.get(clave);

				objDispensador.put("clave", miDispensador.getClave());
				objDispensador.put("nombre", miDispensador.getNombreProducto());
				objDispensador.put("precio", miDispensador.getPrecio());
				objDispensador.put("cantidad", miDispensador.getCantidad());

				objPeticion.put("peticion", "modificar");
				objPeticion.put("nuevoDispensador", objDispensador);

				String json = objPeticion.toJSONString();
				String url = urlDispensadores;
				String response = encargadoPeticiones.putRequest(url, json);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
