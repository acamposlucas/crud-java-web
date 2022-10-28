package model;

public class LivroDao {

	private String id;
	private String titulo;
	private String editora;
	private AutorDao autor;

	public LivroDao() {
		super();
	}

	public LivroDao(String id, String titulo, String editora, AutorDao autor) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.editora = editora;
		this.autor = autor;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getEditora() {
		return editora;
	}

	public void setEditora(String editora) {
		this.editora = editora;
	}

	public AutorDao getAutor() {
		return autor;
	}

	public void setAutor(AutorDao autor) {
		this.autor = autor;
	}

	@Override
	public String toString() {
		return "LivroDao [id=" + id + ", titulo=" + titulo + ", editora=" + editora + ", autor=" + autor + "]";
	}
	
}
