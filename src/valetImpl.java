import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class valetImpl {
  private int floorCount;
  private int maxCarPerFloor;
  private int port;

  public valetImpl() {
    this(4, 50, 1234);
  }

  public valetImpl(int floorCount, int maxCarPerFloor, int port) {
    this.floorCount = floorCount;
    this.maxCarPerFloor = maxCarPerFloor;
    this.port = port;
  }

  public int getPort() {
    return this.port;
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
    valetImpl garage = null;
    if (args.length == 3) {
      garage = new valetImpl(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
    } else if (args.length == 2)
      garage = new valetImpl(Integer.parseInt(args[0]), Integer.parseInt(args[1]), 1234);
    else if (args.length == 0)
      garage = new valetImpl();
    else {
      System.out.println("Need 2 command line arguments: number of floors, number of car on each floor");
      System.exit(1);
    }

    ServerSocket serverSocket = null;
    Socket clientSocket = null;
    try {
      serverSocket = new ServerSocket(garage.getPort());
      clientSocket = serverSocket.accept();
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("Server connected");

    try {
      PrintWriter output = new PrintWriter(clientSocket.getOutputStream());
      BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

      // connection to database
      Connection conn = null;
      try {
        conn = DriverManager.getConnection("jdbc:sqlite:./valetDB.db");
      } catch (SQLException e) {
        e.printStackTrace();
      }

      // get command
      String response = input.readLine().trim();

      String[] names;
      while (!response.equals("done")) {
        response = input.readLine().trim();

        switch (response) {
          case "add":
            // get info to insert into database
            response = input.readLine().trim();

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
            response = input.readLine().trim();

            names = response.split(" ");

            select(conn, names[0], names[1]);

            break;
          case "remove":
            // get name
            response = input.readLine().trim();

            names = response.split(" ");

            // delete from database
            remove(conn, names[0], names[1]);

            break;
          case "select and remove":
            // get name
            response = input.readLine().trim();

            names = response.split(" ");

            // select floor and car
            select(conn, names[0], names[1]);

            // delete from database
            remove(conn, names[0], names[1]);

            break;
        }

        input.close();
        output.close();
        clientSocket.close();
        serverSocket.close();

        try {
          conn.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}