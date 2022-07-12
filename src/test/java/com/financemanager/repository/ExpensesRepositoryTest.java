package com.financemanager.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.financemanager.entity.Category;
import com.financemanager.entity.Expense;
import com.financemanager.entity.Role;
import com.financemanager.entity.User;
import com.financemanager.entity.utils.DatePart;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@DataJpaTest
class ExpensesRepositoryTest {

    @Autowired
    private ExpensesRepository expensesRepository;
    
    @Autowired
    private TestEntityManager entityManager;
    
    
    @Test
    void contextLoads() {
      assertNotNull(entityManager);
    }
    
    @Test
    void findAllExpensesTest() {
        Category category = prepareCategory("Food");
        User user = prepareUser();
        Expense expense = prepareExspense(category, user);
        entityManager.persist(user);
        entityManager.persist(category);
        entityManager.persist(expense);
        assertNotNull(expense.getId());
        assertEquals(1, expensesRepository.count());
        DatePart datePart = new DatePart();
        int userId = 2;
        int categoryId = 2;
        assertEquals(1, expensesRepository.findByUserId(userId, 2, datePart).size());
        assertEquals(1, expensesRepository.findByUserId(userId, null, datePart).size());
        datePart.setMonth(12);
        assertEquals(1, expensesRepository.findByUserId(userId, categoryId, datePart).size());
        datePart.setYear(2022);
        assertEquals(1, expensesRepository.findByUserId(userId, categoryId, datePart).size());
    }
    
    @Test
    void findExpensePage() {
        Category category = prepareCategory("Food");
        User user = prepareUser();
        entityManager.persist(user);
        entityManager.persist(category);
        for(int i=0; i<10; i++) {
            entityManager.persist(prepareExspense(category, user));
        }
        assertEquals(10, expensesRepository.count());
        DatePart datePart = new DatePart();
        int userId = 1;
        int categoryId = 1;
        assertEquals(10, expensesRepository.findByUserId(userId, categoryId, datePart, PageRequest.of(0, 10)).getNumberOfElements());
        datePart.setMonth(12);
        assertEquals(10, expensesRepository.findByUserId(userId, categoryId, datePart, PageRequest.of(0, 10)).getNumberOfElements());
        datePart.setYear(2022);
        assertEquals(10, expensesRepository.findByUserId(userId, categoryId, datePart, PageRequest.of(0, 10)).getNumberOfElements());
        assertEquals(5, expensesRepository.findByUserId(userId, categoryId, datePart, PageRequest.of(1, 5)).getNumberOfElements());
        expensesRepository.deleteById(8L);
        assertEquals(4, expensesRepository.findByUserId(userId, categoryId, datePart, PageRequest.of(1, 5)).getNumberOfElements());
    }
    
    private Expense prepareExspense(Category category, User user) {
        user.addCategory(category);
        Expense expense = new Expense();
        expense.setName("Pizza");
        expense.setPrice(222);
        expense.setCategory(category);
        expense.setUser(user);
        LocalDate date = LocalDate.of(2022, 12, 21);
        expense.setDate(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        return expense;
    }
    
    private Category prepareCategory(String name) {
        Category category = new Category();
        category.setName(name);
        return category;
    }
    
    private User prepareUser() {
        return new User(null, "Yurii", "metro", "jurok3x@gmail.com", Role.ADMIN);
    }

}
