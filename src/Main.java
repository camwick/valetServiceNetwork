public class Main {
  public static void main(String[] args) {
    ParkingGarage garage = new ParkingGarage(200);

    garage.addCar(new Car("Cameron", "Wickersham", "white", "Honda", "Civic"));
    garage.addCar(10, new Car("Billy", "Bob", "black", "Honda", "Civic"));

    System.out.println(garage);

    garage.removeCar("Bob", "Billy");

    System.out.println(garage);
  }
}
