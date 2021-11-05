const monthNames = ["Січень", "Лютий", "Березень", "Квітень", "Травень", "Червень",
	"Липень", "Серпень", "Вересень", "Жовтень", "Листопад", "Грудень"];
//initialize the site: fetch categories, display items for current month, and define all the years in a list
let siteCore = siteNavigator(); //site
init();

async function init (){
	if(localStorage.getItem('SavedToken')){
		document.querySelector('#login-button-group').classList.toggle('hidden');
		document.querySelector('#logout-button').classList.toggle('hidden');
		document.getElementById('username').innerText = localStorage.getItem('userName') + '!';
		//initialize the date
		await getUserYearsList();
		//display all items, draw the bars
		await displayItems();
		await drawCategoryDoughnut();
		drawMonthChart();
	}
}

function siteNavigator(){
	let siteHedders = new Headers();
	const doughnutCtx = document.getElementById('statistic-category').getContext('2d');
	const categoryDoughnut = new Chart(doughnutCtx, {
	    type: 'doughnut',
	    options: {
	     	plugins: {
	     		legend:{
	     			position: 'right'
	     		}
	     	}
	     },
	    data: {
	        datasets: [{
	            label: 'Витрати',
	            backgroundColor: [
	                'rgba(255, 99, 132, 1)',
	                'rgba(54, 162, 235, 1)',
	                'rgba(255, 206, 86, 1)',
	                'rgba(75, 192, 192, 1)',
	                'rgba(153, 102, 255, 1)',
	                'rgba(255, 159, 64, 1)',
					'rgba(107, 142, 35, 1)',
					'rgba(0, 255, 255, 1)',
					'rgba(139, 0, 139, 1)',
					'rgba(183, 183, 183, 1)'
	            ],
			hoverOffset: 4    
	        }]
	    },
	});
	const monthCtx = document.getElementById('statistics-month').getContext('2d');
	const monthChart = new Chart(monthCtx, {
    data: {
        datasets: [{
            type: 'bar',
		    label: 'Витрати по категоріям',
            backgroundColor: 
                'rgba(255, 99, 132, 0.2)',
            borderColor: 
                'rgba(255, 99, 132, 1)',
            borderWidth: 1,
		    order: 2
        }, {
			label: 'Загальні витрати',
            type: 'line',
			borderColor: 'rgb(75, 192, 192)',
			backgroundColor: 'rgba(75, 192, 192, 0.2)',
			fill: true,
            order: 1
	}]
    },
    options: {
        scales: {
            y: {
                beginAtZero: true
            }
        }
    }
});
	return {
		getCategoryDoughnut: () => categoryDoughnut,
		getMonthChart: () => monthChart
		}
	}

async function deleteItem(itemId) {
	const response = await fetch("https://myapp-12344.herokuapp.com/api/items/" + itemId, {
		method: "DELETE",
		headers: { 'authorization' : localStorage.getItem('SavedToken') }	 	  	
    });
	displayItems();
}

async function getUserYearsList() {
		const response = await fetch("https://myapp-12344.herokuapp.com/api/items/years", {
		method: "GET",
		headers: { 'authorization' : localStorage.getItem('SavedToken') }
		})
		const years = await response.json();
		let html = '<option value="">Весь період</option>';
		years.forEach(year =>{
			html += '<option value="' + year + '">' + year + '</option>'
		})
		document.getElementById("year-chart-select").innerHTML = html;
		document.getElementById("year").innerHTML = html;
		const dateNow = new Date();
		document.getElementById('month').value = dateNow.getMonth() + 1;
		document.getElementById("year").value = dateNow.getFullYear();
	};

async function addItem(url, method) {
	console.log('item changed');
	// fetch all entries from the form and check for null
	const itemName = document.getElementById("item-name").value;
	if (!itemName) {
  	    alert("Введіть назву");
	    return false;
 	 }
	const itemPrice = document.getElementById("item-price").value;
	if(isNaN(itemPrice) || !itemPrice){
		alert("Не коректна ціна!");
		return false;
	}	
	if(!document.getElementById('item-date').value){
		alert("Введіть дату!");
		return false;
	}
	const itemDate = document.getElementById('item-date').value;
	if(!document.getElementById('item-category').value){
		alert("Виберіть категорію!");
		return false;
	}
	const itemCategory = await getCategoryById(document.getElementById("item-category").value);
	const  response = await fetch(url, {
		method: method,
		body: JSON.stringify({ name: itemName, price: itemPrice, category: itemCategory, date: itemDate }),
		headers: { 'Content-Type' : 'application/json', 'authorization' : localStorage.getItem('SavedToken') } 	  	
    });
	closeForm();
	displayItems();
}

