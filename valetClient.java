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
      valetInterface valet = (valetInterface) Naming.lookup(registryURL);

      // invoke methods here...
      boolean loop = true;
      while (loop) {
        System.out.println("\nWhat would you to do?");
        System.out.println("1) Add a car to the DB\n2) View entry\n3) Remove a car from the DB\n4) Quit");

        String response = br.readLine().trim();
        String[] queryInfo;
        switch (Integer.parseInt(response)) {
          case 1:
            System.out.println("Supply:\n- Floor Number\n- First Name\n- Last Name\n- Color of Car\n- Make & Model");

            response = br.readLine().trim();
            queryInfo = response.split(" ");
            response = valet.addCar(Integer.parseInt(queryInfo[0]), queryInfo[1], queryInfo[2], queryInfo[3],
                queryInfo[4],
                queryInfo[5]);
            System.out.println(response.trim());
            break;
          case 2:
            System.out.print("Enter first and last name: ");
            response = br.readLine().trim();
            queryInfo = response.split(" ");
            response = valet.select(queryInfo[0], queryInfo[1]);
            System.out.println(response);
            break;
          case 3:
            System.out.print("Enter first and last name: ");
            response = br.readLine().trim();
            queryInfo = response.split(" ");
            response = valet.remove(queryInfo[0], queryInfo[1]);
            System.out.println(response);
            break;
          case 4:
            valet.quit();
            loop = false;
            break;
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}