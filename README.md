# TCP Connection Management System

A Spring Boot-based TCP connection management system that provides APIs for managing TCP servers and clients. This system allows you to register, monitor, and control TCP services with real-time status tracking and connection management.

## Features

- TCP Server Management
  - Register and manage multiple TCP servers
  - Start/Stop/Restart server instances
  - Real-time status monitoring
  - Connection tracking and management

- TCP Client Management
  - Register and manage TCP clients
  - Connection status monitoring
  - Automatic reconnection handling

- RESTful API
  - Service registration and management
  - Status monitoring endpoints
  - Health check endpoints
  - Metrics collection

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Spring Boot 3.2.3

## Configuration

The application is configured through `application.properties`:

```properties
# Server Configuration
server.port=9092

# Logging Configuration
logging.level.root=INFO
logging.level.ir.co.ocs=DEBUG

# Actuator Configuration
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.show-details=always
```

## Building the Project

```bash
mvn clean install
```

## Running the Application

```bash
mvn spring-boot:run
```

The application will start on port 9092.

## API Endpoints

### Service Management

- `POST /api/services` - Register a new service
- `GET /api/services` - List all registered services
- `GET /api/services/{serviceId}` - Get service status
- `POST /api/services/{serviceId}/start` - Start a service
- `POST /api/services/{serviceId}/stop` - Stop a service
- `POST /api/services/{serviceId}/restart` - Restart a service

### Health and Monitoring

- `GET /actuator/health` - Application health status
- `GET /actuator/info` - Application information
- `GET /actuator/metrics` - Application metrics

## Example Usage

### Registering a TCP Server

```bash
curl -X POST http://localhost:9092/api/services \
  -H "Content-Type: application/json" \
  -d '{
    "name": "TestServer",
    "type": "SERVER",
    "host": "localhost",
    "port": 8080,
    "protocol": "TCP"
  }'
```

### Registering a TCP Client

```bash
curl -X POST http://localhost:9092/api/services \
  -H "Content-Type: application/json" \
  -d '{
    "name": "TestClient",
    "type": "CLIENT",
    "host": "localhost",
      "port": 8080,
    "protocol": "TCP"
  }'
```

## Dependencies

- Spring Boot Web
- Spring Boot Actuator
- Apache MINA
- Lombok
- JUnit 5
- Mockito

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support, please open an issue in the GitHub repository or contact the maintainers. 
