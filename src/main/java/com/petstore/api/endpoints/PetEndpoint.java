package com.petstore.api.endpoints;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import com.petstore.api.models.Pet;
import com.petstore.api.config.TestConfig;
import static io.restassured.RestAssured.given;

public class PetEndpoint {
    private static final String PET_ENDPOINT = "/pet";
    private static final String PET_BY_ID = PET_ENDPOINT + "/{petId}";
    private static final String PET_BY_STATUS = PET_ENDPOINT + "/findByStatus";
    private static final String PET_UPLOAD_IMAGE = PET_BY_ID + "/uploadImage";

    private RequestSpecification requestSpec;

    public PetEndpoint() {
        this.requestSpec = TestConfig.getRequestSpec();
    }

    // CREATE
    public Response createPet(Pet pet) {
        return given()
                .spec(requestSpec)
                .body(pet)
                .when()
                .post(PET_ENDPOINT);
    }

    // READ
    public Response getPetById(Long petId) {
        return given()
                .spec(requestSpec)
                .pathParam("petId", petId)
                .when()
                .get(PET_BY_ID);
    }

    public Response getPetsByStatus(String status) {
        return given()
                .spec(requestSpec)
                .queryParam("status", status)
                .when()
                .get(PET_BY_STATUS);
    }

    // UPDATE
    public Response updatePet(Pet pet) {
        return given()
                .spec(requestSpec)
                .body(pet)
                .when()
                .put(PET_ENDPOINT);
    }

    public Response updatePetWithFormData(Long petId, String name, String status) {
        return given()
                .spec(requestSpec)
                .pathParam("petId", petId)
                .formParam("name", name)
                .formParam("status", status)
                .when()
                .post(PET_BY_ID);
    }

    // DELETE
    public Response deletePet(Long petId) {
        return given()
                .spec(requestSpec)
                .pathParam("petId", petId)
                .when()
                .delete(PET_BY_ID);
    }

    public Response deletePetWithApiKey(Long petId, String apiKey) {
        return given()
                .spec(requestSpec)
                .header("api_key", apiKey)
                .pathParam("petId", petId)
                .when()
                .delete(PET_BY_ID);
    }
}
