package com.petstore.tests;

import com.petstore.api.models.Order;
import com.petstore.utils.TestDataGenerator;
import com.petstore.utils.ResponseValidator;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.Assert;
import io.qameta.allure.*;
import java.util.Map;

@Epic("Pet Store API")
@Feature("Store Management")
public class StoreCrudTest extends BaseTest {

    // ========== POSITIVE SCENARIOS ==========

    @Test(priority = 1, description = "Place a new order")
    @Story("Place Order")
    @Severity(SeverityLevel.CRITICAL)
    public void testPlaceOrderPositive() {
        // Arrange
        Order newOrder = TestDataGenerator.createRandomOrder();

        // Act
        Response response = storeEndpoint.placeOrder(newOrder);

        // Assert
        ResponseValidator.validateStatusCode(response, 200);
        Order createdOrder = response.as(Order.class);

        Assert.assertNotNull(createdOrder.getId());
        Assert.assertEquals(createdOrder.getPetId(), newOrder.getPetId());
        Assert.assertEquals(createdOrder.getQuantity(), newOrder.getQuantity());

        // Cleanup
        storeEndpoint.deleteOrder(createdOrder.getId());
    }

    @Test(priority = 2, description = "Get order by ID")
    @Story("Get Order")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetOrderByIdPositive() {
        // Arrange
        Order newOrder = TestDataGenerator.createRandomOrder();
        Response createResponse = storeEndpoint.placeOrder(newOrder);
        Order createdOrder = createResponse.as(Order.class);

        // Act
        Response response = storeEndpoint.getOrderById(createdOrder.getId());

        // Assert
        ResponseValidator.validateStatusCode(response, 200);
        Order retrievedOrder = response.as(Order.class);

        Assert.assertEquals(retrievedOrder.getId(), createdOrder.getId());
        Assert.assertEquals(retrievedOrder.getPetId(), createdOrder.getPetId());

        // Cleanup
        storeEndpoint.deleteOrder(createdOrder.getId());
    }

    @Test(priority = 3, description = "Delete existing order")
    @Story("Delete Order")
    @Severity(SeverityLevel.CRITICAL)
    public void testDeleteOrderPositive() {
        // Arrange
        Order newOrder = TestDataGenerator.createRandomOrder();
        Response createResponse = storeEndpoint.placeOrder(newOrder);
        Order createdOrder = createResponse.as(Order.class);

        // Act
        Response response = storeEndpoint.deleteOrder(createdOrder.getId());

        // Assert
        ResponseValidator.validateStatusCode(response, 200);

        // Verify deletion
        Response getResponse = storeEndpoint.getOrderById(createdOrder.getId());
        ResponseValidator.validateStatusCode(getResponse, 404);
    }

    @Test(priority = 4, description = "Get store inventory")
    @Story("Get Inventory")
    @Severity(SeverityLevel.NORMAL)
    public void testGetInventoryPositive() {
        // Act
        Response response = storeEndpoint.getInventory();

        // Assert
        ResponseValidator.validateStatusCode(response, 200);
        Map<String, Integer> inventory = response.as(Map.class);

        Assert.assertNotNull(inventory);
        Assert.assertTrue(inventory.size() > 0, "Inventory should not be empty");
    }

    // ========== NEGATIVE SCENARIOS ==========

    @Test(priority = 5, description = "Get non-existent order")
    @Story("Get Order - Negative")
    @Severity(SeverityLevel.NORMAL)
    public void testGetNonExistentOrder() {
        // Act
        Response response = storeEndpoint.getOrderById(99999999L);

        // Assert
        ResponseValidator.validateStatusCode(response, 404);
        ResponseValidator.validateFieldValue(response, "message", "Order not found");
    }

    @Test(priority = 6, description = "Place order with invalid data")
    @Story("Place Order - Negative")
    @Severity(SeverityLevel.NORMAL)
    public void testPlaceOrderWithInvalidData() {
        // Arrange
        Order invalidOrder = Order.builder()
                .id(-1L) // Invalid ID
                .petId(-1L) // Invalid pet ID
                .quantity(-5) // Invalid quantity
                .status("invalid_status")
                .build();

        // Act
        Response response = storeEndpoint.placeOrder(invalidOrder);

        // Assert
        ResponseValidator.validateStatusCode(response, 400);
    }

    @Test(priority = 7, description = "Delete non-existent order")
    @Story("Delete Order - Negative")
    @Severity(SeverityLevel.NORMAL)
    public void testDeleteNonExistentOrder() {
        // Act
        Response response = storeEndpoint.deleteOrder(99999999L);

        // Assert
        ResponseValidator.validateStatusCode(response, 404);
    }

    @Test(priority = 8, description = "Get order with invalid ID format")
    @Story("Get Order - Negative")
    @Severity(SeverityLevel.MINOR)
    public void testGetOrderWithInvalidIdFormat() {
        // Act
        Response response = storeEndpoint.getOrderById(-1L);

        // Assert
        ResponseValidator.validateStatusCode(response, 400);
    }

    @Test(priority = 9, description = "Place order without required fields")
    @Story("Place Order - Negative")
    @Severity(SeverityLevel.NORMAL)
    public void testPlaceOrderWithoutRequiredFields() {
        // Arrange
        Order incompleteOrder = Order.builder()
                .quantity(1)
                // Missing petId
                .build();

        // Act
        Response response = storeEndpoint.placeOrder(incompleteOrder);

        // Assert
        ResponseValidator.validateStatusCode(response, 400);
    }
}