function logIn(){
	const userLogin = document.getElementById('login').value;
	const userPassword = document.getElementById('password').value;
	alert(userLogin + userPassword);
	const  response = await fetch("https://myapp-12344.herokuapp.com/api/auth", {
			method: "POST",
			body: JSON.stringify({ login: userLogin, password: userPassword }),
			headers: {
	 	     'Content-Type': 'application/json'
	  	  	}  	  	
	    });
		return await response.json();
}