package accesoDatos;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Query;

import logicaRefrescos.Deposito;
import logicaRefrescos.Dispensador;

public class HibernateManager implements I_Acceso_Datos {
	Session session;
	
	public HibernateManager() {
		HibernateUtil util = new HibernateUtil();
		session = util.getSessionFactory().openSession();
	}

	public HashMap<Integer, Deposito> obtenerDepositos() {
		HashMap<Integer, Deposito> data = new HashMap<Integer, Deposito>();
		session.beginTransaction();
		Query q = session.createQuery("select d from Deposito d");
		List results = q.list();
		
		Iterator<Deposito> depositoIterator = results.iterator();
		while (depositoIterator.hasNext()) {
			Deposito readedData = depositoIterator.next();
			data.put(readedData.getValor(), readedData);
		}
		return data;
	}

	public HashMap<String, Dispensador> obtenerDispensadores() {
		HashMap<String, Dispensador> data = new HashMap<String, Dispensador>();
		session.beginTransaction();
		Query q = session.createQuery("select d from Dispensador d");
		List results = q.list();
		
		Iterator<Dispensador> dispensadorIterator = results.iterator();
		while (dispensadorIterator.hasNext()) {
			Dispensador readedData = dispensadorIterator.next();
			data.put(readedData.getClave(), readedData);
		}
		return data;
	}

	public boolean guardarDepositos(HashMap<Integer, Deposito> depositos) {
		session.beginTransaction();
		for (Integer id : depositos.keySet()) {
			session.save(depositos.get(id));
		}
		return true;
	}

	public boolean guardarDispensadores(HashMap<String, Dispensador> dispensadores) {
		session.beginTransaction();
		for (String clave : dispensadores.keySet()) {
			session.save(dispensadores.get(clave));
		}
		return true;
	}

}
