package carrentalmanagementsystem;

import java.io.FileReader;
import java.sql.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class database {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:tcp://localhost/~/carrentelsystem";

    //  Database credentials
    static final String USER = "dshif";
    static final String PASS = "1234";

    public static Connection connectDb() {
        try {
            Class.forName(JDBC_DRIVER);
            return DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void initDatabase() {
        Connection conn = null;
        Statement stmt = null;
        JSONParser jsonParser = new JSONParser();
        try {
            conn = connectDb();
            stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT CASE WHEN EXISTS(SELECT 1 FROM CARS) THEN 0 ELSE 1 END AS IsEmpty");
            while (rs.next()) {
                // 0 = the cars table not empty
                if (rs.getInt(1) == 0) {
                    return;
                }
            }
//            stmt.executeUpdate("DROP TABLE IF EXISTS CARS");
            String sql = "CREATE TABLE IF NOT EXISTS CARS" +
                    "(id INTEGER not NULL, " +
                    " brand VARCHAR(255), " +
                    " model VARCHAR(255), " +
                    " price DECIMAL(10,2), " +
                    " status VARCHAR(255), " +
                    " rentalDateStart date, " +
                    " rentalDateEnd date, " +
                    " PRIMARY KEY ( id ))";
            stmt.executeUpdate(sql);
            String home = System.getProperty("user.home");
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(home + "/Documents/dev/CarRentalSystem/cars.json"));
            JSONArray jsonArray = (JSONArray) jsonObject.get("cars");
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO CARS values (?, ?, ?, ?, ?, ?, ?)");

            for (Object object : jsonArray) {
                JSONObject record = (JSONObject) object;
                long id = (long) record.get("id");
                String brand = (String) record.get("brand");
                String model = (String) record.get("model");
                double price = (double) record.get("price");
                String status = (String) record.get("status");
                long rentalStartDate = (long) record.get("rentalDateStart");
                long rentalEndDate = (long) record.get("rentalDateEnd");

                pstmt.setLong(1, id);
                pstmt.setString(2, brand);
                pstmt.setString(3, model);
                pstmt.setDouble(4, price);
                pstmt.setString(5, status);
                pstmt.setDate(6, new Date(rentalStartDate));
                pstmt.setDate(7, new Date(rentalEndDate));
                pstmt.executeUpdate();
            }

            System.out.println("CREATED CARS TABLE");
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            // Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
                try {
                    if (conn != null) conn.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        }
    }
}
