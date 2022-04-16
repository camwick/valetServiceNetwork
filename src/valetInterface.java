import java.rmi.Remote;

public interface valetInterface extends Remote {
  public int getPort() throws java.rmi.RemoteException;

  public String addCar(int floor, String firstName, String lastName, String color, String make, String model)
      throws java.rmi.RemoteException;

  public String select(String firstName, String lastName) throws java.rmi.RemoteException;

  public String remove(String firstName, String lastName) throws java.rmi.RemoteException;
}