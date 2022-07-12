package com.financemanager.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.financemanager.entity.Category;
import com.financemanager.entity.User;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest(showSql = true)
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Test
    void contextLoads() {
      assertNotNull(entityManager);
    }
    
    @Test
    void verifyBootstrappingByPersistingCategory() {
        Category category = new Category();
        category.setName("Food");
        category.addUser(new User(1, "Yurii", "password", "jurok3x@gmai.com", null));
        assertNull(category.getId());
        entityManager.persist(category);
        assertNotNull(category.getId());
        assertEquals(category, categoryRepository.findById(2).get());
        assertEquals(1, categoryRepository.count());
        assertEquals(1, categoryRepository.findById(2).get().getUsers().size());
    }
    
    @Test
    void verifyRepositoryByPersistingCategory() {
        Category category = new Category();
        category.setName("Medicine");
        categoryRepository.save(category);
        assertEquals(category, categoryRepository.findById(1).get());
        assertEquals(1, categoryRepository.count());
        categoryRepository.deleteById(1);
        assertEquals(0, categoryRepository.count());
    }

}
