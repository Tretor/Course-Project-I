function check(form) {
    if (form.username.value === "administrator" && form.password.value === "password") {
        document.cookie = "adminAccess=true";
        window.location = "admin.html";
    }
    else {
        alert("Wrong password!");
    }
}