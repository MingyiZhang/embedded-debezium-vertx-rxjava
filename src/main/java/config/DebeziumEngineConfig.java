package config;

import java.util.OptionalInt;
import java.util.Properties;
import java.util.Random;

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

  public static String setRandomOffsetStorageFileFilename() {
    Random random = new Random();
    OptionalInt optionalInt = random.ints().findFirst();
    while (optionalInt.isEmpty()) {
      optionalInt = random.ints().findFirst();
    }
    return String.format("/tmp/offsets-%d.dat", optionalInt.getAsInt());
  }

  public Properties getDebeziumEngineProperties() {
    Properties props = new Properties();
    props.setProperty("name", name);
    props.setProperty("connector.class", "io.debezium.connector.postgresql.PostgresConnector");
    props.setProperty("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore");
    props.setProperty("offset.storage.file.filename", offsetStorageFileFilename);
    props.setProperty("offset.flush.interval.ms", Integer.toString(offsetFlushIntervalMs));

    props.setProperty("database.hostname", databaseConfig.getHostname());
    props.setProperty("database.port", databaseConfig.getPort());
    props.setProperty("database.user", databaseConfig.getUsername());
    props.setProperty("database.password", databaseConfig.getPassword());
    props.setProperty("database.dbname", databaseConfig.getDbname());
    props.setProperty("database.server.name", databaseServerName);
    return props;
  }
}
