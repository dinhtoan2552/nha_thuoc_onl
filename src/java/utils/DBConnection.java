package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // 👉 URL Railway MySQL
    private static final String URL =
            "jdbc:mysql://shinkansen.proxy.rlwy.net:59349/railway"
            + "?useSSL=false"
            + "&serverTimezone=Asia/Ho_Chi_Minh"
            + "&useUnicode=true"
            + "&characterEncoding=UTF-8"
            + "&allowPublicKeyRetrieval=true";

    // 👉 USER
    private static final String USER = "root";

    // ❗ THAY PASSWORD Ở ĐÂY (copy từ Railway → Connect → show)
    private static final String PASSWORD = "DAN_PASSWORD_RAILWAY_VAO_DAY";

    // 👉 Load driver
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ Load MySQL Driver thành công!");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ Không load được MySQL Driver!");
            e.printStackTrace();
        }
    }

    // 👉 Kết nối DB
    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Kết nối DB thành công!");
            return conn;
        } catch (SQLException e) {
            System.out.println("❌ Kết nối DB thất bại!");
            e.printStackTrace();
            return null;
        }
    }

    // 👉 Đóng kết nối (best practice)
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("🔌 Đã đóng kết nối DB.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}