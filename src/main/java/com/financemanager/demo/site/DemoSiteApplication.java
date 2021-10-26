package com.financemanager.demo.site;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoSiteApplication {
	//FIXME Додати змогу виходу
	//TODO В контролері параметри ід юзера замість токена + перевірка
	//TODO Перевірити чи прострочений токен
	//TODO Додати функціонал кнопок ВИДАЛИТИ і ДОДАТИ
	//TODO Додати верхнє вспливаюче меню
	//TODO Додати фільтр на методи категорій юзерів і ролей для адміна
	//TODO Реєстрація юзера
	
	public static void main(String[] args) {
		SpringApplication.run(DemoSiteApplication.class, args);
	}

}