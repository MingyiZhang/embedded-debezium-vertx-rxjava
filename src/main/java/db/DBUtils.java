package db;

public class DBUtils {
  private static final String ALPHA_NUMERIC_STRING =
      "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";

  public static String generateRandomString(int size) {
    StringBuilder sb = new StringBuilder(size);
    for (int i = 0; i < size; i++) {
      int index = (int) (ALPHA_NUMERIC_STRING.length() * Math.random());
      sb.append(ALPHA_NUMERIC_STRING.charAt(index));
    }
    return sb.toString();
  }
}
