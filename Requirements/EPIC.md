This scenario is meant to test your every capability in programming modern day applications. 

### Basic order management system
- tracking the life time of an order
- managing its state transitions 
- managing order exceptions. 

### Functional Requirements
- An order has 4 regular states: Created, Paid, In Fulfillment, and Closed.
- Created 
  - it is triggered by a REST endpoint which accepts a basket of items of your choice. // done
  - This creation process should ensure same requirements for an order to be accepted. (Validation???)
- Paid // done
  - a REST endpoint which confirms the order as paid.  // done
- In fulfillment // done
  - an auxiliary mock endpoint in the system, producing random results (success or failure).
- Closed
  - the order is closed and fulfillment result persisted in the order.
  
### Non-functional Requirements

- The whole application should be structured in the form of state machines. // done
- Every aspect of the application shall be 100% parallelizable, idempotent and 
- Unit tested.
- Rules to programming style apply as usual and documentation shall be down to a minimum. 
- It should be possible to crash your application at any point in time and to continue processing after restart. // done
- Kotlin Programming Language. // done
- lightweight approach instead of Spring Boot applications. // done, KTOR + KOIN + EXPOSE
- As your application is expected to withstand shutdowns, state management will be handled by a database of your choice. // done, Kafka with event driven
- We recommend building test cases based on our requirements
- automate the whole build process as well as testing the application using Gradle (http://www.gradle.org).

### Further remarks

- Most important for us is to understand how you tackle complex problems. 
- Therefore, please finish this challenge as far as possible under the given technological requirements. 
- A correct and understandable partial implementation is more important than a complete one
- The assignment will be presented in a live demo as part of the personal interview

### Nice to have
- DLQs // done
- Retries // done
- config by environment // done
- E2E test with in memory kafka and postgres instance
- Swagger generated API class
- Monitoring metrics
