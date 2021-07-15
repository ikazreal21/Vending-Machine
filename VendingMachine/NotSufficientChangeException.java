package VendingMachine;

import javax.swing.*;

public class NotSufficientChangeException extends RuntimeException {
    private String message;

    public NotSufficientChangeException(String message) {
        this.message = message;
    }

    public void showErrorMessage() {
        JOptionPane.showMessageDialog(null,this.message,"NotFullSufficientChangeException", JOptionPane.ERROR_MESSAGE);
    }
}
