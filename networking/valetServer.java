import java.rmi.registry.Registry;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class valetServer {
    /**
     * Creates registry
     * 
     * @param RMIPortNum int, port number of registry
     * @throws RemoteException
     */
    private static void startRegistry(int RMIPortNum) throws RemoteException {
        try {
            // this will throw an error if the registry does not exist
            Registry registry = LocateRegistry.getRegistry(RMIPortNum);
            registry.list();
        } catch (RemoteException e) {
            // Creates the registry
            System.out.println("RMI registry cannot be located at port " + RMIPortNum);
            Registry registry = LocateRegistry.createRegistry(RMIPortNum);
            System.out.println("RMI registry created at port " + RMIPortNum);
        }
    }

    public static void main(String[] args) {
        InputStreamReader is = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(is);
        String portNum, registryURL;

        try {
            // get port number
            System.out.println("Enter the RMI Registry port number:");
            portNum = br.readLine();
            int RMIPortNum = Integer.parseInt(portNum);

            // startregistry
            valetImpl exportedObj = new valetImpl();
            startRegistry(RMIPortNum);

            // register object under name 'valet'
            registryURL = "rmi://localhost:" + portNum + "/valet";
            Naming.rebind(registryURL, exportedObj);
            System.out.println("Valet server is ready.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
