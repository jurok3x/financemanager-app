package com.financemanager.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.financemanager.entity.Category;
import com.financemanager.entity.Role;
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
        Category category = prepareCategory("Food");
        category.addUser(prepareUser());
        assertNull(category.getId());
        entityManager.persist(category);
        assertNotNull(category.getId());
        assertEquals(category, categoryRepository.findById(category.getId()).get());
        assertEquals(1, categoryRepository.count());
        assertEquals(1, categoryRepository.findById(category.getId()).get().getUsers().size());
    }
    
    @Test
    void verifyRepositoryByPersistingCategory() {
        Category category = prepareCategory("Medicine");
        categoryRepository.save(category);
        assertNotNull(category.getId());
        assertEquals(category, categoryRepository.findById(category.getId()).get());
        assertEquals(1, categoryRepository.count());
        categoryRepository.deleteById(category.getId());
        assertEquals(0, categoryRepository.count());
    }
    
    @Test
    void findByUserIdTest() {
        User user = prepareUser();
        Category category = prepareCategory("Medicine");
        entityManager.persist(user);
        entityManager.persist(category);
        assertEquals(0, categoryRepository.findByUserId(user.getId()).size());
        user.addCategory(category);
        assertEquals(1, categoryRepository.findByUserId(user.getId()).size());
        user.removeCategory(category);
        assertEquals(0, user.getCategories().size());
    }
    
    private User prepareUser() {
        User user = new User();
        Role role = new Role(null, "ROLE_USER");
        entityManager.persist(role);
        user.setEmail("jurok3x@gmail.com");
        user.setName("Yurii");
        user.setPassword("metro090");
        user.setRole(role);
        return user;
    }
    
    private Category prepareCategory(String name) {
        Category category = new Category();
        category.setName(name);
        return category;
    }

}
