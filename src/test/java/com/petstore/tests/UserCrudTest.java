package com.petstore.tests;

import com.petstore.api.models.User;
import com.petstore.utils.TestDataGenerator;
import com.petstore.utils.ResponseValidator;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.Assert;
import io.qameta.allure.*;

@Epic("Pet Store API")
@Feature("User Management")
public class UserCrudTest extends BaseTest {

    // ========== POSITIVE SCENARIOS ==========

    @Test(priority = 1, description = "Create new user with valid data")
    @Story("Create User")
    @Severity(SeverityLevel.CRITICAL)
    public void testCreateUserPositive() {
        // Arrange
        User newUser = TestDataGenerator.createRandomUser();

        // Act
        Response response = userEndpoint.createUser(newUser);

        // Assert
        ResponseValidator.validateStatusCode(response, 200);
        ResponseValidator.validateFieldValue(response, "code", 200);
        ResponseValidator.validateFieldValue(response, "type", "unknown");

        // Cleanup
        userEndpoint.deleteUser(newUser.getUsername());
    }

    @Test(priority = 2, description = "Get user by username")
    @Story("Get User")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetUserByUsernamePositive() {
        // Arrange
        User newUser = TestDataGenerator.createRandomUser();
        userEndpoint.createUser(newUser);

        // Act
        Response response = userEndpoint.getUserByUsername(newUser.getUsername());

        // Assert
        ResponseValidator.validateStatusCode(response, 200);
        User retrievedUser = response.as(User.class);

        Assert.assertEquals(retrievedUser.getUsername(), newUser.getUsername());
        Assert.assertEquals(retrievedUser.getEmail(), newUser.getEmail());

        // Cleanup
        userEndpoint.deleteUser(newUser.getUsername());
    }

    @Test(priority = 3, description = "Update existing user")
    @Story("Update User")
    @Severity(SeverityLevel.CRITICAL)
    public void testUpdateUserPositive() {
        // Arrange
        User newUser = TestDataGenerator.createRandomUser();
        userEndpoint.createUser(newUser);

        newUser.setEmail("updated@test.com");
        newUser.setFirstName("UpdatedName");

        // Act
        Response response = userEndpoint.updateUser(newUser.getUsername(), newUser);

        // Assert
        ResponseValidator.validateStatusCode(response, 200);

        // Verify update
        Response getResponse = userEndpoint.getUserByUsername(newUser.getUsername());
        User updatedUser = getResponse.as(User.class);

        Assert.assertEquals(updatedUser.getEmail(), "updated@test.com");
        Assert.assertEquals(updatedUser.getFirstName(), "UpdatedName");

        // Cleanup
        userEndpoint.deleteUser(newUser.getUsername());
    }

    @Test(priority = 4, description = "Delete existing user")
    @Story("Delete User")
    @Severity(SeverityLevel.CRITICAL)
    public void testDeleteUserPositive() {
        // Arrange
        User newUser = TestDataGenerator.createRandomUser();
        userEndpoint.createUser(newUser);

        // Act
        Response response = userEndpoint.deleteUser(newUser.getUsername());

        // Assert
        ResponseValidator.validateStatusCode(response, 200);

        // Verify deletion
        Response getResponse = userEndpoint.getUserByUsername(newUser.getUsername());
        ResponseValidator.validateStatusCode(getResponse, 404);
    }

    @Test(priority = 5, description = "User login with valid credentials")
    @Story("User Login")
    @Severity(SeverityLevel.CRITICAL)
    public void testUserLoginPositive() {
        // Arrange
        User newUser = TestDataGenerator.createRandomUser();
        userEndpoint.createUser(newUser);

        // Act
        Response response = userEndpoint.loginUser(newUser.getUsername(), newUser.getPassword());

        // Assert
        ResponseValidator.validateStatusCode(response, 200);
        ResponseValidator.validateFieldExists(response, "message");

        String message = response.jsonPath().getString("message");
        Assert.assertTrue(message.contains("logged in user session"));

        // Cleanup
        userEndpoint.deleteUser(newUser.getUsername());
    }

    @Test(priority = 6, description = "User logout")
    @Story("User Logout")
    @Severity(SeverityLevel.NORMAL)
    public void testUserLogoutPositive() {
        // Act
        Response response = userEndpoint.logoutUser();

        // Assert
        ResponseValidator.validateStatusCode(response, 200);
    }

    // ========== NEGATIVE SCENARIOS ==========

    @Test(priority = 7, description = "Get non-existent user")
    @Story("Get User - Negative")
    @Severity(SeverityLevel.NORMAL)
    public void testGetNonExistentUser() {
        // Act
        Response response = userEndpoint.getUserByUsername("nonexistentuser999");

        // Assert
        ResponseValidator.validateStatusCode(response, 404);
        ResponseValidator.validateFieldValue(response, "message", "User not found");
    }

    @Test(priority = 8, description = "Create user with invalid data")
    @Story("Create User - Negative")
    @Severity(SeverityLevel.NORMAL)
    public void testCreateUserWithInvalidData() {
        // Arrange
        User invalidUser = User.builder()
                .username("") // Empty username
                .email("invalid-email") // Invalid email format
                .build();

        // Act
        Response response = userEndpoint.createUser(invalidUser);

        // Assert
        ResponseValidator.validateStatusCode(response, 400);
    }

    @Test(priority = 9, description = "Update non-existent user")
    @Story("Update User - Negative")
    @Severity(SeverityLevel.NORMAL)
    public void testUpdateNonExistentUser() {
        // Arrange
        User user = TestDataGenerator.createRandomUser();

        // Act
        Response response = userEndpoint.updateUser("nonexistentuser999", user);

        // Assert
        ResponseValidator.validateStatusCode(response, 404);
    }

    @Test(priority = 10, description = "Delete non-existent user")
    @Story("Delete User - Negative")
    @Severity(SeverityLevel.NORMAL)
    public void testDeleteNonExistentUser() {
        // Act
        Response response = userEndpoint.deleteUser("nonexistentuser999");

        // Assert
        ResponseValidator.validateStatusCode(response, 404);
    }

    @Test(priority = 11, description = "Login with invalid credentials")
    @Story("User Login - Negative")
    @Severity(SeverityLevel.NORMAL)
    public void testLoginWithInvalidCredentials() {
        // Act
        Response response = userEndpoint.loginUser("invaliduser", "wrongpassword");

        // Assert
        ResponseValidator.validateStatusCode(response, 400);
    }
}
