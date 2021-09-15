function createUser() {
        let userName = document.getElementById("name").value;
        let userLogin = document.getElementById("login").value;
        let userPassword = document.getElementById("password").value;
        let userEmail = document.getElementById("email").value;
        const  response = await fetch("https://myapp-12344.herokuapp.com/api/users", {
			method:
				"POST",
			body:
				JSON.stringify({name: userName, login: userLogin, password: userPassword, email:userEmail}),
			headers: {
 	  	  		'Content-Type': 'application/json'
  		  	}  	  	
  		  });
  		return await response.json();
    }