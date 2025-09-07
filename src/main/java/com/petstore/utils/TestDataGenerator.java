package com.petstore.utils;

import com.petstore.api.models.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class TestDataGenerator {
    private static final Random random = new Random();
    private static final String[] PET_NAMES = {"Max", "Bella", "Charlie", "Lucy", "Cooper", "Luna"};
    private static final String[] CATEGORIES = {"Dogs", "Cats", "Birds", "Fish", "Rabbits"};

    public static Pet createRandomPet() {
        return Pet.builder()
                .id(generateRandomId())
                .name(getRandomPetName())
                .category(createRandomCategory())
                .photoUrls(Arrays.asList("http://example.com/photo1.jpg", "http://example.com/photo2.jpg"))
                .tags(Arrays.asList(createRandomTag()))
                .status(Pet.Status.AVAILABLE.getValue())
                .build();
    }

    public static User createRandomUser() {
        String username = "user" + generateRandomId();
        return User.builder()
                .id(generateRandomId())
                .username(username)
                .firstName("Test")
                .lastName("User")
                .email(username + "@test.com")
                .password("password123")
                .phone("1234567890")
                .userStatus(1)
                .build();
    }

    public static Order createRandomOrder() {
        return Order.builder()
                .id(generateRandomId())
                .petId(generateRandomId())
                .quantity(random.nextInt(5) + 1)
                .shipDate(new Date().toString())
                .status("placed")
                .complete(false)
                .build();
    }

    private static Category createRandomCategory() {
        return Category.builder()
                .id(generateRandomId())
                .name(CATEGORIES[random.nextInt(CATEGORIES.length)])
                .build();
    }

    private static Tag createRandomTag() {
        return Tag.builder()
                .id(generateRandomId())
                .name("tag" + random.nextInt(100))
                .build();
    }

    private static String getRandomPetName() {
        return PET_NAMES[random.nextInt(PET_NAMES.length)];
    }

    private static Long generateRandomId() {
        return ThreadLocalRandom.current().nextLong(100000, 999999);
    }

    public static Pet createInvalidPet() {
        return Pet.builder()
                .id(-1L) // Invalid ID
                .name("") // Empty name
                .photoUrls(null) // Null required field
                .status("invalid_status") // Invalid status
                .build();
    }
}