async function mostPopularItems(){	
	let url = new URL('https://myapp-12344.herokuapp.com/api/items/popular');
	if(document.getElementById('year').value){
		url.searchParams.append('year', document.getElementById('year').value);
	}
	if(document.getElementById('month').value){
		url.searchParams.append('month', document.getElementById('month').value);
	}
	if(document.getElementById('category').value){
		url.searchParams.append('categoryId', document.getElementById('category').value);
	}
	const response = await fetch(url.href , {
		method: "GET",
		headers: { 'authorization' : localStorage.getItem('SavedToken') }});
	let items = await response.json();
	let html = '';
	for(let i = 0; i < items.length; i++){
		let item = items[i];
		html = html + '<tr><th scope="row">' + (i + 1) + '</th>\n' +
		'        <td>' + item.item_Name + '</td>\n' +
		'        <td>' + item.value + '</td>\n' +
		'        <td>' + item.total.toFixed(2) + '</td></tr>\n' +
		'    </tr>';
	}	
	document.getElementById("mostPopular").getElementsByTagName("tbody")[0].innerHTML = html;	
}

async function openForm(itemId){
	let myForm = document.getElementById('add-item');
	myForm.style.display = 'block';
	if(document.getElementById("year").value){
		let date = new Date(Date.UTC(document.getElementById("year").value));
		if(document.getElementById("month").value){
			date.setUTCMonth(document.getElementById("month").value - 1)
		}
		myForm.getElementsByTagName('input')[2].value = date.toISOString().slice(0, 10);
	}
	if(itemId){
		const response = await fetch("https://myapp-12344.herokuapp.com/api/items/" + itemId, {
			method: "GET",
			headers: { 'authorization' : localStorage.getItem('SavedToken') }});
		const item =  await response.json();
		myForm.getElementsByTagName('h3')[0].innerHTML = 'Змінити елемент';
		myForm.getElementsByTagName('input')[0].value = item.name;
		myForm.getElementsByTagName('input')[1].value = item.price;
		myForm.getElementsByTagName('input')[3].value = 'Змінити';
		myForm.getElementsByTagName('select')[0].value = item.category.id;
		myForm.getElementsByTagName('input')[2].value = item.date.slice(0, 10);
		const editItem = () => addItem('https://myapp-12344.herokuapp.com/api/items/' + itemId, 'PUT');
		myForm.getElementsByTagName('input')[3].addEventListener('click', editItem);
		return false;
	}
	const addNewItem = () => addItem('https://myapp-12344.herokuapp.com/api/items/', 'POST');
	myForm.getElementsByTagName('input')[3].addEventListener('click', addNewItem);
}

function closeForm(){
	let myForm = document.getElementById('add-item');
	myForm.getElementsByTagName('input')[3].replaceWith(myForm.getElementsByTagName('input')[3].cloneNode(true));
	myForm.style.display = 'none'
	myForm.getElementsByTagName('h3')[0].innerHTML = 'Додати елемент';
	myForm.getElementsByTagName('input')[0].value = '';
	myForm.getElementsByTagName('input')[1].value = null;
	myForm.getElementsByTagName('input')[3].value = 'Додати';
	myForm.getElementsByTagName('select')[0].value = '';
	myForm.getElementsByTagName('input')[2].value = new Date().toISOString().slice(0, 10);
}

async function displayItems(){
	//display all items for current date
	let itemsUrl = new URL('https://myapp-12344.herokuapp.com/api/items');
	if(document.getElementById('category').value){
		itemsUrl.href += '/category/' + document.getElementById('category').value;
	}
	if(document.getElementById('year').value){
		itemsUrl.searchParams.append('year', document.getElementById('year').value);
	}
	if(document.getElementById('month').value){
		itemsUrl.searchParams.append('month', document.getElementById('month').value);
	}
	const itemResponse = await fetch(itemsUrl.href, {
		method: "GET",
		headers: { 'authorization' : localStorage.getItem('SavedToken') }});
	const responseJson = await itemResponse.json();
	let itemHtml ='';
	let totalPrice = "Витрат немає.";
	if(Object.keys(responseJson)[0] == "_embedded"){
		const items = responseJson._embedded.itemModelList;
		items.forEach((item, index) => {
			itemHtml += '<tr><th scope="row">' + (index + 1) + '</th>\n' +
			'        <td>' + item.name + '</td>\n' +
			'        <td>' + item.price + '</td>\n' +
			'        <td>' + item.category.name + '</td>' +
			'        <td>' + item.date.slice(0, 10) + '</td>' +
			'        <td><button type="button" class="btn btn-warning btn-sm" onclick="openForm(' + item.id + ')" title="Редагувати"> &#9998;</button>' + 
			'<button type="button" class="btn btn-danger btn-sm" onclick="deleteItem(' + item.id + ')" title="Видалити">&#10006;</button></td></tr>';
			
			});
			
		totalPrice = '<strong>Загальні витрати за період: ' + 
			items.reduce((value, item) => value + item.price, 0).toFixed(2) + 'грн</strong>';
	}
	//display category count
	let categoryUrl =  new URL('https://myapp-12344.herokuapp.com/api/categories/count');
	categoryUrl.search = itemsUrl.search;
	const categoriesResponce = await fetch(categoryUrl.href, {
		method: "GET",
		headers: { 'authorization' : localStorage.getItem('SavedToken') }});
	const categoryAndCount = await categoriesResponce.json();
	let categoryTable = '';
	categoryAndCount.forEach(entry => {
		categoryTable += '      <tr><td>' + entry.category_Name + '</td>' +
		'	 <td>' + entry.value +
		'</td></tr>';
		}
	);	
	
	
	// add html
	document.getElementById("total-price").innerHTML = totalPrice;
	document.getElementById("item-list").getElementsByTagName("tbody")[0].innerHTML = itemHtml;
	document.getElementById("category-count").getElementsByTagName("tbody")[0].innerHTML = categoryTable;	
	
	//sort items	
    $("#item-list").trigger("update");
    $("#item-list").tablesorter();
    
    mostPopularItems();
}

