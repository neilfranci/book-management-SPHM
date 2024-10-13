document.querySelectorAll(".dropdown-menu").forEach((dropdown) => {
  dropdown.addEventListener("click", (e) => {
    e.stopPropagation(); // Prevent the dropdown from closing
  });
});

document.addEventListener("DOMContentLoaded", function () {
  document.querySelectorAll(".dropdown-menu a").forEach((item) => {
    item.addEventListener("click", function (e) {
      e.preventDefault();
      // Update the searchBy hidden input
      const searchBy = e.target.getAttribute("data-search-by");
      document.getElementById("searchBy").value = searchBy;
      // Update the button text
      document.getElementById("searchByBtn").textContent = searchBy;
    });
  });
});
