package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
        "jdbc:mysql://shinkansen.proxy.rlwy.net:59349/railway"
        + "?useSSL=false"
        + "&serverTimezone=Asia/Ho_Chi_Minh"
        + "&useUnicode=true"
        + "&characterEncoding=UTF-8";

    private static final String USER = "root";
    private static final String PASSWORD = "railway"; // thay password trong ảnh

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Load driver OK");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
