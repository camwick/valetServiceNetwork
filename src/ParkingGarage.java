public class ParkingGarage {
  private ParkingSpot[] garage;

  public ParkingGarage(int numParkingSpots) {
    this.garage = new ParkingSpot[numParkingSpots];

    for (int i = 0; i < numParkingSpots; ++i) {
      this.garage[i] = new ParkingSpot(i);
    }
  }

  public void addCar(Car car) {
    for (ParkingSpot spot : this.garage) {
      if (!spot.isOccupied()) {
        spot.addCar(car);
        break;
      }
    }
  }

  public void addCar(int x, Car car) {
    this.garage[x].addCar(car);
  }

  public void removeCar(String lastName, String firstName) {
    for (ParkingSpot spot : this.garage) {
      if (!spot.isOccupied())
        continue;
      else if (spot.getCar().getLastName().equals(lastName) && spot.getCar().getFirstName().equals(firstName)) {
        spot.removeCar();
        break;
      }
    }
  }

  public String toString() {
    String output = "";

    for (ParkingSpot spot : this.garage) {
      if (spot.isOccupied())
        output += spot + "\n\n";
    }

    return output;
  }
}