async function getCategoryById(catId){
	if(!catId){
		return false;
	}
	const response = await fetch("https://myapp-12344.herokuapp.com/api/categories/" + catId, {
		method: "GET",
		headers: { 'authorization' : localStorage.getItem('SavedToken') }});
	return await response.json();
}

async function drawCategoryDoughnut(){
	const year = document.getElementById("year").value;
	const month = document.getElementById("month").value;
	const infoText = 'Статистика за ' + ((year) ? year + ' рік' : 'весь період') +
	((month) ? ' місяць ' + monthNames[month - 1].toLowerCase()  : '') + '.';
	document.getElementById('info-category-doughnut').innerHTML = infoText;
	let url = new URL('https://myapp-12344.herokuapp.com/api/categories/cost');
	if(year){
		url.searchParams.append('year', year);
	}
	if(month){
		url.searchParams.append('month', month);
	}
	const data = await getData(url.href);
	if(!data){
		return false;
	}
	const categoryDoughnut = siteCore.getCategoryDoughnut();
	categoryDoughnut.data.labels = data.yLabels;
	categoryDoughnut.data.datasets[0].data = data.xLabels.map(entry => entry.toFixed(2));
	categoryDoughnut.update();
}

async function drawMonthChart(){
	const year = document.getElementById("year-chart-select").value;
	let url = new URL('https://myapp-12344.herokuapp.com/api/items/statistics');
	if(year){
		url.searchParams.append('year', year);
	}
	const data = await getData(url.href);
	if(!data){
		return false;
	}
	const monthChart = siteCore.getMonthChart();
	monthChart.data.labels = data.yLabels.map(entry =>{
		return monthNames[entry - 1].substr(0, 3) + '.';
	});
	monthChart.data.datasets[1].data = data.xLabels.map(entry => entry.toFixed(2));
	monthChart.update();
}

async function drawCategoryBar(){
	const year = document.getElementById("year-chart-select").value;
	const catId = document.getElementById("category-bar-select").value;
	let url = new URL('https://myapp-12344.herokuapp.com/api/items/statistics');
	const monthChart = siteCore.getMonthChart();
	if(!catId){
		monthChart.data.datasets[0].data = [];
		monthChart.update();
		return false;
	}
	url.searchParams.append('categoryId', catId);
	if(year){
		url.searchParams.append('year', year);
	}
	const data = await getData(url);
	if(!data){
		return false;
	}
	monthChart.data.datasets[0].data = data.xLabels.map(entry => entry.toFixed(2));
	monthChart.update();
}

async function getData(url){
	const response = await fetch(url, {
		method: "GET",
		headers: { 'authorization' : localStorage.getItem('SavedToken') }});
	let chartData = await response.json();
	let xLabels = [];
	let yLabels = [];
	if(!chartData.length){ 
		return false;
	}
	let objKeys = Object.keys(chartData[0]);
	chartData.forEach(entry =>{
		xLabels.push(entry[objKeys[0]]);
		yLabels.push(entry[objKeys[1]]);
	})	
	return {xLabels, yLabels}
}

async function logIn(){
	const userLogin = document.getElementById('login').value;
	const userPassword = document.getElementById('password').value
	const  response = await fetch("https://myapp-12344.herokuapp.com/api/auth", {
			method: "POST",
			body: JSON.stringify({ login: userLogin, password: userPassword }),
			headers: {
	 	     'Content-Type': 'application/json'
	  	  	}  	  	
	    });
	 const responseHeader = await response.headers.get('authorization');
	 const user = await response.json();
	 localStorage.setItem('SavedToken', responseHeader);
	 localStorage.setItem('userName', user.name);
	 document.getElementById('login-form').classList.toggle('hidden');
	 init();
}

function logOut(){
	localStorage.clear();
	document.querySelector('#login-button-group').classList.toggle('hidden');
	document.querySelector('#logout-button').classList.toggle('hidden');
	document.getElementById('username').innerText = 'Гість' + '!';
	location.reload();
}

