package com.financemanager.repository;

import static org.junit.jupiter.api.Assertions.*;

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
        User user = new User();
        user.setEmail("jurok3x@gmail.com");
        user.setName("Yurii");
        user.setPassword("metro090");
        user.setRole(Role.ADMIN);
        entityManager.persist(user);
        assertEquals(user, userRepository.findByEmail("jurok3x@gmail.com").get());
    }
    
    @Test
    void verifyRepositoryByPersistingUser() {
        User user = new User();
        user.setEmail("jurok3x@gmail.com");
        user.setName("Yurii");
        user.setPassword("metro090");
        user.setRole(Role.ADMIN);
        userRepository.save(user);
        assertEquals(1, userRepository.count());
        userRepository.deleteById(1);
        assertEquals(0, userRepository.count());
    }

}
