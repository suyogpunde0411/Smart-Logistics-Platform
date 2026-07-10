package com.smartlogistics.userservice.domain.repository;

import com.smartlogistics.userservice.domain.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserProfileRepositoryTest {

    @Autowired
    private UserProfileRepository userRepository;

    @Test
    void testSaveAndFindUser() {
        UserProfile user = UserProfile.builder()
                .email("repo@example.com")
                .phone("+919999988888")
                .firstName("Repository")
                .lastName("Test")
                .status("ACTIVE")
                .build();
        user.setId(UUID.randomUUID());

        UserProfile saved = userRepository.save(user);
        assertNotNull(saved.getId());

        Optional<UserProfile> found = userRepository.findByEmail("repo@example.com");
        assertTrue(found.isPresent());
        assertEquals("Repository", found.get().getFirstName());
    }

    @Test
    void testSearchUsers_byCity() {
        UserProfile user = UserProfile.builder()
                .email("city@example.com")
                .phone("+919999977777")
                .firstName("CitySearch")
                .lastName("Test")
                .status("ACTIVE")
                .build();
        user.setId(UUID.randomUUID());

        Address address = Address.builder()
                .line1("123 Street")
                .city("Pune")
                .state("Maharashtra")
                .zip("411001")
                .country("India")
                .type("HOME")
                .user(user)
                .build();
        user.getAddresses().add(address);

        userRepository.save(user);

        Page<UserProfile> result = userRepository.searchUsers("Pune", PageRequest.of(0, 10));
        assertEquals(1, result.getTotalElements());
        assertEquals("CitySearch", result.getContent().get(0).getFirstName());
    }
}
