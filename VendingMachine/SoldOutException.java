package VendingMachine;

import javax.swing.*;

public class SoldOutException extends RuntimeException {
    private String message;

    public SoldOutException(String string) {
        this.message = string;
    }

    public void showErrorMessage() {
        JOptionPane.showMessageDialog(null,this.message,"SoldOutException", JOptionPane.ERROR_MESSAGE);
    }
}

