import config.DatabaseConfig;
import config.DebeziumEngineConfig;
import db.DBVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Launcher;
import io.vertx.reactivex.core.AbstractVerticle;
import java.sql.Connection;

public class MainVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher.executeCommand("run", MainVerticle.class.getName(), "--conf", args[0]);
  }

  @Override
  public void start() throws Exception {
    DatabaseConfig databaseConfig =
        new DatabaseConfig(
            config().getString("dbtype"),
            config().getString("hostname"),
            config().getInteger("port"),
            config().getString("username"),
            config().getString("password"),
            config().getString("dbname"));

    DebeziumEngineConfig debeziumEngineConfig =
        new DebeziumEngineConfig(
            "engine",
            DebeziumEngineConfig.getRandomOffsetStorageFileFilename(),
            1000,
            databaseConfig,
            "dbserver");
    Connection connection = databaseConfig.getConnection();

    String receiverName1 = "1";
    String receiverName2 = "2";
    String tableName1 = "foo";
    String tableName2 = "bar";
    String address = "address";

    vertx.deployVerticle(new DBVerticle(connection, tableName1, 1000));
    vertx.deployVerticle(new DBVerticle(connection, tableName2, 1000));

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

    vertx.deployVerticle(
        new DebeziumRecordSenderVerticle(debeziumEngineConfig, address),
        new DeploymentOptions().setWorker(true));
  }
}
