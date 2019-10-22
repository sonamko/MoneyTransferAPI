# Money transfer Rest API

A Java RESTful API for money transfers between users accounts

### Technologies
- JAX-RS API
- As an in memory database static Concurrent Hashmap used
- Jetty Container 
- Grizzly server for Test

### How to run
   Executable jar at path : executableJar/

     java -jar moneytransfer-1.0   {Will start the server, the api can then be hit using Postman}
```

#### Implementation Design
Application starts a jetty server on localhost port 8080 . An in memory database (currently concurrent Hashmap) initialized with some sample user and account data To view

- http://localhost:8080/userAccount/1
- http://localhost:8080/userAccount/2

-Basic validations to prompt the user in case the request is not proper. For eg, when user does not have sufficient balance

-Junit Testcases to user account service and transaction service.


### Available Services


	| HTTP METHOD |       PATH            | USAGE  |
	| ----------- | --------------------- | ------ |
	| GET         | /userAccount/{userId} | get userAccount by user Id | 
	| GET         | /userAccount/         | get all userAccounts |
	| POST        | /userAccount/         | create a new userAccount | 
	| PUT         | /userAccount/{userId} | update userAccount | 
	| DELETE      | /userAccount/{userId} | remove userAccount | 
	| POST        | /transaction/withdraw | withdraw money from account | 
	| POST        | /transaction/deposit  | deposit money to account | 
	| POST        | /transaction/transfer | perform transfer between 2 user accounts | 

### Http Status
- 200 OK: The request has succeeded
- 201 Created: The request has been created .
- 400 Bad Request: The request could not be understood by the server 
- 404 Not Found: The requested resource cannot be found
- 500 Internal Server Error: The server encountered an unexpected condition 

### Sample JSON for User and Account
##### User : 
```sh
{
	"userName" : "Test",
	"balance" : "100"
	}
```
##### User Transaction: 

```sh
{  
   "fromUserId": "2",
	 "toUserId": "1",
	"amount": "100"
} 
```
#### Current Limitations:
- Static Hashmap can be replaced by in memory database like H2
- Validations can be enhanced.
