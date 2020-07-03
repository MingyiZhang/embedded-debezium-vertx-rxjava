import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import io.vertx.reactivex.core.AbstractVerticle;
import java.util.OptionalInt;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DebeziumRecordSenderVerticle extends AbstractVerticle {

  private DebeziumEngine<ChangeEvent<String, String>> debeziumEngine;

  public DebeziumRecordSenderVerticle(Properties props, String address) {
    System.out.println(props);
    debeziumEngine =
        DebeziumEngine.create(Json.class).using(props).notifying(System.out::println).build();
  }

  public static Properties getEngineProperties(Properties databaseConfig) {
    Properties props = new Properties();
    props.setProperty("name", "engine");
    props.setProperty("connector.class", "io.debezium.connector.postgresql.PostgresConnector");
    props.setProperty("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore");
    props.setProperty("offset.storage.file.filename", setOffsetStorageFilename());
    props.setProperty("offset.flush.interval.ms", "1000");

    props.setProperty("database.hostname", databaseConfig.getProperty("hostname", "localhost"));
    props.setProperty("database.port", databaseConfig.getProperty("port", "5432"));
    props.setProperty("database.user", databaseConfig.getProperty("username", "postgres"));
    props.setProperty("database.password", databaseConfig.getProperty("password", "postgres"));
    props.setProperty("database.dbname", databaseConfig.getProperty("dbname", "postgres"));
    props.setProperty("database.server.name", "dbserver");
    return props;
  }

  private static String setOffsetStorageFilename() {
    Random random = new Random();
    OptionalInt optionalInt = random.ints().findFirst();
    while (optionalInt.isEmpty()) {
      optionalInt = random.ints().findFirst();
    }
    return String.format("/tmp/offsets-%d.dat", optionalInt.getAsInt());
  }

  @Override
  public void start() throws Exception {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.execute(debeziumEngine);
  }

  @Override
  public void stop() throws Exception {
    debeziumEngine.close();
  }
}
