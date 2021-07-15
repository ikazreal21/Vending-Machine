package VendingMachine;

public enum Item  {
    COKE("Coke",25,"The Coca-Cola Company"),
    PEPSI("Pepsi",35,"Pepsi.Co"),
    SODA("Soda",45,"The Coca-Cola Company");

    private String name;
    private final int price;
    private String manufacturer;

    Item(String name,int price,String manufacturer){
        this.name=name;
        this.price=price;
        this.manufacturer=manufacturer;
    }

    public int getPrice(){
        return this.price;
    }

    public String getName() {
        return this.name;
    }
    public String getManufacturer(){
        return this.manufacturer;
    }
}
