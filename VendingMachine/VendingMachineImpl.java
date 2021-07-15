package VendingMachine;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class VendingMachineImpl implements VendingMachine {
    private Inventory<Coin> coinInventory = new Inventory<Coin>();
    private Inventory<Item> itemInventory = new Inventory<Item>();
    private List<Coin> balanceFragment=new ArrayList<>();
    private Bucket<Item, List<Coin>> bucket;
    private int change;
    private int balance;
    private int totalSales;
    private Item currentItem;

    public VendingMachineImpl() {
        initializeInventory(5);
        showMenu();
    }

    private void initializeInventory(int qty) {
        for (Coin c : Coin.values()) coinInventory.put(c, 0);
        for (Item i : Item.values()) itemInventory.put(i, qty);
    }

    @Override
    public void buyItem() {
        inputCoins("Insert");
        showItems();
    }

    @Override
    public void refill() {
        String selection = JOptionPane.showInputDialog(null, "Please choose:\n0. Items\n1. Coins",
                "Refill", JOptionPane.QUESTION_MESSAGE);
        String selectionItem = "";

        if (selection.equals("0")) {
            selectionItem = "Item";
            showRefillItem();
        } else if (selection.equals("1")) {
            selectionItem = "Coin";
            inputCoins("Refill");
        } else refill();

        JOptionPane.showMessageDialog(null, selectionItem + " successfully refilled!", "Message", JOptionPane.INFORMATION_MESSAGE);
        showMenu();
    }

    @Override
    public void displayStatistics() {
        JOptionPane.showMessageDialog(null, "ITEMS\n" +
                "Coke   (25)   - " + itemInventory.getQuantity(Item.COKE) + " pc(s)\n" +
                "Pepsi  (35)   - " + itemInventory.getQuantity(Item.PEPSI) + " pc(s)\n" +
                "Soda   (45)   - " + itemInventory.getQuantity(Item.SODA) + " pc(s)\n\n" +
                "\nCOINS\n" +
                "Penny    (1)    - " + coinInventory.getQuantity(Coin.PENNY) + " pc(s)\n" +
                "Nickel    (5)    - " + coinInventory.getQuantity(Coin.NICKEL) + " pc(s)\n" +
                "Dime      (10)  - " + coinInventory.getQuantity(Coin.DIME) + " pc(s)\n" +
                "Quarter (25)  - " + coinInventory.getQuantity(Coin.QUARTER) + " pc(s)\n\n" +
                "TOTAL SALES : Php " + totalSales, "Statistics", JOptionPane.INFORMATION_MESSAGE);

        showMenu();
    }

    @Override
    public void reset() {
        itemInventory.clear();
        coinInventory.clear();
        balanceFragment.clear();
        totalSales = 0;
        balance = 0;
        currentItem = null;
        initializeInventory(5);
    }

    @Override
    public Bucket<Item, List<Coin>> getOrderAndChange() {
        return new Bucket<>(currentItem, getChange());
    }

    @Override
    public List<Coin> refund() {
        List<Coin> refundAmount = balanceFragment.stream().collect(Collectors.toList());
        updateCoinInventory(refundAmount);
        balance = 0;
        balanceFragment.clear();
        change=0;
        currentItem=null;
        return refundAmount;
    }


    private void updateCoinInventory(List<Coin> coins) throws NotSufficientChangeException{

            for(Coin coin : coins){
                coinInventory.deduct(coin);
            }

    }

//    private boolean isChangeSufficient(Coin coin){
//        int total=0;
//
//        for(Coin c: Coin.values()){
//            if(c.getDenomination()<=coin.getDenomination())
//             total+= c.getDenomination()*coinInventory.getQuantity(c);
//        }
//        System.out.println(change + " "+total);
//        return total>=change;
//    }
//



    private int getTotalOfCoins(List<Coin> coins) {
        int result = 0;

        for (Coin coin : coins) {
            result += coin.getDenomination();
        }

        return result;
    }

    private List<Coin> getChange(){
        List<Coin> changeFragment = null;
        System.out.println("change: "+( balance - currentItem.getPrice())+" balance: "+balance+" item: "+currentItem.getName());
        try {
            change = balance - currentItem.getPrice();
            changeFragment = getChangeFragment(change);
            updateCoinInventory(changeFragment);
            totalSales += currentItem.getPrice();
            itemInventory.deduct(currentItem);
        }catch (NotSufficientChangeException sce){
            sce.showErrorMessage();
            showItems();
        }
        balance = 0;
        balanceFragment.clear();
        currentItem = null;

        return changeFragment;
    }

    private List<Coin> getChangeFragment(int amount) throws NotSufficientChangeException {

        List<Coin> changeFragment = Collections.EMPTY_LIST;
        if (amount > 0) {
            changeFragment = new ArrayList<>();

            Coin[] values = Coin.values();
            for (int i = values.length - 1; i >= 0; i--) {

                Coin c = values[i];

                    if (amount >= c.getDenomination() && coinInventory.hasItem(c) ) {
                        int pcs = (int) Math.floor(amount / c.getDenomination());
                        pcs = pcs <= coinInventory.getQuantity(c)? pcs : coinInventory.getQuantity(c);
                        for (int j = 0; j < pcs; j++)  changeFragment.add(c);

                        amount -= (c.getDenomination() * pcs);

                }

            }
        }

        if(amount>0) throw new NotSufficientChangeException("Insufficient coins in the inventory.\nPlease Try Again!");


        return changeFragment;
    }

    @Override
    public void showMenu() {
        String choice = JOptionPane.showInputDialog(null, "Balmonte's Vending Machine\n\n" +
                "MENU: \n" +
                "a. Buy item\n" +
                "b. Refill Items\n" +
                "c. Display Statistics\n" +
                "d. Reset\n\n");

        switch (choice + "") {
            case "a":
                buyItem();
                break;
            case "b":
                refill();
                break;
            case "c":
                displayStatistics();
                break;
            case "d":
                int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to reset the vending machine?",
                        "Reset", JOptionPane.OK_CANCEL_OPTION);
                if (confirmation == 0) {
                    reset();
                    JOptionPane.showMessageDialog(null, "Vending Machine successfully reset!");
                }
                showMenu();
                break;
            case "null":
            default:
                return;
        }
    }

    private void inputCoins(String operation) {
        for (Coin c : Coin.values()) {
            int qty = Integer.parseInt(JOptionPane.showInputDialog(null, operation + " your coins:\n" +
                    c + " (" + c.getDenomination() + ") : "));
            if (operation.equals("Insert")) balance += qty * c.getDenomination();

            coinInventory.add(c, qty);
            for(int i=0; i<qty; i++) balanceFragment.add(c);
        }
    }

    private void showItems() {

        String orderMsg = "";
        String title="order";

        int answer = Integer.parseInt(JOptionPane.showInputDialog(null, "Please select one of the options:\n" +
                "Balance: Php " + balance + "\n\n" +
                "0. Coke  - Php 25 - " + itemInventory.getQuantity(Item.COKE) + " available\n" +
                "1. Pepsi - Php 35 - " + itemInventory.getQuantity(Item.PEPSI) + " available\n" +
                "2. Soda  - Php 45 - " + itemInventory.getQuantity(Item.SODA) + " available\n" +
                "3. Refund\n\n"));

        switch (answer) {
            case 0:
                currentItem = Item.COKE;
                orderMsg = "Coke\nPhp 25.00\n";
                break;
            case 1:
                currentItem = Item.PEPSI;
                orderMsg = "Pepsi\nPhp 35.00\n";
                break;
            case 2:
                currentItem = Item.SODA;
                orderMsg = "Soda\nPhp 45.00\n";
                break;
            case 3:
                title="refund";
                orderMsg = "Are you sure you want to refund your cash?\n";
                break;
            default:
                JOptionPane.showMessageDialog(null, "Selection Invalid", "Error", JOptionPane.ERROR_MESSAGE);
                showItems();
                break;
        }

        int orderConfirmation = JOptionPane.showConfirmDialog(null, "Please confirm your selection.\n\n" +
                orderMsg + "Your balance is " + balance + "\n\n", "Confirm "+title, JOptionPane.CANCEL_OPTION);

        if (orderConfirmation == 0) {

            if (answer != 3) {

                try {
                    validateTransaction();
                    showSuccessfulTransaction();
                } catch (NotFullPaidException ne) {
                    ne.showErrorMessage();
                    int ans = JOptionPane.showConfirmDialog(null, "Do you want to add coins? ", "Insufficient Cash", JOptionPane.CANCEL_OPTION);
                    if (ans == 0) inputCoins("Insert");
                    showItems();
                } catch (SoldOutException se) {
                    se.showErrorMessage();
                    showItems();
                }

            } else {
                int moneyRefunded = getTotalOfCoins(refund());
                System.out.println("moneyRefunded: "+moneyRefunded);
                JOptionPane.showMessageDialog(null, "\nRefund Amount : " + moneyRefunded + "\n\n", "Refund Completed!", JOptionPane.INFORMATION_MESSAGE);
                showMenu();
            }

        } else {
            currentItem = null;
            showItems();
        }
    }

    private void validateTransaction() {

        if (itemInventory.getQuantity(currentItem) < 1) {
            throw new SoldOutException(currentItem.getName() + " is sold out!\nPlease try again!\n");
        }

        if (balance < currentItem.getPrice())
            throw new NotFullPaidException("Insufficient cash, remaining : Php " +
                    (currentItem.getPrice() - balance) + "\nPlease try again!\n\n");
    }

    private void showSuccessfulTransaction() {


            bucket = getOrderAndChange();
            Item item = bucket.getItem();

            JOptionPane.showMessageDialog(null, "Order: " + item.getName() +
                    "\nManufacturer: " + item.getManufacturer() +
                    "\nPrice: Php " + item.getPrice() +
                    "\nCash: Php " + (getTotalOfCoins(bucket.getChange()) + item.getPrice()) +
                    "\nChange: Php " + getTotalOfCoins(bucket.getChange()) + "\n\n" +
                    " Thank you for purchasing!\n" +
                    "Balmonte's Vending Machine\n", "Order has been Delivered!", JOptionPane.PLAIN_MESSAGE);

            showMenu();

    }

    private void showRefillItem() {
        for (Item i : Item.values()) {
            int qty = Integer.parseInt(JOptionPane.showInputDialog(null, "Refill your items:\n" +
                    i + " (" + i.getPrice() + ") : "));
            itemInventory.add(i, qty);
        }
    }


    public static void main(String[] args) {
        VendingMachine v = VendingMachineFactory.createVendingMachine();

    }
}