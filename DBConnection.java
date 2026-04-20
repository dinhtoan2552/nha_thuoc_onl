package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/banthuoc"
            + "?useSSL=false"
            + "&serverTimezone=Asia/Ho_Chi_Minh"
            + "&useUnicode=true"
            + "&characterEncoding=UTF-8"
            + "&autoReconnect=true";

    private static final String USER = "root";
    private static final String PASSWORD = "";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Da nap MySQL JDBC Driver thanh cong!");
        } catch (ClassNotFoundException e) {
            System.out.println("Khong tim thay MySQL JDBC Driver!");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Properties props = new Properties();
            props.setProperty("user", USER);
            props.setProperty("password", PASSWORD);

            conn = DriverManager.getConnection(URL, props);

            conn.setAutoCommit(true);
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            System.out.println("Ket noi database thanh cong!");
        } catch (SQLException e) {
            System.out.println("Ket noi database that bai!");
            e.printStackTrace();
        }
        return conn;
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                    System.out.println("Da dong ket noi database.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}