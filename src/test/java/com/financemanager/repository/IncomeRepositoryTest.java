package com.financemanager.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.financemanager.entity.Income;
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
import java.util.Arrays;
import java.util.Date;

@DataJpaTest
class IncomeRepositoryTest {

    @Autowired
    private IncomeRepository incomeRepository;
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Test
    void contextLoads() {
      assertNotNull(entityManager);
    }
     
    @Test
    void verifyRepositoryByPersistingIncome() {
        User user = prepareUser();
        Income income = prepareIncome(230, user);
        assertNull(income.getId());
        entityManager.persist(user);
        entityManager.persist(income);
        assertNotNull(income.getId());
        assertEquals(230, incomeRepository.findById(income.getId()).get().getAmount());
        assertEquals(1, incomeRepository.count());
        assertEquals(Arrays.asList(income), incomeRepository.findAll());
        incomeRepository.deleteById(income.getId());
        assertEquals(0, incomeRepository.count());
    }
    
    @Test
    void verifyBootstrappingByPersistingIncome() {
        User user = prepareUser();
        Income income = prepareIncome(220, user);
        assertNull(income.getId());
        entityManager.persist(user);
        entityManager.persist(income);
        assertNotNull(income.getId());
        assertEquals(income, incomeRepository.findById(income.getId()).get());
        assertEquals(220, incomeRepository.findById(income.getId()).get().getAmount());
    }
    
    @Test
    void findIncomesTest() {
        User user = prepareUser();
        entityManager.persist(user);
        for(int i=0; i<10; i++) {
            entityManager.persist(prepareIncome(i * 100, user));
        }
        assertEquals(10, incomeRepository.count());
        DatePart datePart = new DatePart();
        assertEquals(10, incomeRepository.findByUserIdAndDatePart(user.getId(), datePart).size());
        datePart.setMonth(12);
        assertEquals(10, incomeRepository.findByUserIdAndDatePart(user.getId(), datePart).size());
        datePart.setYear(2022);
        assertEquals(10, incomeRepository.findByUserIdAndDatePart(user.getId(), datePart).size());
        assertEquals(5, incomeRepository.findByUserIdAndDatePart(user.getId(), datePart, PageRequest.of(1, 5)).getSize());
        incomeRepository.deleteById(5L);// TODO: fix this
        assertEquals(4, incomeRepository.findByUserIdAndDatePart(user.getId(), datePart, PageRequest.of(1, 5)).getSize());
    }
    
    private Income prepareIncome(double amount, User user) {
        Income income = new Income();
        income.setAmount(amount);
        LocalDate date = LocalDate.of(2022, 12, 21);
        income.setDate(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        income.setName("Salary");
        income.setUser(user);
        return income; 
    }
    
    private User prepareUser() {
        return new User(null, "Yurii", "metro", "jurok3x@gmail.com", Role.ADMIN);
    }
    
}
