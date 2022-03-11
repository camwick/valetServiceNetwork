public class Car{
    private String firstName, lastName, color, make, model;
    //maybe the year of the vehicle as well
    private byte tier;

    public Car(String firstName, String lastName, String color, String make, String model, byte tier){
        this.firstName = firstName;
        this.lastName = lastName;
        this.color = color;
        this.make = make;
        this.model = model;
        this.tier = tier;
    }

    public String getColor(){
        return this.color;
    }

    public String getMake(){
        return this.make;
    }

    public String getModel(){
        return this.model;
    }

    public void setColor(String c){
        this.color = c;
    }

    public void setMake(String m){
        this.make = m;
    }

    public void setModel(String mo){
        this.model = mo;
    }

    public String toString(){
        return this.color + " " + this.make + " " + this.model + " owned by " + this.firstName + " " + this.lastName + ".";
    }
}