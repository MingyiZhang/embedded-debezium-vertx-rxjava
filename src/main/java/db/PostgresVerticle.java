package db;

import java.sql.Connection;
import java.sql.SQLException;

public class PostgresVerticle extends AbstractDBVerticle {

  public PostgresVerticle(Connection connection, String tableName, int delay) throws SQLException {
    super(connection, tableName, delay);
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
          } catch (SQLException throwables) {
            throwables.printStackTrace();
          }
        });
  }
}
