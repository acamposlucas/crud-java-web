package model;

public class AutorDao {

	private String id;
	private String nome;
	
	public AutorDao() {
		super();
	}

	public AutorDao(String id, String nome) {
		this.id = id;
		this.nome = nome;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return "AutorDao [nome=" + nome + "]";
	}

}
