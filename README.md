# Booking Service

This project provides a basic structure for a cleaning service reservation system. It utilizes PostgreSQL as the database, Swagger for documentation, and includes sample data loaded using the `SampleDataLoader` class.

## Technologies Used

- Spring Boot
- Swagger [LocalHost Link](http://localhost:8080/swagger-ui/index.html)
- PostgreSQL
- MapStruct
- Spring Cache
- Java 17

### Prerequisites

Before running the project, it is necessary to create and start a PostgreSQL database using Docker or you can change application.yml You can use the following command:

```bash
docker run --name docker_postgres -e POSTGRES_PASSWORD=123456 -d -p 5432:5432 -v $HOME/docker/volumes/postgres:/var/lib/postgresql/data postgres
