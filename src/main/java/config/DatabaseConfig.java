package config;

import io.vertx.core.json.JsonObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

  public static final String POSTGRES = "postgresql";
  public static final String MYSQL = "mysql";

  private final String dbType;
  private final String hostname;
  private final String port;
  private final String username;
  private final String password;
  private final String dbname;

  public DatabaseConfig(
      String dbType, String hostname, int port, String username, String password, String dbname) {
    this.dbType = dbType;
    this.hostname = hostname;
    this.port = Integer.toString(port);
    this.username = username;
    this.password = password;
    this.dbname = dbname;
  }

  public DatabaseConfig(JsonObject json) {
    this.dbType = json.getString("dbtype");
    this.hostname = json.getString("hostname");
    this.port = Integer.toString(json.getInteger("port"));
    this.username = json.getString("username");
    this.password = json.getString("password");
    this.dbname = json.getString("dbname");
  }

  public String getDbType() {
    return dbType;
  }

  public String getHostname() {
    return hostname;
  }

  public String getPort() {
    return port;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getDbname() {
    return dbname;
  }

  public String getJdbcUrl() {
    return String.format("jdbc:%s://%s:%s/%s", dbType, hostname, port, dbname);
  }

  public Connection getConnection() throws SQLException {
    return DriverManager.getConnection(getJdbcUrl(), getUsername(), getPassword());
  }
}
