import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class valetImpl extends UnicastRemoteObject implements valetInterface {
  private int floorCount;
  private int maxCarPerFloor;
  private Connection conn = null;

  public valetImpl() throws RemoteException, SQLException {
    this(4, 50);
  }

  public valetImpl(int floorCount, int maxCarPerFloor) throws RemoteException, SQLException {
    super();
    this.floorCount = floorCount;
    this.maxCarPerFloor = maxCarPerFloor;
    this.conn = DriverManager.getConnection("jdbc:sqlite:./valetDB.db");
  }

  @Override
  public String addCar(int floor, String firstName, String lastName, String color, String make, String model)
      throws RemoteException {
    String output = "";

    if (floor > this.floorCount)
      return "Invalid floor number";

    try {
      PreparedStatement query = this.conn.prepareStatement("SELECT Count(floor) FROM Garage WHERE floor = ?");
      query.setInt(1, floor);
      ResultSet result = query.executeQuery();

      if (result.getInt("Count(floor)") >= this.maxCarPerFloor)
        return "Floor is full";

      // create query
      query = this.conn.prepareStatement(
          "INSERT INTO Garage (floor, firstName, lastName, color, make, model) VALUES (?, ?, ?, ?, ?, ?)");
      query.setInt(1, floor);
      query.setString(2, firstName);
      query.setString(3, lastName);
      query.setString(4, color);
      query.setString(5, make);
      query.setString(6, model);

      // run query
      query.executeUpdate();
      query.close();

      output += "Successfully added car";
      System.out.println("Added " + firstName + " " + lastName + "'s car to database.");
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return output;
  }

  @Override
  public String select(String firstName, String lastName) throws RemoteException {
    String output = "";

    // select floor and car
    try {
      // prepare query
      PreparedStatement query = this.conn
          .prepareStatement(
              "SELECT floor, color, make, model FROM Garage WHERE firstName = ? AND lastName = ?");
      query.setString(1, firstName);
      query.setString(2, lastName);

      // execute query
      ResultSet result = query.executeQuery();
      while (result.next()) {
        if (!result.isClosed()) {
          output += "Floor: " + result.getInt("floor") + "\n";
          output += "Car: " + result.getString("color") + " " + result.getString("make") + " "
              + result.getString("model") + "\n";
        } else
          output += "Name is not in database";
      }

      result.close();
      query.close();
    } catch (SQLException e) {
      System.out.println("test");
      e.printStackTrace();
    }

    return output;
  }

  @Override
  public String remove(String firstName, String lastName) throws RemoteException {
    String output = "";

    try {
      // prepare query
      PreparedStatement query = this.conn
          .prepareStatement("DELETE FROM Garage WHERE firstName = ? AND lastName = ?");
      query.setString(1, firstName);
      query.setString(2, lastName);

      // execute query
      query.executeUpdate();
      query.close();

      output += "Car removed from database";
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return output;
  }

  @Override
  public void quit() throws RemoteException, SQLException {
    this.conn.close();
  }

  @Override
  public String test() throws RemoteException {
    return "this is a test";
  }

  public static void main(String[] args) throws RemoteException, SQLException {
    valetImpl test = new valetImpl();
    System.out.println(test.addCar(2, "Cameron", "Wickersham", "white", "Honda", "Civic"));
    test.quit();
    System.out.println("test");
  }

}