package persist;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Data Access Object
public class DAO {

	public DAO() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.err.println("Classe não localizada ao abrir conexão. Error: " + e.getMessage());
		}
	}
	
	protected Connection openConnect() throws SQLException {
		return DriverManager.getConnection("jdbc:mysql://localhost/cruddb2", "root", "");
	}
}
