```markdown
# Petstore API Test Framework

## Overview
Comprehensive API testing framework for Petstore API using REST Assured and TestNG with Java.

## Features
- ✅ Complete CRUD operation testing
- ✅ Positive and negative test scenarios
- ✅ Request/Response logging
- ✅ Response schema validation
- ✅ OOP design principles
- ✅ Data-driven testing support
- ✅ Allure reporting integration
- ✅ Parallel test execution
- ✅ Configurable test environment

## Prerequisites
- Java 11 or higher
- Maven 3.6+
- IDE (IntelliJ IDEA / Eclipse)

## Project Structure
```
petstore-api-tests/
├── src/main/java       - Main source code
│   ├── endpoints       - API endpoint classes
│   ├── models         - POJO models
│   └── utils          - Utility classes
├── src/test/java      - Test classes
└── src/test/resources - Test configuration
```

## Setup Instructions

### 1. Clone Repository
```bash
git clone <repository-url>
cd petstore-api-tests
```

### 2. Install Dependencies
```bash
mvn clean install
```

### 3. Configure Environment
Edit `src/test/resources/config.properties`:
```properties
api.base.url=https://petstore.swagger.io/v2
api.log.enabled=true
```

## Running Tests

### Run All Tests
```bash
mvn clean test
```

### Run Specific Test Class
```bash
mvn test -Dtest=PetCrudTest
```

### Run Specific Test Method
```bash
mvn test -Dtest=PetCrudTest#testCreatePetPositive
```

### Run Test Suite
```bash
mvn test -DsuiteXmlFile=testng.xml
```

### Run with Specific Tags
```bash
mvn test -Dgroups=smoke
```

## Test Reports

### Generate Allure Report
```bash
 mvn io.qameta.allure:allure-maven:serve
```

### View TestNG Report
Open `target/surefire-reports/index.html`

## Test Scenarios

### Pet Management
**Positive Scenarios:**
- Create pet with valid data
- Get pet by ID
- Update existing pet
- Delete pet
- Find pets by status

**Negative Scenarios:**
- Create pet with invalid data
- Get non-existent pet
- Update with invalid data
- Delete non-existent pet
- Invalid status search

### User Management
**Positive Scenarios:**
- Create user
- Get user by username
- Update user
- Delete user
- User login/logout

**Negative Scenarios:**
- Invalid user data
- Non-existent user operations
- Invalid credentials

### Store Management
**Positive Scenarios:**
- Place order
- Get order by ID
- Delete order
- Get inventory

**Negative Scenarios:**
- Invalid order data
- Non-existent order operations

## Key Features Implementation

### 1. OOP Principles
- **Encapsulation**: Private fields with getters/setters
- **Inheritance**: BaseTest class for common functionality
- **Abstraction**: Endpoint classes abstract API calls
- **Polymorphism**: Method overloading for different scenarios

### 2. Error Handling
- Custom exception handling
- Retry mechanism for flaky tests
- Graceful failure reporting

### 3. Logging
- Request/Response logging
- Test execution logs
- Configurable log levels

### 4. Data Management
- Test data generators
- Builder pattern for models
- External test data files

## Best Practices
1. **Independent Tests**: Each test can run independently
2. **Clean State**: Tests clean up after themselves
3. **Meaningful Names**: Clear, descriptive test names
4. **Assertions**: Multiple validation points
5. **Documentation**: JavaDoc for complex methods
6. **Configuration**: Externalized configuration

## CI/CD Integration

### Jenkins Pipeline
```groovy
pipeline {
    agent any
    stages {
        stage('Test') {
            steps {
                sh 'mvn clean test'
            }
        }
        stage('Report') {
            steps {
                allure results: [[path: 'target/allure-results']]
            }
        }
    }
}
```

### GitHub Actions
```yaml
name: API Tests
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - uses: actions/setup-java@v2
        with:
          java-version: '11'
      - run: mvn clean test
```

## Troubleshooting

### Common Issues
1. **Connection timeout**: Increase timeout in config.properties
2. **Test failures**: Check API availability at https://petstore.swagger.io/
3. **Build errors**: Ensure Java 11+ is installed

## Contributing
1. Fork the repository
2. Create feature branch
3. Commit changes
4. Push to branch
5. Create Pull Request

## License
MIT License