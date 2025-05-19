# OpenAPI Specification Scoring App

## Table of Contents
1. [About Application](#about-application)
2. [Scoring](#scoring)
3. [Setup](#setup)
4. [Testing](#testing)

## About Application
This Spring Boot application takes an OpenAPI (v3.x) spec in JSON, evaluates its structure and content against industry best practices, and provides a score for each criterion with clear, actionable feedback. 

Made by Elene Kvitsiani

Disclaimer: ekvit22 is my other account for university and I changed my github account in Intellij after I started creating this project so that's why the first 2 commits are done by that account.

## Scoring
### Scoring Criteria (each weighted; total = 100 points)

**Schema & Types (20 pts):** Use of proper data types; no free‑form objects without schema.

**Descriptions & Documentation (20 pts):** All paths, operations, parameters, request bodies, and responses include meaningful description fields.

**Paths & Operations (15 pts):** Consistent naming and CRUD conventions; no overlapping or redundant paths.

**Response Codes (15 pts):** Appropriate use of HTTP status codes; each operation defines expected success and error codes.

**Examples & Samples (10 pts):** Presence of request/response examples for major endpoints.

**Security (10 pts):** Defined and referenced security schemes where needed.

**Miscellaneous Best Practices (10 pts):** e.g., versioning, servers array, tags, components reuse.

## Setup

### Clone the GitHub Repository
```bash
git clone <repo-url>
cd <folder-name>
```
### Build the applicaiton
Keep in mind that this application starts on `http://localhost:8000`. If you would like to change that then change the port in `src/main/resources/application.properties`.
The application is configured using Maven, therefore we need to run maven:
```
mvn clean validate install
```
To start the application you need to run:
```
mvn spring-boot:run
```
### (Optional, but recommended) Postman
To run the application with Postman, write `http://localhost:8000/api/score` in the address bar. Choose POST request, then go to `Body` and choose `raw` and `JSON` for file types.
Input the OpenAPI spec in JSON to send the request.

## **API Endpoints**
### POST: /api/score
Sends the scorecard
**Example:**
```

```
**Response for valid input:**
```
HTTP/1.1 200 OK
```
**Response for invalid input:**
```
HTTP/1.1 400 BAD REQUEST
```

## Testing
You can see the tests in `src/test/java/org.example.theneoassignment`. These tests also use custom JSON files located in `src/test/resources/unit_tests` for Unit Tests that test each scoring criterion and `src/test/resources/samples` contain actual public OpenAPI sample specs. These public samples were taken from `https://github.com/APIs-guru/openapi-directory/tree/main`. Run the tests with:
```
mvn test
```


