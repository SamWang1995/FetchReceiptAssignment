Running the Application:

1. Make sure Maven is installed (`brew install maven`)
2. Build the project: `mvn clean install`
3. Run the application: mvn spring-boot:run or locate to ReceiptsApplication.java and hit the run button on your IDE
4. Use a tool like Postman to test the endpoints

Running on Docker:
1. Build Docker image docker: `build -t receipt-application .`
2. Run in Docker Container: `docker run -p 8080:8080 receipt-application`
