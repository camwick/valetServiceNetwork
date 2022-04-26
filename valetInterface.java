import java.rmi.Remote;
import java.sql.SQLException;

public interface valetInterface extends Remote {

    public String addCar(int floor, String firstName, String lastName, String color, String make, String model)
            throws java.rmi.RemoteException;

    public String select(String firstName, String lastName) throws java.rmi.RemoteException;

    public String remove(String firstName, String lastName) throws java.rmi.RemoteException;

    public void quit() throws java.rmi.RemoteException, SQLException;

    public void test() throws java.rmi.RemoteException;
}