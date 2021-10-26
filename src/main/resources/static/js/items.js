const monthNames = ["Січень", "Лютий", "Березень", "Квітень", "Травень", "Червень",
	"Липень", "Серпень", "Вересень", "Жовтень", "Листопад", "Грудень"];
//initialize the site: fetch categories, display items for current month, and define all the years in a list
let siteCore = siteNavigator(); //site

async function init (){
	document.getElementById('login-form').style.display = 'none';
	document.getElementById('login-text').style.display = 'none';
	//initialize the date
	await getUserYearsList();
	daysList();
	//display all items, draw the bars
	await displayItems();
	await mostPopularItems(0);
	await drawCategoryDoughnut();
	drawMonthChart();
	
}

if(localStorage.getItem('SavedToken')){
	init();
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

function daysList() {
	let y = document.getElementById("year").value;
	let m = document.getElementById("month").value;
	let daysInMonth = /8|3|5|10/.test(--m)?30:m==1?(!(y%4)&&y%100)||!(y%400)?29:28:31;
	let html = '<option value="">День:</option>';
	for (let i = 1; i <= daysInMonth; i++) {
		html = html + '<option value="' + i + '">' + i + '</option>';
	}
	document.getElementById("item-day").innerHTML = html;
};

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

async function addItem() {
	
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
	if(!document.getElementById('item-day').value){
		alert("Введіть день!");
		return false;
	}
	const itemDate = new Date(Date.UTC(document.getElementById('year').value, document.getElementById('month').value - 1,
	document.getElementById('item-day').value));
	if(!document.getElementById('item-category').value){
		alert("Виберіть категорію!");
		return false;
	}
	const itemCategory = await getCategoryById(document.getElementById("item-category").value);
	const  response = await fetch('https://myapp-12344.herokuapp.com/api/items', {
		method: "POST",
		body: JSON.stringify({ name: itemName, price: itemPrice, category: itemCategory, date: itemDate }),
		headers: { 'Content-Type' : 'application/json', 'authorization' : localStorage.getItem('SavedToken') } 	  	
    });
	document.getElementById('itemForm').style.display = 'none';
	displayItems();
}

async function mostPopularItems(catId){	
	let url = new URL('https://myapp-12344.herokuapp.com/api/items/popular');
	if(document.getElementById('year').value){
		url.searchParams.append('year', document.getElementById('year').value);
	}
	if(document.getElementById('month').value){
		url.searchParams.append('month', document.getElementById('month').value);
	}
	if(catId){
		url.searchParams.append('categoryId', catId);
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
			'        <td><button onclick="deleteItem(' + item.id + ')">Видалити</button></td></tr>';
			});
			
		totalPrice = '<strong>Загальні витрати за період: ' + 
			items.reduce((value, item) => value + item.price, 0).toFixed(2) + 'грн</strong>';
	}
	//display category count
	let categoryUrl =  new URL('https://myapp-12344.herokuapp.com/api/categories/count');
	categoryUrl.searchParams = itemsUrl.searchParams;
	const categoriesResponce = await fetch(categoryUrl, {
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
    $("#item-list").tablesorter()
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
	if(year){
		url.searchParams.append('year', year);
	}
	if(catId){
		url.searchParams.append('categoryId', catId);
	}
	const data = await getData(url);
	const monthChart = siteCore.getMonthChart();
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
	let objKeys = Object.keys(chartData[0]);
	chartData.forEach(entry =>{
		xLabels.push(entry[objKeys[0]]);
		yLabels.push(entry[objKeys[1]]);
	})	
	return {xLabels, yLabels}
}

async function logIn(){
	const userLogin = document.getElementById('login').value;
	const userPassword = document.getElementById('password').value;
	
	const  response = await fetch("https://myapp-12344.herokuapp.com/api/auth", {
			method: "POST",
			body: JSON.stringify({ login: userLogin, password: userPassword }),
			headers: {
	 	     'Content-Type': 'application/json'
	  	  	}  	  	
	    });
	 const responseHeader = response.headers.get('authorization');
	 localStorage.setItem('SavedToken', responseHeader);
	 init();
}

