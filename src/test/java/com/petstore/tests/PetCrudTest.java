package com.petstore.tests;

import com.petstore.api.models.Pet;
import com.petstore.utils.TestDataGenerator;
import com.petstore.utils.ResponseValidator;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.Assert;
import io.qameta.allure.*;

@Epic("Pet Store API")
@Feature("Pet Management")
public class PetCrudTest extends BaseTest {

    // ========== POSITIVE SCENARIOS ==========

    @Test(priority = 1, description = "Create a new pet with valid data")
    @Story("Create Pet")
    @Severity(SeverityLevel.CRITICAL)
    public void testCreatePetPositive() {
        // Arrange
        Pet newPet = TestDataGenerator.createRandomPet();

        // Act
        Response response = petEndpoint.createPet(newPet);

        // Assert
        ResponseValidator.validateStatusCode(response, 200);
        Pet createdPet = response.as(Pet.class);

        Assert.assertNotNull(createdPet.getId());
        Assert.assertEquals(createdPet.getName(), newPet.getName());
        Assert.assertEquals(createdPet.getStatus(), newPet.getStatus());

        // Cleanup
        cleanupTestData(createdPet.getId());
    }

    @Test(priority = 2, description = "Get pet by valid ID")
    @Story("Get Pet")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetPetByIdPositive() {
        // Arrange
        Pet newPet = TestDataGenerator.createRandomPet();
        Response createResponse = petEndpoint.createPet(newPet);
        Pet createdPet = createResponse.as(Pet.class);

        // Act
        Response response = petEndpoint.getPetById(createdPet.getId());

        // Assert
        ResponseValidator.validateStatusCode(response, 200);
        Pet retrievedPet = response.as(Pet.class);

        Assert.assertEquals(retrievedPet.getId(), createdPet.getId());
        Assert.assertEquals(retrievedPet.getName(), createdPet.getName());

        // Cleanup
        cleanupTestData(createdPet.getId());
    }

    @Test(priority = 3, description = "Update existing pet with valid data")
    @Story("Update Pet")
    @Severity(SeverityLevel.CRITICAL)
    public void testUpdatePetPositive() {
        // Arrange
        Pet newPet = TestDataGenerator.createRandomPet();
        Response createResponse = petEndpoint.createPet(newPet);
        Pet createdPet = createResponse.as(Pet.class);

        createdPet.setName("Updated Name");
        createdPet.setStatus(Pet.Status.SOLD.getValue());

        // Act
        Response response = petEndpoint.updatePet(createdPet);

        // Assert
        ResponseValidator.validateStatusCode(response, 200);
        Pet updatedPet = response.as(Pet.class);

        Assert.assertEquals(updatedPet.getName(), "Updated Name");
        Assert.assertEquals(updatedPet.getStatus(), Pet.Status.SOLD.getValue());

        // Cleanup
        cleanupTestData(createdPet.getId());
    }

    @Test(priority = 4, description = "Delete existing pet")
    @Story("Delete Pet")
    @Severity(SeverityLevel.CRITICAL)
    public void testDeletePetPositive() {
        // Arrange
        Pet newPet = TestDataGenerator.createRandomPet();
        Response createResponse = petEndpoint.createPet(newPet);
        Pet createdPet = createResponse.as(Pet.class);

        // Act
        Response response = petEndpoint.deletePet(createdPet.getId());

        // Assert
        ResponseValidator.validateStatusCode(response, 200);

        // Verify deletion
        Response getResponse = petEndpoint.getPetById(createdPet.getId());
        ResponseValidator.validateStatusCode(getResponse, 404);
    }

    @Test(priority = 5, description = "Find pets by status")
    @Story("Find Pets")
    @Severity(SeverityLevel.NORMAL)
    public void testFindPetsByStatusPositive() {
        // Act
        Response response = petEndpoint.getPetsByStatus("available");

        // Assert
        ResponseValidator.validateStatusCode(response, 200);
        Pet[] pets = response.as(Pet[].class);

        Assert.assertTrue(pets.length > 0, "No pets found with status 'available'");
        for (Pet pet : pets) {
            Assert.assertEquals(pet.getStatus(), "available");
        }
    }

    // ========== NEGATIVE SCENARIOS ==========

    @Test(priority = 6, description = "Create pet with invalid data")
    @Story("Create Pet - Negative")
    @Severity(SeverityLevel.NORMAL)
    public void testCreatePetNegative() {
        // Arrange
        Pet invalidPet = TestDataGenerator.createInvalidPet();

        // Act
        Response response = petEndpoint.createPet(invalidPet);

        // Assert
        ResponseValidator.validateStatusCode(response, 400);
    }

    @Test(priority = 7, description = "Get pet with non-existent ID")
    @Story("Get Pet - Negative")
    @Severity(SeverityLevel.NORMAL)
    public void testGetPetByIdNegative() {
        // Act
        Response response = petEndpoint.getPetById(99999999L);

        // Assert
        ResponseValidator.validateStatusCode(response, 404);
        ResponseValidator.validateFieldValue(response, "message", "Pet not found");
    }

    @Test(priority = 8, description = "Get pet with invalid ID format")
    @Story("Get Pet - Negative")
    @Severity(SeverityLevel.MINOR)
    public void testGetPetWithInvalidIdFormat() {
        // Act
        Response response = petEndpoint.getPetById(-1L);

        // Assert
        ResponseValidator.validateStatusCode(response, 400);
    }

    @Test(priority = 9, description = "Update non-existent pet")
    @Story("Update Pet - Negative")
    @Severity(SeverityLevel.NORMAL)
    public void testUpdateNonExistentPet() {
        // Arrange
        Pet nonExistentPet = TestDataGenerator.createRandomPet();
        nonExistentPet.setId(99999999L);

        // Act
        Response response = petEndpoint.updatePet(nonExistentPet);

        // Assert
        ResponseValidator.validateStatusCode(response, 404);
    }

    @Test(priority = 10, description = "Delete non-existent pet")
    @Story("Delete Pet - Negative")
    @Severity(SeverityLevel.NORMAL)
    public void testDeleteNonExistentPet() {
        // Act
        Response response = petEndpoint.deletePet(99999999L);

        // Assert
        ResponseValidator.validateStatusCode(response, 404);
    }

    @Test(priority = 11, description = "Find pets with invalid status")
    @Story("Find Pets - Negative")
    @Severity(SeverityLevel.MINOR)
    public void testFindPetsByInvalidStatus() {
        // Act
        Response response = petEndpoint.getPetsByStatus("invalid_status");

        // Assert
        ResponseValidator.validateStatusCode(response, 400);
    }

    @Test(priority = 12, description = "Create pet without required fields")
    @Story("Create Pet - Negative")
    @Severity(SeverityLevel.NORMAL)
    public void testCreatePetWithoutRequiredFields() {
        // Arrange
        Pet incompletePet = Pet.builder()
                .name("TestPet")
                // Missing required photoUrls
                .build();

        // Act
        Response response = petEndpoint.createPet(incompletePet);

        // Assert
        ResponseValidator.validateStatusCode(response, 200);
    }
}


