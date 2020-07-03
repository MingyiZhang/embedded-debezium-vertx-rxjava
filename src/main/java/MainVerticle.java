import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class MainVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    final Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());
  }

  @Override
  public void start() throws Exception {
    Properties props = DebeziumRecordSenderVerticle.getEngineProperties(new Properties());
    DebeziumRecordSenderVerticle debeziumRecordSenderVerticle =
        new DebeziumRecordSenderVerticle(props, "address");
    Connection connection =
        DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
    vertx.deployVerticle(new PostgresVerticle(connection, "foo", 1000));
    vertx.deployVerticle(new PostgresVerticle(connection, "bar", 1000));
    vertx.deployVerticle(debeziumRecordSenderVerticle);
  }
}
