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
