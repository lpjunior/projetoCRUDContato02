package persist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entity.Contato;
import entity.Endereco;

public class ContatoDAO extends DAO {

	private Connection connection;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	public ContatoDAO() {
	}

	public void addContato(Contato contato) throws SQLException {
		abrirConexao();

		try {
			String sql = "insert into contato(nome, email, telefone) values (?, ?, ?)";

			pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, contato.getNome());
			pstmt.setString(2, contato.getEmail());
			pstmt.setString(3, contato.getTelefone());

			if (pstmt.executeUpdate() == 0) {
				throw new SQLException("Ocorreu um erro ao inserir contato");
			}


			try (ResultSet key = pstmt.getGeneratedKeys()) {
				if (key.next()) {

					sql = "insert into endereco(logradouro, cidade, estado, contatoId) values (?, ?, ?, ?)";

					long id = key.getLong(1);

					pstmt.close();
					
					pstmt = connection.prepareStatement(sql);
					pstmt.setString(1, contato.getEndereco().getLogradouro());
					pstmt.setString(2, contato.getEndereco().getCidade());
					pstmt.setString(3, contato.getEndereco().getEstado());
					pstmt.setLong(4, id);

					if (pstmt.executeUpdate() == 0) {
						throw new SQLException("Ocorreu um erro ao inserir endereco");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erro ao efetuar os inserts. Error: " + e.getMessage());
			connection.rollback();
		} finally {
			try {
				fechaStatements();
			} catch (SQLException e) {
				e.printStackTrace();
				System.err.println("Erro ao fechar os Statements");
			}
		}
	}

	public Contato findContatoId(Long id) throws SQLException {
		abrirConexao();
		
		String sql = "Select c.id, c.nome, c.email, c.telefone, e.logradouro, e.cidade, e.estado from contato c "
				+ "inner join endereco e "
				+ "on c.id = e.contatoId where c.id = ?";
		try {
			pstmt = connection.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				return new Contato(
						rs.getLong(1),
						rs.getString(2),
						rs.getString(3),
						rs.getString(4),
						new Endereco(rs.getString(5), rs.getString(6), rs.getString(7))
						);
			}
			
			return null;
		} finally {
			try {
				fechaStatements();
			} catch (SQLException e) {
				e.printStackTrace();
				System.err.println("Erro ao fechar os Statements");
			}
		}
	}

	public List<Contato> findAllContatos() throws SQLException {
		abrirConexao();
		
		String sql = "Select c.id, c.nome, c.email, c.telefone, e.logradouro, e.cidade, e.estado from contato c "
				+ "inner join endereco e "
				+ "on c.id = e.contatoId";
		try {
			pstmt = connection.prepareStatement(sql);
			rs = pstmt.executeQuery();
			List<Contato> contatos = new ArrayList<Contato>();
			while(rs.next()) {
				contatos.add(new Contato(
						rs.getLong(1),
						rs.getString(2),
						rs.getString(3),
						rs.getString(4),
						new Endereco(rs.getString(5), rs.getString(6), rs.getString(7))
						));
				return contatos;
			}
			
			return null;
		} finally {
			try {
				fechaStatements();
			} catch (SQLException e) {
				e.printStackTrace();
				System.err.println("Erro ao fechar os Statements");
			}
		}
	}
	
	public void deleteContato(Long id) throws SQLException {
		abrirConexao();
		
		String sql = "delete from contato where id = ?";
		try {
			pstmt = connection.prepareStatement(sql);
			
			if (pstmt.executeUpdate() == 0) {
				throw new SQLException("Ocorreu um erro ao inserir endereco");
			}
		} finally {
			try {
				fechaStatements();
			} catch (SQLException e) {
				e.printStackTrace();
				System.err.println("Erro ao fechar os Statements");
			}
		}
	}
	
	public void updateContato(Contato contato) throws SQLException {
		abrirConexao();
		
		String sql = "update contato set nome = ?, email = ?, telefone = ? where id = ";
		try {
			pstmt = connection.prepareStatement(sql);
			
			if (pstmt.executeUpdate() == 0) {
				throw new SQLException("Ocorreu um erro ao inserir endereco");
			}
		} finally {
			try {
				fechaStatements();
			} catch (SQLException e) {
				e.printStackTrace();
				System.err.println("Erro ao fechar os Statements");
			}
		}
	}

	private void abrirConexao() {
		try {
			connection = openConnect();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Não foi possível abrir a conexão com o banco. Error: " + e.getErrorCode() + " - "
					+ e.getMessage());
		}
	}

	private void fechaStatements() throws SQLException {
		if (connection != null)
			connection.close();
		if (pstmt != null)
			pstmt.close();
		if (rs != null)
			rs.close();
	}

}
