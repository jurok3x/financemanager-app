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
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void contextLoads() {
      assertNotNull(entityManager);
    }

    @Test
    void whenFindUserByEmail_thenReturnUser() {
        User user = prepareUser();
        entityManager.persist(user);
        assertEquals(user, userRepository.findByEmail(user.getEmail()).get());
    }
    
    @Test
    void verifyRepositoryByPersistingUser() {
        User user = prepareUser();
        userRepository.save(user);
        assertEquals(1, userRepository.count());
        userRepository.deleteById(user.getId());
        assertEquals(0, userRepository.count());
    }
    
    @Test
    void findCategoriesTest() {
        User user = prepareUser();
        Category category = prepareCategory("Food");
        entityManager.persist(user);
        entityManager.persist(category);
        assertEquals(0, userRepository.findByCategoryId(1).size());
        user.addCategory(category);
        assertEquals(1, userRepository.findByCategoryId(category.getId()).size());
        user.removeCategory(category);
        assertEquals(0, userRepository.findByCategoryId(category.getId()).size());
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
