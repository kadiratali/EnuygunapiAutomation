package com.petstore.api.endpoints;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import com.petstore.api.models.User;
import com.petstore.api.config.TestConfig;
import static io.restassured.RestAssured.given;

public class UserEndpoint {
    private static final String USER_ENDPOINT = "/user";
    private static final String USER_BY_USERNAME = USER_ENDPOINT + "/{username}";
    private static final String USER_LOGIN = USER_ENDPOINT + "/login";
    private static final String USER_LOGOUT = USER_ENDPOINT + "/logout";

    private RequestSpecification requestSpec;

    public UserEndpoint() {
        this.requestSpec = TestConfig.getRequestSpec();
    }

    // CREATE
    public Response createUser(User user) {
        return given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post(USER_ENDPOINT);
    }

    // READ
    public Response getUserByUsername(String username) {
        return given()
                .spec(requestSpec)
                .pathParam("username", username)
                .when()
                .get(USER_BY_USERNAME);
    }

    // UPDATE
    public Response updateUser(String username, User user) {
        return given()
                .spec(requestSpec)
                .pathParam("username", username)
                .body(user)
                .when()
                .put(USER_BY_USERNAME);
    }

    // DELETE
    public Response deleteUser(String username) {
        return given()
                .spec(requestSpec)
                .pathParam("username", username)
                .when()
                .delete(USER_BY_USERNAME);
    }

    // LOGIN/LOGOUT
    public Response loginUser(String username, String password) {
        return given()
                .spec(requestSpec)
                .queryParam("username", username)
                .queryParam("password", password)
                .when()
                .get(USER_LOGIN);
    }

    public Response logoutUser() {
        return given()
                .spec(requestSpec)
                .when()
                .get(USER_LOGOUT);
    }
}
