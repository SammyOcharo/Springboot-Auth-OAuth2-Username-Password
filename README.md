# Springboot-Auth-OAuth2-Username-Password

This is a Spring Boot project that implements authentication using both **Username/Password** login and **OAuth2.0** (with GitHub and Google as providers). It demonstrates how to secure APIs and web applications by supporting both traditional username/password authentication and modern OAuth2.0 login mechanisms.

## Features

- **Username/Password Authentication** using Spring Security.
- **OAuth2.0 Login** with GitHub and Google authentication.
- **JWT (JSON Web Token)** generation for securing API endpoints.
- **MySQL Integration** for storing user credentials and OAuth tokens.
- Secure password storage using **BCrypt** hashing.

## Prerequisites

Before you begin, ensure you have the following installed:

- **JDK 21** or higher.
- **Maven** for managing dependencies.
- **MySQL** or another relational database for user authentication storage.
- **GitHub** and **Google OAuth2 credentials**.

## Getting Started

### 1. Clone the Repository

Clone this repository to your local machine:

```bash
git clone https://github.com/your-username/Springboot-Auth-OAuth2-Username-Password.git
cd Springboot-Auth-OAuth2-Username-Password

```


### 2. Set Up the Database
Make sure you have MySQL installed and set up. Create a database for your project:

sql

```
CREATE DATABASE customizedAuth;
```
Update your application.properties (or application.yml) file with the correct database credentials:

properties
```
spring.datasource.url=jdbc:mysql://localhost:3306/customizedAuth?createDatabaseIfNotExist=true
spring.datasource.username=<your-db-username>
spring.datasource.password=<your-db-password>
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect

```

### 3. Set Up OAuth2.0 Credentials
You need to create OAuth2 credentials for Google and GitHub to enable authentication via these providers.

GitHub: Go to the GitHub Developer settings and create a new OAuth application.
Google: Visit Google Developers Console, create a new project, and set up OAuth2 credentials.
Once you have the credentials, add them to your application.properties file:

properties
```
# GitHub OAuth2.0 Configuration
spring.security.oauth2.client.registration.github.client-id=<your-github-client-id>
spring.security.oauth2.client.registration.github.client-secret=<your-github-client-secret>

# Google OAuth2.0 Configuration
spring.security.oauth2.client.registration.google.client-id=<your-google-client-id>
spring.security.oauth2.client.registration.google.client-secret=<your-google-client-secret>
spring.security.oauth2.client.registration.google.scope=profile,email
```

### 4. Build and Run the Application
To build and run the application, use Maven:

bash
```
mvn clean install
mvn spring-boot:run
```
By default, the application will run on http://localhost:8080.

### 5. Testing the Authentication
Username/Password Login: Use the /login endpoint to authenticate using a username and password.
OAuth2.0 Login: Use the /oauth2/authorization/github or /oauth2/authorization/google endpoints to authenticate using GitHub or Google.
Once authenticated, a JWT token will be issued and can be used to access protected endpoints.

Project Structure
Here’s a quick overview of the project structure:


Security Configurations
Password Encoder: The project uses BCryptPasswordEncoder for securely hashing passwords.
JWT Integration: JWT tokens are issued upon successful login and are used for authentication in subsequent requests.
OAuth2 Login: Supports both GitHub and Google as OAuth2.0 providers.
Example Request
To test the OAuth2.0 authentication, visit the following endpoints:

GitHub OAuth2.0 Login: 
```
http://localhost:8080/oauth2/authorization/github
```
Google OAuth2.0 Login: 
```
http://localhost:8080/oauth2/authorization/google

```
After successful authentication, you will receive a JWT token that can be used to authenticate requests to secured endpoints.

Conclusion
This project provides a comprehensive guide to implementing OAuth2.0 authentication alongside traditional username/password login in a Spring Boot application. You can extend it to include more OAuth2.0 providers or implement additional features such as role-based access control.

License
This project is licensed under the MIT License - see the LICENSE file for details.
