import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.EventBus;
import io.vertx.reactivex.core.eventbus.Message;

public class RecordReceiverVerticle extends AbstractVerticle {

  private final String name;
  private final String address;
  private final Function<Message<?>, Boolean> messageFilter;
  private final Consumer<Message<?>> messageHandler;

  public RecordReceiverVerticle(
      String name,
      String address,
      Function<Message<?>, Boolean> messageFilter,
      Consumer<Message<?>> messageHandler) {
    this.name = name;
    this.address = address;
    this.messageFilter = messageFilter;
    this.messageHandler = messageHandler;
  }

  public static Function<Message<?>, Boolean> defaultMessageFilter(String tableName) {
    return message -> tableName.equals(getTableName(message));
  }

  public static Consumer<Message<?>> defaultMessageHandler(String name) {
    return message ->
        System.out.println(
            String.format("Receiver %s: Received news: %s", name, getTableName(message)));
  }

  private static String getTableName(Message<?> message) {
    JsonObject jsonMessage = new JsonObject((String) message.body());
    if (jsonMessage.containsKey("payload")) {
      return jsonMessage.getJsonObject("payload").getJsonObject("source").getString("table");
    }
    return null;
  }

  @Override
  public void start() {
    EventBus eventBus = vertx.eventBus();
    eventBus.consumer(address).toFlowable().filter(messageFilter::apply).subscribe(messageHandler);
    System.out.println(String.format("Receiver %s is ready!", name));
  }
}
