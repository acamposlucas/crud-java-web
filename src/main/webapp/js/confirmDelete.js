/**
 * Confirms if selected Book must be deleted	
 * @param id {number}
 */
 
 
 function confirmDelete(id) {
	let confirmation = confirm("Confirma a exclusão desse livro?");
	
	if (confirmation) {
		window.location.href = `delete?id=${id}`;
	}
}