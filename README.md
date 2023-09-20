# Mihalik: demo project for Thales - Building Rest CRUD API example

Build:
mvn clean install

Run:
mvn spring-boot:run

Usage:
POST, PUT, GET and DEL requests on API
http://localhost:8080/api/transactions

Please, refer to postman_collection.json for detailed tests.
Please let me know if you need to see any other test examples e.g. unit tests.

I made simple search engine with org.springframework.data.jpa.Specification
You can search by actor, type and createdBefore and createdAfter criteria. See "get by query" postman example
