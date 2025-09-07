package com.petstore.utils;

import io.restassured.response.Response;
import org.testng.Assert;
import static org.hamcrest.Matchers.*;

public class ResponseValidator {

    public static void validateStatusCode(Response response, int expectedStatusCode) {
        Assert.assertEquals(response.getStatusCode(), expectedStatusCode,
                "Status code mismatch. Response: " + response.asString());
    }

    public static void validateResponseTime(Response response, long maxTimeInMs) {
        Assert.assertTrue(response.getTime() < maxTimeInMs,
                "Response time exceeded limit. Actual: " + response.getTime() + "ms");
    }

    public static void validateJsonSchema(Response response, String schemaPath) {
        // Schema validation can be added here
        response.then().assertThat()
                .contentType("application/json");
    }

    public static void validateFieldExists(Response response, String field) {
        response.then().assertThat()
                .body(field, notNullValue());
    }

    public static void validateFieldValue(Response response, String field, Object expectedValue) {
        response.then().assertThat()
                .body(field, equalTo(expectedValue));
    }

    public static void validateErrorMessage(Response response, String expectedMessage) {
        response.then().assertThat()
                .body("message", containsString(expectedMessage));
    }
}
