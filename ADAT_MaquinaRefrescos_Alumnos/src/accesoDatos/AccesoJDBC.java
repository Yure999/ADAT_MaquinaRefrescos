package accesoDatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

import auxiliares.LeeProperties;
import logicaRefrescos.Deposito;
import logicaRefrescos.Dispensador;

public class AccesoJDBC implements I_Acceso_Datos {

	private String driver, urlbd, user, password; // Datos de la conexion
	private Connection conn1;

	public AccesoJDBC() {
		System.out.println("ACCESO A DATOS - Acceso JDBC");
		
		try {
			HashMap<String,String> datosConexion;
			
			LeeProperties properties = new LeeProperties("Ficheros/config/accesoJDBC.properties");
			datosConexion = properties.getHash();		
			
			driver = datosConexion.get("driver");
			urlbd = datosConexion.get("urlbd");
			user = datosConexion.get("user");
			password = datosConexion.get("password");
			conn1 = null;
			
			Class.forName(driver);
			conn1 = DriverManager.getConnection(urlbd, user, password);
			if (conn1 != null) {
				System.out.println("Conectado a la base de datos");
			} 

		} catch (ClassNotFoundException e1) {
			System.out.println("ERROR: No Conectado a la base de datos. No se ha encontrado el driver de conexion");
			//e1.printStackTrace();
			System.out.println("No se ha podido inicializar la maquina\n Finaliza la ejecucion");
			System.exit(1);
		} catch (SQLException e) {
			System.out.println("ERROR: No se ha podido conectar con la base de datos");
			System.out.println(e.getMessage());
			//e.printStackTrace();
			System.out.println("No se ha podido inicializar la maquina\n Finaliza la ejecucion");
			System.exit(1);
		}
	}

	public int cerrarConexion() {
		try {
			conn1.close();
			System.out.println("Cerrada conexion");
			return 0;
		} catch (Exception e) {
			System.out.println("ERROR: No se ha cerrado corretamente");
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public HashMap<Integer, Deposito> obtenerDepositos() {
		HashMap<Integer, Deposito> depositosCreados = new HashMap<Integer,Deposito>();
		ResultSet rs;
		String query = "select * from depositos";
		try {
			PreparedStatement pst = conn1.prepareStatement(query);
			rs = pst.executeQuery();
			while(rs.next()) {
				String nombre = rs.getString("nombre");
				int valor = rs.getInt("valor");
				int cantidad = rs.getInt("cantidad");
				Deposito miDeposito = new Deposito(nombre,valor,cantidad);
				depositosCreados.put(valor, miDeposito);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return depositosCreados;

	}

	@Override
	public HashMap<String, Dispensador> obtenerDispensadores() {
		HashMap<String, Dispensador> dispensadoresCreados = new HashMap<String,Dispensador>();;
		ResultSet rs;
		String query = "select * from dispensadores";
		try {
			PreparedStatement pst = conn1.prepareStatement(query);
			rs = pst.executeQuery();
			while(rs.next()) {
				String clave = rs.getString("clave");
				String nombre = rs.getString("nombre");
				int precio = rs.getInt("precio");
				int cantidad = rs.getInt("cantidad");
				Dispensador miDispensador = new Dispensador(clave,nombre,precio,cantidad);
				dispensadoresCreados.put(clave,miDispensador);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dispensadoresCreados;
	}

	public boolean guardarDepositos(HashMap<Integer, Deposito> depositos) {
		boolean todoOK = true;
		String query = "update depositos set cantidad = ?\r\n" + 
				"where valor = ?;";
		try {
			PreparedStatement pst = conn1.prepareStatement(query);
			for (Integer ID : depositos.keySet()) {
				Deposito miDeposito = depositos.get(ID);
				pst.setInt(1, miDeposito.getCantidad());
				pst.setInt(2, miDeposito.getValor());
				pst.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			todoOK = false;
		}
		return todoOK;
	}

	public boolean guardarDispensadores(HashMap<String, Dispensador> dispensadores) {
		boolean todoOK = true;
		String query = "update dispensadores set cantidad = ?\r\n" + 
				"where clave = ?;";
		try {
			PreparedStatement pst = conn1.prepareStatement(query);
			for (String clave : dispensadores.keySet()) {
				Dispensador miDispensador = dispensadores.get(clave);
				pst.setInt(1, miDispensador.getCantidad());
				pst.setString(2, miDispensador.getClave());
				pst.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			todoOK = false;
		}

		return todoOK;
	}

} // Fin de la clase