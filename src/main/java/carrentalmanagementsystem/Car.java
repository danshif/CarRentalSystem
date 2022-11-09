package carrentalmanagementsystem;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Objects;

public class Car {
    private final int id;
    private String brand;
    private String model;
    private String status;
    private Double price;
    private Date rentalDateStart;
    private Date rentalDateEnd;

    public Car(int id, String brand, String model, String status, Double price, Date rentalDateStart, Date rentalDateEnd) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.status = status;
        this.price = price;
        this.rentalDateStart = rentalDateStart;
        this.rentalDateEnd = rentalDateEnd;
    }

    public int getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getRentalDateStart() {
        return rentalDateStart;
    }

    public void setRentalDateStart(Date rentalDateStart) {
        this.rentalDateStart = rentalDateStart;
    }

    public Date getRentalDateEnd() {
        return rentalDateEnd;
    }

    public void setRentalDateEnd(Date rentalDateEnd) {
        this.rentalDateEnd = rentalDateEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return id == car.id
                && Objects.equals(brand, car.brand)
                && Objects.equals(model, car.model)
                && Objects.equals(status, car.status)
                && Objects.equals(price, car.price)
                && Objects.equals(rentalDateStart, car.rentalDateStart)
                && Objects.equals(rentalDateEnd, car.rentalDateEnd);
    }
    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", status='" + status + '\'' +
                ", price='" + price + '\'' +
                ", rentalDateStart=" + rentalDateStart +
                ", rentalDateEnd=" + rentalDateEnd +
                '}';
    }
}
