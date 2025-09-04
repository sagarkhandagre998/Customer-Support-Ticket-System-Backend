# 🎫 Ticketing System Backend

A comprehensive **Spring Boot** backend application for managing support tickets with role-based access control, real-time notifications, and advanced filtering capabilities.

## 📋 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Prerequisites](#-prerequisites)
- [Installation & Setup](#-installation--setup)
- [Database Configuration](#-database-configuration)
- [Environment Variables](#-environment-variables)
- [API Documentation](#-api-documentation)
- [Project Structure](#-project-structure)
- [Running the Application](#-running-the-application)
- [Testing](#-testing)
- [Contributing](#-contributing)
- [License](#-license)

## ✨ Features

### 🔐 Authentication & Authorization
- **JWT-based authentication** with secure token management
- **Role-based access control** (User, Agent, Admin)
- **Password encryption** using BCrypt
- **Session management** with configurable token expiration

### 🎫 Ticket Management
- **Create, read, update, and delete** tickets
- **Advanced filtering** by status, priority, category, and assignee
- **Real-time status updates** (Open, In Progress, Resolved, Closed)
- **Priority levels** (Low, Medium, High, Critical)
- **Category-based organization**
- **File attachments** support

### 💬 Communication
- **Ticket comments** system for collaboration
- **Email notifications** for status changes and assignments
- **Real-time updates** for ticket modifications

### 📊 Admin Dashboard
- **User management** (create, update, delete users)
- **Role assignment** and permission management
- **Ticket oversight** and force status updates
- **System statistics** and analytics

### 🔍 Advanced Features
- **Search functionality** across tickets
- **Pagination** for large datasets
- **CORS configuration** for frontend integration
- **Health monitoring** endpoints
- **Database migrations** with Flyway

## 🛠 Tech Stack

- **Framework**: Spring Boot 3.2.5
- **Language**: Java 17
- **Database**: PostgreSQL
- **Security**: Spring Security + JWT
- **ORM**: Spring Data JPA + Hibernate
- **Migration**: Flyway
- **Email**: Spring Mail (Gmail SMTP)
- **Build Tool**: Maven
- **Documentation**: Lombok for clean code

## 📋 Prerequisites

Before running this application, ensure you have the following installed:

- **Java 17** or higher
- **Maven 3.6+**
- **PostgreSQL 12+** (or access to a PostgreSQL database)
- **Git** for version control

## 🚀 Installation & Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd backend
```

### 2. Database Setup

#### Option A: Local PostgreSQL
```bash
# Install PostgreSQL (Ubuntu/Debian)
sudo apt-get install postgresql postgresql-contrib

# Create database
sudo -u postgres createdb ticketing_system

# Create user (optional)
sudo -u postgres createuser --interactive
```

#### Option B: Cloud Database (Recommended)
- Use **Neon**, **Supabase**, or **AWS RDS** for PostgreSQL
- Note the connection details for configuration

### 3. Configure Application

Update `src/main/resources/application.yml` with your database credentials:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://your-host:5432/your-database
    username: your-username
    password: your-password
```

### 4. Email Configuration

Configure Gmail SMTP settings in `application.yml`:

```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: your-email@gmail.com
    password: your-app-password  # Use Gmail App Password
```

> **⚠️ Important**: Use Gmail App Password, not your regular password. Enable 2FA and generate an app password in your Google Account settings.

### 5. JWT Configuration

Update JWT settings in `application.yml`:

```yaml
app:
  jwt:
    secret: your-secure-secret-key-here
    expiration-ms: 3600000  # 1 hour
```

## 🔧 Environment Variables

For production deployment, consider using environment variables:

```bash
export DB_URL=jdbc:postgresql://your-host:5432/your-database
export DB_USERNAME=your-username
export DB_PASSWORD=your-password
export JWT_SECRET=your-secure-secret-key
export EMAIL_USERNAME=your-email@gmail.com
export EMAIL_PASSWORD=your-app-password
```

## 📚 API Documentation

### Authentication Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|---------|
| POST | `/auth/register` | Register new user | Public |
| POST | `/auth/login` | User login | Public |
| GET | `/auth/me` | Get current user | Authenticated |

### Ticket Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|---------|
| POST | `/tickets/create` | Create new ticket | Authenticated |
| GET | `/tickets` | Get all tickets (filtered) | Authenticated |
| GET | `/tickets/my-tickets` | Get user's tickets | Authenticated |
| GET | `/tickets/{id}` | Get ticket by ID | Authenticated |
| PUT | `/tickets/{id}/status` | Update ticket status | Agent/Admin |

### Admin Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|---------|
| GET | `/admin/users` | Get all users | Admin |
| POST | `/admin/users` | Create user | Admin |
| PUT | `/admin/users/{id}/role` | Assign role | Admin |
| DELETE | `/admin/users/{id}` | Delete user | Admin |
| GET | `/admin/tickets` | Get all tickets | Admin |
| PATCH | `/admin/tickets/{id}/status` | Force update status | Admin |
| PATCH | `/admin/tickets/{id}/assign` | Reassign ticket | Admin |

### Comment Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|---------|
| POST | `/comments` | Add comment to ticket | Authenticated |
| GET | `/comments/ticket/{ticketId}` | Get ticket comments | Authenticated |

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/application/
│   │   ├── Controllers/          # REST API endpoints
│   │   │   ├── AdminController.java
│   │   │   ├── AuthController.java
│   │   │   ├── TicketController.java
│   │   │   └── CommentController.java
│   │   ├── Entities/             # JPA entities
│   │   │   ├── User.java
│   │   │   ├── Ticket.java
│   │   │   ├── TicketComment.java
│   │   │   └── Role.java
│   │   ├── Repository/           # Data access layer
│   │   ├── Services/             # Business logic
│   │   ├── Security/             # Security configuration
│   │   └── dto/                  # Data transfer objects
│   └── resources/
│       ├── application.yml       # Configuration
│       └── db/migration/         # Database migrations
└── test/                         # Test files
```

## 🏃‍♂️ Running the Application

### Development Mode

```bash
# Install dependencies
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Production Mode

```bash
# Build JAR file
mvn clean package

# Run JAR file
java -jar target/ticketing-backend-0.0.1-SNAPSHOT.jar
```

### Using Maven Wrapper

```bash
# Windows
./mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

## 🧪 Testing

### Run Unit Tests

```bash
mvn test
```

### Run Integration Tests

```bash
mvn verify
```

### Health Check

Visit `http://localhost:8080/actuator/health` to check application health.

## 🔒 Security Features

- **JWT Authentication**: Secure token-based authentication
- **Role-based Authorization**: Granular permission system
- **Password Encryption**: BCrypt hashing
- **CORS Configuration**: Secure cross-origin requests
- **Input Validation**: Request validation and sanitization

## 📊 Database Schema

The application uses the following main entities:

- **Users**: User accounts with roles
- **Tickets**: Support tickets with status tracking
- **Ticket Comments**: Communication on tickets
- **Ratings**: User feedback system
- **Roles**: Permission management

## 🚀 Deployment

### Docker Deployment (Optional)

Create a `Dockerfile`:

```dockerfile
FROM openjdk:17-jdk-slim
COPY target/ticketing-backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Cloud Deployment

- **Heroku**: Use Heroku Postgres addon
- **AWS**: Deploy on EC2 with RDS
- **Google Cloud**: Use Cloud Run with Cloud SQL
- **Railway**: One-click deployment with PostgreSQL

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

If you encounter any issues or have questions:

1. Check the [Issues](https://github.com/your-repo/issues) page
2. Create a new issue with detailed information
3. Contact the development team

---

**Built with ❤️ using Spring Boot**
