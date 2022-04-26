import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;

public class valetClient {
    public static void main(String[] args) {
        try {
            int RMIPort;
            String hostName;
            String portNum;
            InputStreamReader is = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(is);

            // obtaining RMI stuff
            System.out.println("Enter the RMI Registry Host Name:");
            hostName = br.readLine();
            System.out.println("Enter the RMI Registry Port Number:");
            portNum = br.readLine();
            RMIPort = Integer.parseInt(portNum);

            // grab remote object
            String registryURL = "rmi://" + hostName + ":" + portNum + "/valet";
            valetInterface h = (valetInterface) Naming.lookup(registryURL);

            // invoke methods here...
            System.out.println(h.test());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}