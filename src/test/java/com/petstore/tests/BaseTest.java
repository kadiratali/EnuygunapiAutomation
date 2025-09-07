package com.petstore.tests;

import com.petstore.api.config.TestConfig;
import com.petstore.api.endpoints.*;
import org.testng.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.qameta.allure.Step;

public class BaseTest {
    protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);

    protected PetEndpoint petEndpoint;
    protected UserEndpoint userEndpoint;
    protected StoreEndpoint storeEndpoint;

    @BeforeSuite
    public void setupSuite() {
        logger.info("Starting Test Suite");
        TestConfig.setupRestAssured();
    }

    @BeforeClass
    public void setupClass() {
        petEndpoint = new PetEndpoint();
        userEndpoint = new UserEndpoint();
        storeEndpoint = new StoreEndpoint();
    }

    @BeforeMethod
    public void setupMethod() {
        logger.info("Starting test: " + this.getClass().getSimpleName());
    }

    @AfterMethod
    public void tearDownMethod() {
        logger.info("Test completed");
    }

    @AfterSuite
    public void tearDownSuite() {
        logger.info("Test Suite completed");
    }

    @Step("Cleanup test data with ID: {0}")
    protected void cleanupTestData(Long id) {
        try {
            petEndpoint.deletePet(id);
        } catch (Exception e) {
            logger.warn("Failed to cleanup test data: " + e.getMessage());
        }
    }
}
