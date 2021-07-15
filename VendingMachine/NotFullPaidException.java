package VendingMachine;

import javax.swing.*;

public class NotFullPaidException extends RuntimeException {
    private String message;

    public NotFullPaidException(String message) {
        this.message=message;
    }

    public void showErrorMessage() {
        JOptionPane.showMessageDialog(null,this.message,"NotFullPaidException", JOptionPane.ERROR_MESSAGE);
    }
}
