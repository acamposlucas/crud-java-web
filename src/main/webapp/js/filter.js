/**
 * 
 */
 
 const filterForm = document.forms["filterForm"];
 
 filterForm.addEventListener("change", () => {
	window.location.href = `filter?autor=${filterForm.elements["listaAutores"].value}`;
})