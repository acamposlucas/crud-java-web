package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import model.AutorDao;
import model.DAO;
import model.LivroDao;

// TODO: Auto-generated Javadoc
/**
 * The Class Controller.
 */
@WebServlet(urlPatterns = { "/Controller", "/main", "/insert", "/select", "/update", "/delete", "/report", "/filter" })
public class Controller extends HttpServlet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	DAO dao = new DAO();
	LivroDao livro = new LivroDao();
	AutorDao autor = new AutorDao();

	public Controller() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getServletPath();
		System.out.print("Servlet Path: ");
		System.out.println(action);
		
		if (action.equals("/main")) {
			listarLivros(request, response);
		} else if (action.equals("/insert")) {
			adicionarLivro(request, response);
		} else if (action.equals("/select")) {
			selecionarLivro(request, response);
		} else if (action.equals("/update")) {
			editarLivro(request, response);
		} else if (action.equals("/delete")) {
			removerLivro(request, response);
		} else if (action.equals("/report")) {
			gerarListaDeLivros(request, response);
		} else if (action.equals("/filter")) {
			listarLivrosPorAutor(request, response);
		} else {
			response.sendRedirect("index.html");
		}
	}

	protected void listarLivros(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ArrayList<LivroDao> livros = dao.listarLivros();
		ArrayList<AutorDao> autores = dao.listarAutores();

		request.setAttribute("livros", livros);
		request.setAttribute("autores", autores);
		RequestDispatcher rd = request.getRequestDispatcher("livros.jsp");
		rd.forward(request, response);
	}

	/*
	protected void listarAutores(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ArrayList<AutorDao> autores = dao.listarAutores();

		request.setAttribute("autores", autores);
		RequestDispatcher rd = request.getRequestDispatcher("livros.jsp");
		rd.forward(request, response);
	}
	*/

	/** LISTAR LIVROS POR AUTOR **/
	protected void listarLivrosPorAutor(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ArrayList<LivroDao> listaDeLivrosPorAutor = dao.listarLivrosPorAutor(request.getParameter("autor"));
		ArrayList<AutorDao> autores = dao.listarAutores();
		
		request.setAttribute("livros", listaDeLivrosPorAutor);
		request.setAttribute("autores", autores);
		RequestDispatcher rd = request.getRequestDispatcher("livros.jsp");
		rd.forward(request, response);
	}

	protected void adicionarLivro(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		livro.setTitulo(request.getParameter("titulo"));
		livro.setEditora(request.getParameter("editora"));

		autor.setNome(request.getParameter("autor"));
		livro.setAutor(autor);

		dao.inserirLivro(livro);
		response.sendRedirect("main");
	}

	/**
	 * Selecionar livro.
	 */
	protected void selecionarLivro(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		livro.setId(request.getParameter("id"));

		// Executa o método para selecionar Livro
		dao.selecionaLivro(livro);

		// Define atributos do formulário com os dados do Livro
		request.setAttribute("id", livro.getId());
		request.setAttribute("titulo", livro.getTitulo());
		request.setAttribute("autor", livro.getAutor().getNome());
		request.setAttribute("editora", livro.getEditora());

		// Encaminhar para editarLivro.jsp
		RequestDispatcher rd = request.getRequestDispatcher("editarLivro.jsp");
		rd.forward(request, response);
	}

	/**
	 * Editar livro.
	 */
	protected void editarLivro(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		livro.setId(request.getParameter("id"));
		livro.setTitulo(request.getParameter("titulo"));
		autor.setNome(request.getParameter("autor"));
		livro.setAutor(autor);
		livro.setEditora(request.getParameter("editora"));

		dao.editarLivro(livro);

		response.sendRedirect("main");
	}

	/**
	 * Remover livro.
	 */
	protected void removerLivro(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Recebe id do Livro a ser excluído
		String id = request.getParameter("id");

		livro.setId(id);

		dao.deletarLivro(livro);

		response.sendRedirect("main");
	}

	/**
	 * Gerar lista de livros.
	 *
	 * @param request  the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	protected void gerarListaDeLivros(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Document document = new Document();

		try {
			// Tipo de conteúdo
			response.setContentType("application/pdf");

			// Nome do documento
			response.addHeader("Content-Disposition", "inline; filename=" + "livros.pdf");

			// Cria o documento
			PdfWriter.getInstance(document, response.getOutputStream());

			// Abre o documento para gerar o conteúdo
			document.open();
			document.add(new Paragraph("Lista de livros"));
			document.add(new Paragraph(" "));

			// Criar uma tabela
			PdfPTable table = new PdfPTable(3); // Tabela com três colunas
			PdfPCell col1 = new PdfPCell(new Paragraph("Título"));
			PdfPCell col2 = new PdfPCell(new Paragraph("Autor"));
			PdfPCell col3 = new PdfPCell(new Paragraph("Editora"));

			table.addCell(col1);
			table.addCell(col2);
			table.addCell(col3);

			// Adiciona livros à tabela
			ArrayList<LivroDao> lista = dao.listarLivros();

			for (LivroDao livro : lista) {
				System.out.println(livro);
				table.addCell(livro.getTitulo());
				table.addCell(livro.getAutor().getNome());
				table.addCell(livro.getEditora());
			}

			document.add(table);
		} catch (Exception e) {
			System.out.println(e);

		} finally {
			document.close();
		}
	}
}
