package com.petstore.api.endpoints;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import com.petstore.api.models.Order;
import com.petstore.api.config.TestConfig;
import static io.restassured.RestAssured.given;

public class StoreEndpoint {
    private static final String STORE_ENDPOINT = "/store";
    private static final String ORDER_ENDPOINT = STORE_ENDPOINT + "/order";
    private static final String ORDER_BY_ID = ORDER_ENDPOINT + "/{orderId}";
    private static final String INVENTORY = STORE_ENDPOINT + "/inventory";

    private RequestSpecification requestSpec;

    public StoreEndpoint() {
        this.requestSpec = TestConfig.getRequestSpec();
    }

    // CREATE
    public Response placeOrder(Order order) {
        return given()
                .spec(requestSpec)
                .body(order)
                .when()
                .post(ORDER_ENDPOINT);
    }

    // READ
    public Response getOrderById(Long orderId) {
        return given()
                .spec(requestSpec)
                .pathParam("orderId", orderId)
                .when()
                .get(ORDER_BY_ID);
    }

    public Response getInventory() {
        return given()
                .spec(requestSpec)
                .when()
                .get(INVENTORY);
    }

    // DELETE
    public Response deleteOrder(Long orderId) {
        return given()
                .spec(requestSpec)
                .pathParam("orderId", orderId)
                .when()
                .delete(ORDER_BY_ID);
    }
}
