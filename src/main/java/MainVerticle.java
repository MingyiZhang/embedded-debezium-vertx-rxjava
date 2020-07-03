import config.DatabaseConfig;
import config.DebeziumEngineConfig;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import java.sql.Connection;

public class MainVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    final Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());
  }

  @Override
  public void start() throws Exception {
    DatabaseConfig databaseConfig =
        new DatabaseConfig("localhost", 5432, "postgres", "postgres", "postgres");
    DebeziumEngineConfig debeziumEngineConfig =
        new DebeziumEngineConfig(
            "engine",
            DebeziumEngineConfig.setRandomOffsetStorageFileFilename(),
            1000,
            databaseConfig,
            "dbserver");
    Connection connection = databaseConfig.getConnection();

    vertx.deployVerticle(new PostgresVerticle(connection, "foo", 1000));
    vertx.deployVerticle(new PostgresVerticle(connection, "bar", 1000));
    vertx.deployVerticle(
        new DebeziumRecordSenderVerticle(
            debeziumEngineConfig.getDebeziumEngineProperties(), "address"));
  }
}
