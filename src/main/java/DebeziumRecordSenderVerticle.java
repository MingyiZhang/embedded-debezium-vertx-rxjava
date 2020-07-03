import config.DebeziumEngineConfig;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.EventBus;
import io.vertx.reactivex.core.eventbus.MessageProducer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DebeziumRecordSenderVerticle extends AbstractVerticle {

  private final DebeziumEngineConfig debeziumEngineConfig;
  private final String address;

  public DebeziumRecordSenderVerticle(DebeziumEngineConfig debeziumEngineConfig, String address) {
    this.debeziumEngineConfig = debeziumEngineConfig;
    this.address = address;
  }

  @Override
  public void start() throws Exception {
    EventBus eventBus = vertx.eventBus();
    MessageProducer<String> producer = eventBus.publisher(address);
    DebeziumEngine<ChangeEvent<String, String>> debeziumEngine =
        DebeziumEngine.create(Json.class)
            .using(debeziumEngineConfig.getDebeziumEngineProperties())
            .notifying(
                record -> {
                  producer.write(record.value());
                })
            .build();

    ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.execute(debeziumEngine);
  }
}
