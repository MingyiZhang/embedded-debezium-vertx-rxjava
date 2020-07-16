package config;

import io.debezium.connector.mysql.MySqlConnector;
import io.debezium.connector.postgresql.PostgresConnector;
import io.debezium.relational.history.FileDatabaseHistory;
import java.util.OptionalInt;
import java.util.Properties;
import java.util.Random;
import org.apache.kafka.connect.storage.FileOffsetBackingStore;

public class DebeziumEngineConfig {

  private final String name;
  private final String offsetStorageFileFilename;
  private final int offsetFlushIntervalMs;
  private final DatabaseConfig databaseConfig;
  private final String databaseServerName;

  public DebeziumEngineConfig(
      String name,
      String offsetStorageFileFilename,
      int offsetFlushIntervalMs,
      DatabaseConfig databaseConfig,
      String databaseServerName) {
    this.name = name;
    this.offsetStorageFileFilename = offsetStorageFileFilename;
    this.offsetFlushIntervalMs = offsetFlushIntervalMs;
    this.databaseConfig = databaseConfig;
    this.databaseServerName = databaseServerName;
  }

  private static int generateRandomInt() {
    Random random = new Random();
    OptionalInt optionalInt = OptionalInt.empty();
    while (optionalInt.isEmpty()) {
      optionalInt = random.ints().findFirst();
    }
    return optionalInt.getAsInt();
  }

  public static String getRandomOffsetStorageFileFilename() {
    return String.format("/tmp/offsets-%d.dat", generateRandomInt());
  }

  private static String getRandomDatabaseHistoryFileFilename() {
    return String.format("/tmp/dbhistory-%d.dat", generateRandomInt());
  }

  public Properties getDebeziumEngineProperties() throws IllegalArgumentException {
    Properties props = new Properties();
    switch (databaseConfig.getDbType()) {
      case DatabaseConfig.POSTGRESQL:
        props.setProperty("connector.class", PostgresConnector.class.getName());
        props.setProperty("database.dbname", databaseConfig.getDbname());
        break;
      case DatabaseConfig.MYSQL:
        props.setProperty("connector.class", MySqlConnector.class.getName());
        props.setProperty("database.server.id", "85744");
        props.setProperty("database.history", FileDatabaseHistory.class.getName());
        props.setProperty("database.history.file.filename", getRandomDatabaseHistoryFileFilename());
        break;
      default:
        throw new IllegalArgumentException("only postgresql and mysql are supported.");
    }
    props.setProperty("name", name);
    props.setProperty("offset.storage", FileOffsetBackingStore.class.getName());
    props.setProperty("offset.storage.file.filename", offsetStorageFileFilename);
    props.setProperty("offset.flush.interval.ms", Integer.toString(offsetFlushIntervalMs));

    props.setProperty("database.hostname", databaseConfig.getHostname());
    props.setProperty("database.port", databaseConfig.getPort());
    props.setProperty("database.user", databaseConfig.getUsername());
    props.setProperty("database.password", databaseConfig.getPassword());
    props.setProperty("database.server.name", databaseServerName);
    return props;
  }
}
