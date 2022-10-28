package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class DAO.
 */
public class DAO {

	private String driver = "com.mysql.cj.jdbc.Driver";
	private String url = "jdbc:mysql://localhost:3306/dblivraria?useTimezone=true&serverTimezone=UTC";
	private String user = "root";
	private String password = "a1b2c3d4";

	private Connection getConnection() {
		// Cria um objeto
		Connection conn = null;

		// Trata as exceções
		try {
			// Usa o Driver
			Class.forName(driver);
			// Obtem parâmetros da conexão e conecta
			conn = DriverManager.getConnection(url, user, password);
			// Retorna conexão
			return conn;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	/**
	 * CRUD CREATE *.
	 * 
	 * @param livro the livro
	 */
	public void inserirLivro(LivroDao livro) {

		try {
			// Abrir a conexão com o DB
			Connection conn = getConnection();
			PreparedStatement pst = null;
			
			// Procurar autor no DB.
			pst = conn.prepareStatement("SELECT id FROM tbautores WHERE nome = ?");
			pst.setString(1, livro.getAutor().getNome());
			ResultSet rs = pst.executeQuery();
			
			if (rs.next()) {
				livro.getAutor().setId(rs.getString("id"));
			} else {
				pst = conn.prepareStatement("INSERT nome INTO tbautores VALUES (?)", Statement.RETURN_GENERATED_KEYS);
				pst.setString(1, livro.getAutor().getNome());
				int rowsAffected = pst.executeUpdate();
				
				if (rowsAffected > 0) {
					rs = pst.getGeneratedKeys();
					
					if(rs.next()) {
						String id = rs.getString("id");
						livro.getAutor().setId(id);
					}
				} else {
					System.out.println("Erro no cadastro de novo autor");
				}
			}

			// Prepara a QUERY para execução de linha no DB
			pst = conn.prepareStatement("INSERT INTO tblivros (titulo, autorId, editora) " + "VALUES (?, ?, ?)");

			// Substitui os parâmetros pelo conteúdo das variáveis do objeto LivroDao
			pst.setString(1, livro.getTitulo());
			pst.setString(2, livro.getAutor().getId());
			pst.setString(3, livro.getEditora());

			// Executa a QUERY
			pst.executeUpdate();

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Inserir autor.
	 * 
	 * @param autor the autor
	 */
	public void inserirAutor(AutorDao autor) {
		String insert = "INSERT INTO autores (nome) VALUES (?)";

		try {
			// Abrir a conexão com o DB
			Connection conn = getConnection();

			// Prepara a QUERY para execução de linha no DB
			PreparedStatement pst = conn.prepareStatement(insert);
			pst.setString(1, autor.getNome());

			// Executa a QUERY
			pst.executeUpdate();

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * CRUD READ *.
	 */
	public ArrayList<LivroDao> listarLivros() {
		// Cria um objeto para acessar a classe LivroDao
		ArrayList<LivroDao> livros = new ArrayList<>();

		String read = "SELECT tblivros.id, titulo, tbautores.nome as autor, editora FROM tblivros INNER JOIN tbautores ON tblivros.autorId = tbautores.id;";

		try {
			Connection conn = getConnection();
			PreparedStatement pst = conn.prepareStatement(read);
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				String id = rs.getString("id");
				String titulo = rs.getString("titulo");
				String autorDb = rs.getString("autor");
				String editora = rs.getString("editora");

				AutorDao autor = new AutorDao();
				autor.setNome(autorDb);

				LivroDao livro = new LivroDao();
				livro.setId(id);
				livro.setAutor(autor);
				livro.setTitulo(titulo);
				livro.setEditora(editora);
				livros.add(livro);
			}

			conn.close();

			return livros;

		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	public ArrayList<AutorDao> listarAutores() {
		ArrayList<AutorDao> autores = new ArrayList<>();

		try {
			Connection conn = getConnection();
			PreparedStatement pst = conn.prepareStatement("SELECT * FROM tbautores");
			ResultSet rs = pst.executeQuery();
			
			while (rs.next()) {
				AutorDao autor = new AutorDao(rs.getString("id"), rs.getString("nome"));
				autores.add(autor);
			}
			
			conn.close();
			return autores;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	public ArrayList<LivroDao> listarLivrosPorAutor(String autorParam) {
		ArrayList<LivroDao> lista = new ArrayList<>();

		try {
			Connection conn = getConnection();
			PreparedStatement pst = conn.prepareStatement(
					"SELECT titulo, tblivros.id, tbautores.nome as autor, tbautores.id as autorId, editora "
					+ "FROM tblivros "
					+ "INNER JOIN tbautores "
					+ "ON tblivros.autorId = tbautores.id "
					+ "WHERE autorId = ?"
					);
			pst.setString(1, autorParam);
			ResultSet rs = pst.executeQuery();
			
			AutorDao autor = new AutorDao();
			while (rs.next()) {
				autor.setNome(rs.getString("autor"));
				autor.setId(rs.getString("autorId"));
				lista.add(new LivroDao(rs.getString("id"), rs.getString("titulo"), rs.getString("editora"), autor));
			}

			conn.close();

			return lista;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	/**
	 * CRUD UPDATE *.
	 */
	public void selecionaLivro(LivroDao livro) {
		AutorDao autor = new AutorDao();

		try {
			Connection conn = getConnection();

			PreparedStatement pst = conn.prepareStatement(
			"SELECT tblivros.id, titulo, tbautores.nome as autor, editora "
			+ "FROM tblivros INNER JOIN tbautores "
			+ "ON tblivros.autorId = tbautores.id "
			+ "WHERE tblivros.id = ?"
			);
			
			pst.setString(1, livro.getId());

			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				livro.setId(rs.getString("id"));
				livro.setTitulo(rs.getString("titulo"));
				livro.setEditora(rs.getString("editora"));
				autor.setNome(rs.getString("autor"));
				livro.setAutor(autor);
			}

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Editar livro.
	 */
	public void editarLivro(LivroDao livro) {
		
		try {
			Connection conn = getConnection();
			
			PreparedStatement pst = null;
			
			pst = conn.prepareStatement("SELECT id FROM tbautores WHERE nome = ?");
			pst.setString(1, livro.getAutor().getNome());
			ResultSet rs = pst.executeQuery();
			
			if (rs.next()) {
				livro.getAutor().setId(rs.getString("id"));
			} else {
				pst = conn.prepareStatement("INSERT INTO tbautores (nome) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
				pst.setString(1, livro.getAutor().getNome());
				int rowsAffected = pst.executeUpdate();
				
				if (rowsAffected > 0) {
					rs = pst.getGeneratedKeys();
					
					if(rs.next()) {
						String id = rs.getString(1);
						livro.getAutor().setId(id);
					}
				} else {
					System.out.println("Erro no cadastro de novo autor");
				}
			}

			pst = conn.prepareStatement(
					"UPDATE tblivros SET titulo = ?, autorId = ?, editora = ? "
					+ "WHERE id = ?"
					);
			
			pst.setString(1, livro.getTitulo());
			pst.setString(2, livro.getAutor().getId());
			pst.setString(3, livro.getEditora());
			pst.setString(4, livro.getId());

			pst.executeUpdate();

			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * CRUD DELETE *.
	 */
	public void deletarLivro(LivroDao livro) {

		try {
			Connection conn = getConnection();

			PreparedStatement pst = conn.prepareStatement("DELETE FROM tblivros WHERE id = ?");
			pst.setString(1, livro.getId());

			pst.executeUpdate();

			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
