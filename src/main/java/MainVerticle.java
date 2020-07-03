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

    String receiverName1 = "1";
    String receiverName2 = "2";
    String tableName1 = "foo";
    String tableName2 = "bar";
    String address = "address";

    vertx.deployVerticle(new PostgresVerticle(connection, tableName1, 1000));
    vertx.deployVerticle(new PostgresVerticle(connection, tableName2, 1000));

    vertx.deployVerticle(
        new RecordReceiverVerticle(
            receiverName1,
            address,
            RecordReceiverVerticle.defaultMessageFilter(tableName1),
            RecordReceiverVerticle.defaultMessageHandler(receiverName1)));
    vertx.deployVerticle(
        new RecordReceiverVerticle(
            receiverName2,
            address,
            RecordReceiverVerticle.defaultMessageFilter(tableName2),
            RecordReceiverVerticle.defaultMessageHandler(receiverName2)));

    vertx.deployVerticle(new DebeziumRecordSenderVerticle(debeziumEngineConfig, address));
  }
}
