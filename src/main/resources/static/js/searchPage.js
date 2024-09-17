document.querySelectorAll('.dropdown-menu').forEach(function (dropdown) {
	dropdown.addEventListener('click', function (e) {
		e.stopPropagation(); // Prevent the dropdown from closing
	});
});