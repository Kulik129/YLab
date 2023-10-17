package containers;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresTestContainer extends PostgreSQLContainer<PostgresTestContainer> {


    public static final String IMAGE_VERSION = "postgres:11.1";
    public static final String DATABASE_NAME = "y_lab";
    public static PostgreSQLContainer container;

    public PostgresTestContainer() {
        super(IMAGE_VERSION);
    }

    public static PostgreSQLContainer getInstance() {
        if (container == null) {
            container = new PostgresTestContainer().withDatabaseName(DATABASE_NAME);
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("db.url", container.getJdbcUrl());
        System.setProperty("db.username", container.getUsername());
        System.setProperty("db.password", container.getPassword());
    }

    @Override
    public void stop() {
    }
}
