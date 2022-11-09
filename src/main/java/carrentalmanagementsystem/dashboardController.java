package carrentalmanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class dashboardController implements Initializable {

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button close;

    @FXML
    private Button minimize;

    @FXML
    private Button home_btn;

    @FXML
    private Button availableCars_btn;

    @FXML
    private Label home_allCars;

    @FXML
    private Label home_availableCars;

    @FXML
    private Label home_totalPriceCars;

    @FXML
    private BarChart<?, ?> home_incomeChart;

    @FXML
    private AnchorPane availableCars_form;

    @FXML
    private TextField availableCars_carId;

    @FXML
    private TextField availableCars_brand;

    @FXML
    private TextField availableCars_model;

    @FXML
    private ComboBox availableCars_status;

    @FXML
    private DatePicker rent_dateRented;

    @FXML
    private DatePicker rent_dateReturn;

    @FXML
    private Button availableCars_insertBtn;

    @FXML
    private Button availableCars_updateBtn;

    @FXML
    private Button availableCars_deleteBtn;

    @FXML
    private Button availableCars_clearBtn;

    @FXML
    private TextField availableCars_price;

    @FXML
    private TableView<Car> availableCars_tableView;

    @FXML
    private TableColumn<Car, String> availableCars_col_carId;

    @FXML
    private TableColumn<Car, String> availableCars_col_brand;

    @FXML
    private TableColumn<Car, String> availableCars_col_model;

    @FXML
    private TableColumn<Car, String> availableCars_col_price;

    @FXML
    private TableColumn<Car, String> availableCars_col_status;
    @FXML
    private TableColumn<Car, Date> availableCars_col_dateRented;

    @FXML
    private TableColumn<Car, Date> availableCars_col_dateReturn;

    @FXML
    private AnchorPane home_form;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Statement statement;

    public void homeAllCars() {
        String sql = "SELECT COUNT(id) FROM CARS";

        connect = database.connectDb();
        int countAC = 0;
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                countAC = result.getInt("COUNT(id)");
            }
            home_allCars.setText(String.valueOf(countAC));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void homeAvailableCars() {
        String sql = "SELECT COUNT(id) FROM CARS WHERE status = 'Available'";

        connect = database.connectDb();
        int countAC = 0;
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                countAC = result.getInt("COUNT(id)");
            }
            home_availableCars.setText(String.valueOf(countAC));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void homeSumPriceDayCars() {
        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        String sql = "SELECT PRICE FROM CARS";

        connect = database.connectDb();
        double sum = 0;
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                sum += result.getDouble("PRICE");
            }
            home_totalPriceCars.setText(String.valueOf(sum));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void homeIncomeChart() {

        home_incomeChart.getData().clear();

        String sql = "SELECT rentalDateStart, SUM(PRICE) FROM CARS GROUP BY rentalDateStart ORDER BY rentalDateStart ASC LIMIT 8";

        connect = database.connectDb();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            XYChart.Series chart = new XYChart.Series();
            while (result.next()) {
                if (result.getString(1) != null && result.getString(2) != null) {
                    chart.getData().add(new XYChart.Data(result.getString(1), result.getDouble(2)));
                }
            }
            home_incomeChart.getData().add(chart);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void availableCarAdd() {
        String sql = "INSERT INTO CARS (id, brand, model, price, status, rentalDateStart, rentalDateEnd) "
                + "VALUES(?,?,?,?,?,?,?)";

        connect = database.connectDb();

        try {
            Alert alert;

            if (availableCars_carId.getText().isEmpty()
                    || availableCars_brand.getText().isEmpty()
                    || availableCars_model.getText().isEmpty()
                    || availableCars_status.getSelectionModel().getSelectedItem() == null
                    || availableCars_price.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, availableCars_carId.getText());
                prepare.setString(2, availableCars_brand.getText());
                prepare.setString(3, availableCars_model.getText());
                prepare.setString(4, availableCars_price.getText());
                prepare.setString(5, (String) availableCars_status.getSelectionModel().getSelectedItem());
                ;
                prepare.setDate(6, java.sql.Date.valueOf(rent_dateRented.getValue()));
                prepare.setDate(7, java.sql.Date.valueOf(rent_dateReturn.getValue()));
                prepare.executeUpdate();

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Added!");
                alert.showAndWait();

                availableCarShowListData();
                availableCarClear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void availableCarUpdate() {
        String sql = "UPDATE CARS SET brand = '" + availableCars_brand.getText() + "', model = '"
                + availableCars_model.getText() + "', status ='"
                + availableCars_status.getSelectionModel().getSelectedItem() + "', price = '"
                + availableCars_price.getText() + "', rentalDateStart = '" + java.sql.Date.valueOf(rent_dateRented.getValue())
                + "', rentalDateEnd = '" + java.sql.Date.valueOf(rent_dateReturn.getValue()) + "' WHERE id = '" + availableCars_carId.getText() + "'";

        connect = database.connectDb();

        try {
            Alert alert;

            if (availableCars_carId.getText().isEmpty()
                    || availableCars_brand.getText().isEmpty()
                    || availableCars_model.getText().isEmpty()
                    || availableCars_status.getSelectionModel().getSelectedItem() == null
                    || availableCars_price.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE Car ID: " + availableCars_carId.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Updated!");
                    alert.showAndWait();

                    availableCarShowListData();
                    availableCarClear();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void availableCarDelete() {
        String sql = "DELETE FROM CARS WHERE id = '" + availableCars_carId.getText() + "'";

        connect = database.connectDb();

        try {
            Alert alert;
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to DELETE Car ID: " + availableCars_carId.getText() + "?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                statement = connect.createStatement();
                statement.executeUpdate(sql);

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Deleted");
                alert.showAndWait();

                availableCarShowListData();
                availableCarClear();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void availableCarClear() {
        availableCars_carId.setText("");
        availableCars_brand.setText("");
        availableCars_model.setText("");
        availableCars_status.getSelectionModel().clearSelection();
        availableCars_price.setText("");
        rent_dateRented.setValue(null);
        rent_dateReturn.setValue(null);
    }

    private final String[] listStatus = {"Available", "Not Available"};

    public void availableCarStatusList() {

        List<String> listS = new ArrayList<>();

        for (String data : listStatus) {
            listS.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listS);
        availableCars_status.setItems(listData);
    }

    public ObservableList<Car> availableCarListData() {

        ObservableList<Car> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM CARS";

        connect = database.connectDb();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            Car car;

            while (result.next()) {
                car = new Car(result.getInt("id"),
                        result.getString("brand"),
                        result.getString("model"),
                        result.getString("status"),
                        result.getDouble("price"),
                        result.getDate("rentalDateStart"),
                        result.getDate("rentalDateEnd"));
                listData.add(car);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<Car> availableCarList;

    public void availableCarShowListData() {
        availableCarList = availableCarListData();

        availableCars_col_carId.setCellValueFactory(new PropertyValueFactory<>("id"));
        availableCars_col_brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        availableCars_col_model.setCellValueFactory(new PropertyValueFactory<>("model"));
        availableCars_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        availableCars_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        availableCars_col_dateRented.setCellValueFactory(new PropertyValueFactory<>("rentalDateStart"));
        availableCars_col_dateReturn.setCellValueFactory(new PropertyValueFactory<>("rentalDateEnd"));

        availableCars_tableView.setItems(availableCarList);
    }

    public void availableCarSelect() {
        Car car = availableCars_tableView.getSelectionModel().getSelectedItem();
        int num = availableCars_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        availableCars_carId.setText(String.valueOf(car.getId()));
        availableCars_brand.setText(car.getBrand());
        availableCars_model.setText(car.getModel());
        availableCars_price.setText(String.valueOf(car.getPrice()));
        rent_dateRented.setValue(car.getRentalDateStart() != null ? car.getRentalDateStart().toLocalDate() : null);
        rent_dateReturn.setValue(car.getRentalDateEnd() != null ? car.getRentalDateEnd().toLocalDate() : null);
    }

    public void switchForm(ActionEvent event) {

        if (event.getSource() == home_btn) {
            home_form.setVisible(true);
            availableCars_form.setVisible(false);

            home_btn.setStyle("-fx-background-color:#364156");
            availableCars_btn.setStyle("-fx-background-color:transparent");

            homeAllCars();
            homeAvailableCars();
            homeIncomeChart();

        } else if (event.getSource() == availableCars_btn) {
            home_form.setVisible(false);
            availableCars_form.setVisible(true);

            availableCars_btn.setStyle("-fx-background-color:#364156");
            home_btn.setStyle("-fx-background-color:transparent");

            availableCarShowListData();
            availableCarStatusList();
        }
    }

    public void close() {
        System.exit(0);
    }

    public void minimize() {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        homeAllCars();
        homeAvailableCars();
        homeSumPriceDayCars();
        homeIncomeChart();

        availableCarShowListData();
        availableCarStatusList();
    }
}
