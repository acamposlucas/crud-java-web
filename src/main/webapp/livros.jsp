<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.LivroDao"%>
<%@ page import="model.AutorDao"%>
<%@ page import="java.util.ArrayList"%>
<%
	@ SuppressWarnings ("unchecked")
	ArrayList<LivroDao> lista = (ArrayList<LivroDao>) request.getAttribute("livros");
%>
<%
	@ SuppressWarnings ("unchecked")
	ArrayList<AutorDao> autores = (ArrayList<AutorDao>) request.getAttribute("autores");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Lista de livros</title>
<link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
	<h1>Lista de livros</h1>
	<div class="container">
		<a href="adicionarLivro.html" class="btn">Adicionar Livro</a>
		<a href="report" class="btn">Gerar lista</a>
		<a href="main" class="btn">Listar todos os livros</a>
		<form name="filterForm" action="filter">
			<label for="listaAutores">Listar por autor: </label>
			<select name="listaAutores" id="listaAutores">
				<option value="" selected="selected">Selecionar autor</option>
				<% for(AutorDao autor : autores){ %>
					<option value="<%=autor.getId() %>"><%=autor.getNome() %></option>
				<%} %>
			</select>
		</form>
	</div>
	<table>
		<thead>
			<tr>
				<th>ID</th>
				<th>Título</th>
				<th>Autor</th>
				<th>Editora</th>
				<th>Ações</th>
			</tr>
		</thead>
		<tbody>
			<%for(LivroDao livro : lista) { %>
				<tr>
					<td><%=livro.getId() %></td>
					<td><%=livro.getTitulo() %></td>
					<td><%=livro.getAutor().getNome() %></td>
					<td><%=livro.getEditora() %></td>
					<td>
						<a href="select?id=<%=livro.getId() %>" class="btn">Editar</a>
						<a href="javascript: confirmDelete(<%=livro.getId() %>)" class="btn btn__danger">Remover</a>
					</td>
				</tr>
			<%} %>
		</tbody>
	</table>

<script src="js/confirmDelete.js" type="text/javascript"></script>
<script src="js/filter.js" type="text/javascript"></script>
</body>
</html>