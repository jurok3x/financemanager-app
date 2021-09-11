function createUser() {
        var userName = document.getElementById("name").value;
        var userLogin = document.getElementById("login").value;
        var userPassword = document.getElementById("password").value;
        var userEmail = document.getElementById("email").value;
        var xmlhttp = new XMLHttpRequest();   // new HttpRequest instance
        xmlhttp.open("POST", "http://localhost:8083/users/save");
        xmlhttp.setRequestHeader("Content-Type", "application/json");
        xmlhttp.send(JSON.stringify({name: userName, login: userLogin, password: userPassword, email:userEmail}));
    }