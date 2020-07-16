package db;

import io.vertx.reactivex.core.AbstractVerticle;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBVerticle extends AbstractVerticle {

  private final String tableName;
  private final int delay;
  private final Statement statement;
  private int count = 0;

  public DBVerticle(Connection connection, String tableName, int delay) throws SQLException {
    this.statement = connection.createStatement();
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
                    tableName, count++, DBUtils.generateRandomString(8));
            statement.execute(sql);
          } catch (SQLException e) {
            e.printStackTrace();
          }
        });
  }
}
