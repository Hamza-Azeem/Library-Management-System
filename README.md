# **Library Management System Documentation**

## **Getting Started**

To run the application using Spring Boot and MySQL, follow these steps:

1. **Clone the Repository:**
   - Clone the project from GitHub to your local machine.

2. **Database Setup:**
   - Open MySQL Workbench.
   - Create a new schema called `library` using the following SQL command:
     ```sql
     CREATE SCHEMA library;
     ```

3. **Configure Database Connection:**
   - Open the project directory in your favorite IDE.
   - Navigate to the `application.properties` file.
   - Update the following properties with your MySQL credentials:
     ```properties
     spring.datasource.username=your-database-username
     spring.datasource.password=your-database-password
     ```

4. **Run the Application:**
   - Start the application using your IDE's run configuration or by executing the command:
     ```bash
     mvn spring-boot:run
     ```

After completing these steps, the application should be up and running.

## **Application Initialization**

- Upon the first start of the application, a default user is created:
  - **Username:** `admin`
  - **Password:** `admin`
  - **Role:** `ADMIN`

## **Security and Authorization**

The application integrates role-based security. The roles and their corresponding access levels are as follows:

- **MANAGER:**
  - Can access all endpoints without restrictions.
  - You can create a role named `MANAGER` in the `role` table and assign it to a user.

- **ADMIN:**
  - Can access all `POST`, `PUT`, and `GET` endpoints.
  - Cannot access `DELETE` endpoints.
  - The `admin` user has this role by default.

- **PATRON:**
  - Can only access specific endpoints if the patron being accessed is the same as the one connected to the user:
    - `GET /api/patron/{id}`
    - `PUT /api/patron/{id}`

- **USER:**
  - Can access only the `POST /api/patron` endpoint to create a new patron for their account.

- **Public Endpoints:**
  - `GET /api/book`
  - `GET /api/book/{id}`
  - `POST /api/auth/register`

## **API Endpoints**


### **Book Management**

- **GET /api/book:**
  - Retrieve a list of all books.

- **GET /api/book/{id}:**
  - Retrieve details of a specific book by its ID.

- **POST /api/book:**
  - Add a new book to the library.
  - **Request Body:**
    ```json
    {
      "title": "The Great Gatsby",
      "author": "F. Scott Fitzgerald",
      "publicationYear": 1925,
      "isbn": "9780743273565"
    }
    ```

- **PUT /api/book/{id}:**
  - Update an existing book's information.
  - **Request Body:**
    ```json
    {
      "title": "Updated Title",
      "author": "Updated Author",
      "publicationYear": 2022,
      "isbn": "9780743273565"
    }
    ```

- **DELETE /api/book/{id}:**
  - Remove a book from the library.

### **Patron Management**

- **GET /api/patron:**
  - Retrieve a list of all patrons.

- **GET /api/patron/{id}:**
  - Retrieve details of a specific patron by ID.

- **POST /api/patron:**
  - Add a new patron to the system, linked to the currently authenticated user account. The user's information is provided through the `Authorization` header.
  - **Request Body:**
    ```json
    {
        "name": "John Doe",
        "email": "john.doe@example.com",
        "phoneNumber": "(941) 430-6886 x985",
        "address": "Suite 071 968 Maribeth Mission, South Cletustown, WA 60694"
    }
    ```

- **PUT /api/patron/{id}:**
  - Update an existing patron's information.
  - **Request Body:**
    ```json
    {
        "name": "John Doe",
        "email": "john.doe@example.com",
        "phoneNumber": "(941) 430-6886 x985",
        "address": "Suite 071 968 Maribeth Mission, South Cletustown, WA 60694"
    }
    ```

- **DELETE /api/patron/{id}:**
  - Remove a patron from the system.

### **Borrowing**

- **POST /api/borrow/{bookId}/patron/{patronId}:**
  - Allow a patron to borrow a book.

- **PUT /api/return/{bookId}/patron/{patronId}:**
  - Record the return of a borrowed book by a patron.
 
### **Authentication**

- **POST /api/auth/register:**
  - Allow anyone to create a user.
  - **Request Body:**
    ```json
    {
        "username": "JohnDoe12",
        "password": "password"
    }
    ```

## **Data Storage**

The application uses **MySQL** as the database to persist details about books, patrons, users, roles and borrowing records. Relationships between entities are properly set up to maintain data integrity.

## **Validation and Error Handling**

- All API requests undergo input validation to ensure data correctness.
- In case of validation failures or exceptions, appropriate HTTP status codes and error messages are returned to the client.

## **Authentication and Authorization**

- The application has integrated security features that enforce role-based authentication and authorization.

## **Caching**

- A caching mechanism is implemented to ensure fast data retrieval.
