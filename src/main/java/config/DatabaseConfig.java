package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
  private final String hostname;
  private final String port;
  private final String username;
  private final String password;
  private final String dbname;

  public DatabaseConfig(
      String hostname, int port, String username, String password, String dbname) {
    this.hostname = hostname;
    this.port = Integer.toString(port);
    this.username = username;
    this.password = password;
    this.dbname = dbname;
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
    return String.format("jdbc:postgresql://%s:%s/%s", hostname, port, dbname);
  }

  public Connection getConnection() throws SQLException {
    return DriverManager.getConnection(getJdbcUrl(), getUsername(), getPassword());
  }
}
