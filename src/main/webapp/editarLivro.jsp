<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Editar Livro</title>
<link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
	<h1>Editar Livro</h1>
	<form name="editarLivro" action="update">
		<div class="input__container">
			<label for="id"></label> <input id="ID" name="id" placeholder="ID"
				required readonly="readonly" value="<%out.println(request.getAttribute("id")); %>" />
		</div>
		<div class="input__container">
			<label for="titulo"></label> <input id="titulo" name="titulo"
				placeholder="Título" value="<%out.println(request.getAttribute("titulo")); %>" required />
		</div>
		<div class="input__container">
			<label for="autor"></label> <input id="autor" name="autor" placeholder="Autor" value="<%out.println(request.getAttribute("autor")); %>" required />
		</div>
		<div class="input__container">
			<label for="editora"></label> <input id="editora" name="editora"
				placeholder="Editora" value="<%out.println(request.getAttribute("editora")); %>" required />
		</div>
		<button type="submit">Salvar</button>
	</form>
</body>
</html>