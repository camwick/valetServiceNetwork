import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class valetServer {
  private int floorCount;
  private int maxCarPerFloor;

  public valetServer() {
    this(4, 50);
  }

  public valetServer(int floorCount, int maxCarPerFloor) {
    this.floorCount = floorCount;
    this.maxCarPerFloor = maxCarPerFloor;
  }

  private static void select(Connection conn, String firstName, String lastName) {
    // select floor and car
    try {
      // prepare query
      PreparedStatement query = conn
          .prepareStatement("SELECT floor, color, make, model FROM Garage WHERE firstName = ? AND lastName = ?");
      query.setString(1, firstName);
      query.setString(2, lastName);

      // execute query
      ResultSet result = query.executeQuery();

      System.out.println("Floor: " + result.getInt("floor"));
      System.out.println(
          "Car: " + result.getString("color") + " " + result.getString("make") + " " + result.getString("model"));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private static void remove(Connection conn, String firstName, String lastName) {
    try {
      // prepare query
      PreparedStatement query = conn
          .prepareStatement("DELETE FROM Garage WHERE firstName = ? AND lastName = ?");
      query.setString(1, firstName);
      query.setString(2, lastName);

      // execute query
      query.executeUpdate();

      System.out.println("Car removed from database");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    // creating garage object
    valetServer garage = null;
    if (args.length == 2)
      garage = new valetServer(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
    else if (args.length == 0)
      garage = new valetServer();
    else {
      System.out.println("Need 2 command line arguments: number of floors, number of car on each floor");
      System.exit(1);
    }

    // connection to database
    Connection conn = null;
    try {
      conn = DriverManager.getConnection("jdbc:sqlite:./valetDB.db");
    } catch (SQLException e) {
      e.printStackTrace();
    }

    System.out.println("What would you like to do?");
    Scanner input = new Scanner(System.in);

    String response = "";
    String[] names;
    while (!response.equals("done")) {
      response = input.nextLine().trim();

      switch (response) {
        case "add":
          // get info to insert into database
          System.out.println("Ready for info");
          response = input.nextLine().trim();

          String[] info = response.split(" ");

          try {
            // create query
            PreparedStatement query = conn.prepareStatement(
                "INSERT INTO Garage (floor, firstName, lastName, color, make, model) VALUES (?, ?, ?, ?, ?, ?)");
            query.setInt(1, Integer.parseInt(info[0]));
            for (int i = 1; i < info.length; ++i) {
              query.setString(i + 1, info[i]);
            }

            // run query
            query.executeUpdate();
          } catch (SQLException e) {
            e.printStackTrace();
          }

          break;
        case "select":
          // get name
          System.out.print("First and last name: ");
          response = input.nextLine().trim();

          names = response.split(" ");

          select(conn, names[0], names[1]);

          break;
        case "remove":
          // get name
          System.out.print("First and last name: ");
          response = input.nextLine().trim();

          names = response.split(" ");

          // delete from database
          remove(conn, names[0], names[1]);

          break;
        case "select and remove":
          // get name
          System.out.print("First and last name: ");
          response = input.nextLine().trim();

          names = response.split(" ");

          // select floor and car
          select(conn, names[0], names[1]);

          // delete from database
          remove(conn, names[0], names[1]);

          break;
      }
    }

    input.close();

    try {
      conn.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}