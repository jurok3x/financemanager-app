const monthNames = ["Січень", "Лютий", "Березень", "Квітень", "Травень", "Червень",
	"Липень", "Серпень", "Вересень", "Жовтень", "Листопад", "Грудень"];
//initialize the site: fetch categories, display items for current month, and define all the years in a list
let siteCore = siteNavigator(); //site
daysList();
displayItems();
mostPopularItems(0);
drawCategoryDoughnut();
drawMonthChart();

function siteNavigator(){
	let d = new Date();
	let month = d.getMonth();
	let year = d.getFullYear();
	const doughnutCtx = document.getElementById('categoryStatistic').getContext('2d');
	const categoryDoughnut = new Chart(doughnutCtx, {
	    type: 'doughnut',
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
	const monthCtx = document.getElementById('monthStatistic').getContext('2d');
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
	
	(async() => {
		let response = await fetch("http://localhost:8083/api/items/years", {
		method: "GET"})
		let years = await response.json();
		let html = '<option value="">Весь час</option>';
		years.forEach(year =>{
			html += '<option value="' + year + '">' + year + '</option>'
		})
			document.getElementById("yearsList").innerHTML = html;
	})();
	return {
		getMonth: () => month,
		getYear: () => year,
		getCategoryDoughnut: () => categoryDoughnut,
		getMonthChart: () => monthChart,
		monthBack: () => {
			month--;
			if (month < 0) {
				year--;
				month = 11;
			}
			if (month != d.getMonth() && year != d.getYear()) {
				if(document.getElementById("forward").className != "move"){
					document.getElementById("forward").className = "move";
				}
			}
			displayItems();
			daysList();
				},
		monthForward: () => {
			if (month != d.getMonth() || year != d.getFullYear()) {
				month++;
				if(month == d.getMonth() && year == d.getFullYear()){
					document.getElementById("forward").className = "disabled";
				}
					if (month > 11) {
						year++;
						month = 0;
				} 		
			displayItems();
			daysList();	
			}
		}
	}
}

async function deleteItem(itemId) {
	const response = await fetch("http://localhost:8083/api/items/" + itemId, {
		method: "DELETE"	 	  	
    });
	displayItems();
}

function daysList() {
	let y = siteCore.getYear();
	let m = siteCore.getMonth() + 1;
	let daysInMonth = /8|3|5|10/.test(--m)?30:m==1?(!(y%4)&&y%100)||!(y%400)?29:28:31;
	let html = '<option value="">День:</option>';
	for (let i = 1; i <= daysInMonth; i++) {
		html = html + '<option value="' + i + '">' + i + '</option>';
	}
	document.getElementById("days").innerHTML = html;
};

async function addItem() {
	// fetch all entries from the form and check for null
	let itemName = document.getElementById("item_name").value;
	if (!itemName) {
  	    alert("Введіть назву");
	    return false;
 	 }
	let itemPrice = document.getElementById("price").value;
	if(isNaN(itemPrice) || !itemPrice){
		alert("Не коректна ціна!");
		return false;
	}	
	if(!document.getElementById("days").value){
		alert("Введіть день!");
		return false;
	}
	let itemDate = new Date(siteCore.getYear(), siteCore.getMonth(),
	document.getElementById("days").value);
	if(!document.getElementById("categories").value){
		alert("Виберіть категорію!");
		return false;
	}
	let itemCategory = await getCategoryById(document.getElementById("categories").value);
	const  response = await fetch("http://localhost:8083/api/items/", {
		method: "POST",
		body: JSON.stringify({ name: itemName, price: itemPrice, category: itemCategory, date: itemDate }),
		headers: {
 	     'Content-Type': 'application/json'
  	  	}  	  	
    });
	document.getElementById('decor').style.display = 'none';
	displayItems();
	return await response.json();
}

async function mostPopularItems(catId){	
	let url = "http://localhost:8083/api/items/popular" + ((catId) ? ('?categoryId=' + catId) : '');
	const response = await fetch(url , {
		method: "GET"});
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
	const itemsUrl = "http://localhost:8083/api/items" + "?year=" + siteCore.getYear() +
			 "&month=" + (siteCore.getMonth() + 1);
	const itemResponse = await fetch(itemsUrl, {
		method: "GET"});
	const responseJson = await itemResponse.json();
	let itemHtml ='';
	let totalPrice = "Витрат немає.";
	if(Object.keys(responseJson)[0] == "_embedded"){
		let items = responseJson._embedded.itemModelList;
		items.forEach((item, index) => {
			console.log(item);
			itemHtml += '<tr><td>' + (index + 1) + '</td>\n' +
			'        <td>' + item.name + '</td>\n' +
			'        <td>' + item.price + '</td>\n' +
			'        <td>' + item.category.name + '</td>' +
			'        <td>' + new Date(item.date).getDate() + '</td>' +
			'        <td><button onclick="deleteItem(' + item.id + ')">Видалити</button></td></tr>';
			});
		totalPrice = "Загальні витрати за місяць: " + 
			items.reduce((value, item) => value + item.price, 0).toFixed(2) + "грн";
	}
	//display category count
	const categoryUrl = "http://localhost:8083/api/categories/count" + "?year=" + siteCore.getYear() +
			 "&month=" + (siteCore.getMonth() + 1);
	const categoriesResponce = await fetch(categoryUrl, {
		method: "GET"});
	const categoryAndCount = await categoriesResponce.json();
	let categoryTable = '';
	categoryAndCount.forEach(entry => {
		categoryTable += '      <tr><td>' + entry.category_Name + '</td>' +
		'	 <td>' + entry.value +
		'</td></tr>';
		}
	);	
	
	//sort items	
	let resort = true;
    $("#itemsList").trigger("update", [resort]);
	
	// add html
	document.getElementById("total_price").innerHTML = totalPrice;
	document.getElementById("itemsList").getElementsByTagName("tbody")[0].innerHTML = itemHtml;
	document.getElementById("categoryList").getElementsByTagName("tbody")[0].innerHTML = categoryTable;
	document.getElementById("current_date").innerHTML = monthNames[siteCore.getMonth()] +
	 ' ' + siteCore.getYear();
				
}

async function getCategoryById(catId){
	let response = await fetch("http://localhost:8083/api/categories/" + catId, {
		method: "GET"});
	return await response.json();
}

async function drawCategoryDoughnut(){
	let year = document.getElementById("yearsList").value;
	let url = "http://localhost:8083/api/categories/cost?year=" + year;
	if(!year){
		url = "http://localhost:8083/api/categories/cost";
	}
	const data = await getData(url);
	const categoryDoughnut = siteCore.getCategoryDoughnut();
	categoryDoughnut.data.labels = data.xLabels;
	categoryDoughnut.data.datasets[0].data = data.yLabels;
	categoryDoughnut.update();
}

async function drawMonthChart(){
	const year = document.getElementById("yearsList").value;
	let url = "http://localhost:8083/api/items/statistics";
	if(year){
		url += '?year=' + year;
	}
	const data = await getData(url);
	const monthChart = siteCore.getMonthChart();
	monthChart.data.labels = data.yLabels.map(entry =>{
		return monthNames[entry - 1].substr(0, 3) + '.';
	});
	monthChart.data.datasets[1].data = data.xLabels;
	monthChart.update();
}

async function drawCategoryBar(){
	const year = document.getElementById("yearsList").value;
	const catId = document.getElementById("categoryYearList").value;
	let url = "http://localhost:8083/api/items/statistics";
	if(year){
		url += '?year=' + year;
	}
	if(catId){
		url += ((url.indexOf("year") === -1) ? '?' : '&') + 'categoryId=' + catId;
	}
	const data = await getData(url);
	const monthChart = siteCore.getMonthChart();
	monthChart.data.datasets[0].data = data.xLabels;
	monthChart.update();
}

async function getData(url){
	const response = await fetch(url, {
		method: "GET"});
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




$(function() {
  $("#itemsList").tablesorter();
});