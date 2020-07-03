import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import io.vertx.reactivex.core.AbstractVerticle;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DebeziumRecordSenderVerticle extends AbstractVerticle {

  private DebeziumEngine<ChangeEvent<String, String>> debeziumEngine;

  public DebeziumRecordSenderVerticle(Properties props, String address) {
    System.out.println(props);
    debeziumEngine =
        DebeziumEngine.create(Json.class).using(props).notifying(System.out::println).build();
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
