package com.financemanager.demo.site;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoSiteApplication {
	//FIXME Виправити форму
	//FIXME Додати змогу виходу
	//TODO В контролері параметри імя юзера замість токена + перевірка
	//TODO Перевірити чи прострочений токен
	//TODO Змінити кнопки ВИДАЛИТИ і ЗМІНИТИ
	//TODO Додати текстове поле для статистики
	//TODO Додати фільтр категорій
	//TODO Додати фільтр на методи категорій юзерів і ролей для адміна
	//TODO Реєстрація юзера
	
	public static void main(String[] args) {
		SpringApplication.run(DemoSiteApplication.class, args);
	}

}