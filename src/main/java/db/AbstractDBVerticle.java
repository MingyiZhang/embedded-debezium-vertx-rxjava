package db;

import io.vertx.reactivex.core.AbstractVerticle;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class AbstractDBVerticle extends AbstractVerticle {
  protected final String tableName;
  protected final int delay;
  protected Connection connection;
  protected Statement statement;
  protected int count = 0;

  public AbstractDBVerticle(Connection connection, String tableName, int delay) throws SQLException {
    this.connection = connection;
    this.statement = this.connection.createStatement();
    this.tableName = tableName;
    this.delay = delay;
  }
}
