package com.financemanager.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.financemanager.entity.Role;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest(showSql = true)
public class RoleRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void contextLoads() {
      assertNotNull(entityManager);
    }
    
    @Test
    void verifyRepositoryByPersistingRole() {
        Role role = new Role(null, "ROLE_USER");
        entityManager.persist(role);
    }
}
