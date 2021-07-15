package VendingMachine;

import java.util.List;

public class Bucket <Item, Change>{
    private Item item;
    private Change change;
    public Bucket(Item item, Change change){
        this.item=item;
        this.change=change;
    }

    public Item getItem(){
        return this.item;
    }

    public Change getChange() {
        return change;
    }
}
