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

    public String addCar(int floor, String firstName, String lastName, String color, String make, String model)
            throws RemoteException {
        String output = "";

        try {
            // create query
            PreparedStatement query = this.conn.prepareStatement(
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
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return output;
    }

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
            query.close();

            output += "Floor: " + result.getInt("floor");
            output += "Car: " + result.getString("color") + " " + result.getString("make") + " "
                    + result.getString("model");
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return output;
    }

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

    public void quit() throws RemoteException, SQLException {
        this.conn.close();
        System.exit(1);
    }

    public void test() throws RemoteException {
        System.out.println("This is a test.");
    }

    public static void main(String[] args) throws RemoteException, SQLException {
        valetImpl test = new valetImpl();
        System.out.println(test.addCar(2, "Cameron", "Wickersham", "white", "Honda", "Civic"));
        test.quit();
        System.out.println("test");
    }

}