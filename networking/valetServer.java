
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

  public static void main(String[] args) {
    valetServer garage = null;
    if (args.length == 2)
      garage = new valetServer(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
    else if (args.length == 0)
      garage = new valetServer();
    else {
      System.out.println("Need 2 command line arguments: number of floors, number of car on each floor");
      System.exit(1);
    }
  }
}