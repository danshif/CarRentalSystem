package carrentalmanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CarRentalController {

    public static ObservableList<Car> availableCarListData() {
        ObservableList<Car> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM CARS";

        Connection connect = database.connectDb();

        try {
            PreparedStatement prepare = connect.prepareStatement(sql);
            ResultSet result = prepare.executeQuery();

            Car car;
            while (result.next()) {
                car = new Car(result.getInt("id"),
                        result.getString("brand"),
                        result.getString("model"),
                        result.getString("status"),
                        result.getDouble("price"),
                        result.getDate("rentalStartDate"),
                        result.getDate("rentalEndDate")
                );

                listData.add(car);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }
}
