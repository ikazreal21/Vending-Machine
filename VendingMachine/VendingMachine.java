package VendingMachine;

import java.util.List;

public interface VendingMachine {

    void showMenu();
    void buyItem();
    void refill();
    void displayStatistics();
    void reset();
    Bucket<Item, List<Coin>> getOrderAndChange();
    List<Coin> refund();

}
