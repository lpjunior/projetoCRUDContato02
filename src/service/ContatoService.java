package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entity.Contato;
import persist.ContatoDAO;

public class ContatoService {

	private ContatoDAO dao;
	
	public ContatoService() {
		dao = new ContatoDAO();
	}
	
	public boolean adicionarContato(Contato contato) {
		try {
			dao.addContato(contato);
			return Boolean.TRUE;
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error na ContatoService. Error: " + e.getMessage());
		}
		return Boolean.FALSE;
	}
	
	public List<Contato> lista() {
		try {
			return dao.findAllContatos();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}
}
