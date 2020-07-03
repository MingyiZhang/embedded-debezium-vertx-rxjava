import io.vertx.reactivex.core.AbstractVerticle;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgresVerticle extends AbstractVerticle {

  private final String alphaNumericString =
      "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";
  private final String tableName;
  private final int delay;
  private Connection connection;
  private Statement statement;
  private int count = 0;

  public PostgresVerticle(Connection connection, String tableName, int delay) throws SQLException {
    this.connection = connection;
    this.statement = this.connection.createStatement();
    this.tableName = tableName;
    this.delay = delay;
  }

  @Override
  public void start() throws Exception {
    statement.execute(String.format("drop table if exists %s", tableName));
    statement.execute(
        String.format(
            "create table %s (id int not null, title varchar(255), primary key (id))", tableName));
    vertx.setPeriodic(
        delay,
        event -> {
          try {
            String sql =
                String.format(
                    "insert into %s values (%d, '%s')",
                    tableName, count++, generateRandomString(8));
            statement.execute(sql);
          } catch (SQLException throwables) {
            throwables.printStackTrace();
          }
        });
  }

  private String generateRandomString(int size) {
    StringBuilder sb = new StringBuilder(size);
    for (int i = 0; i < size; i++) {
      int index = (int) (alphaNumericString.length() * Math.random());
      sb.append(alphaNumericString.charAt(index));
    }
    return sb.toString();
  }
}
