import java.time.LocalDateTime;

public class ParkingSpot {
  private int id;
  private Car car;
  private boolean occupied;
  private LocalDateTime timeIn;

  public ParkingSpot(int id) {
    this.id = id;
    this.occupied = false;
  }

  public Car getCar() {
    return this.car;
  }

  public boolean isOccupied() {
    return this.occupied;
  }

  public LocalDateTime getTimeIn() {
    return this.timeIn;
  }

  public void addCar(Car car) {
    this.car = car;
    this.occupied = true;
    this.timeIn = LocalDateTime.now();
  }

  public void removeCar() {
    this.occupied = false;
    this.car = null;
    this.timeIn = null;
  }

  public String toString() {
    String output = "";

    output += "ID: " + this.id + "\n";
    output += "Car: " + this.car;

    return output;
  }
}