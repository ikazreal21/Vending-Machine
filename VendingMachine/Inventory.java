package VendingMachine;


import java.util.Map;
import java.util.HashMap;

public class Inventory<T> {

    private Map<T, Integer> inventory=new HashMap<T,Integer>();

    public void add(T item, int quantity) {
        if (quantity > 0) {
            Integer temp=inventory.get(item);
            int oldQty = temp == null ? 0 : temp;
            inventory.put(item,quantity+oldQty);
            return;
        }

    }

    public int getQuantity(T item) {
        return inventory.get(item)==null?0:inventory.get(item);
    }

    public void deduct(T item) {

        if(hasItem(item)){
            int qty=inventory.get(item);
            inventory.replace(item,qty-1);
        }

    }

    public boolean hasItem(T item){
        return inventory.get(item)>0;
    }

    public void clear(){
        inventory.clear();
    }

    public void put(T item, int quantity) {
        inventory.put(item, quantity);
    }


}
