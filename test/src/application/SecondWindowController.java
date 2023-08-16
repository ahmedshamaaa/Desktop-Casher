package application;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.SQLException;
import java.time.LocalDateTime;
public class SecondWindowController {

	
	   @FXML
	   public TextField cashAmountField;
	    @FXML
	    public Label invoiceTotalLabel;
	    @FXML
	    public Label changeLabel;
	    @FXML
	    public Button finalizeButton;

	    public double invoiceTotal; // Total amount for the invoice
	    public OnInvoiceFinalizedListener onInvoiceFinalizedListener;

	    public void initialize(double total) {
	        invoiceTotal = total;
	        invoiceTotalLabel.setText(String.format("%.2f EGP", invoiceTotal));
	        setupCashAmountListener();
	    }

	    @FXML
	    private void onCashAmountEntered() {
	    	updateTotalAndChangeLabels();
	    }

	    @FXML
	    private void onFinalizeButtonClicked() {
	        finalizeInvoice();
	    }

	    public void updateTotalAndChangeLabels() {
	    	 double total = calculateInvoiceTotal();
	         invoiceTotalLabel.setText(String.format("%.2f EGP", total));
	         
	         try {
	             double cashAmount = Double.parseDouble(cashAmountField.getText());
	             double change = cashAmount - total;
	             changeLabel.setText(String.format("%.2f EGP", change));
	             finalizeButton.setDisable(change < 0);
	         } catch (NumberFormatException e) {
	             changeLabel.setText("N/A");
	             finalizeButton.setDisable(true);
	         }
	    }

	    public void finalizeInvoice() {
	        if (onInvoiceFinalizedListener != null) {
	            onInvoiceFinalizedListener.onInvoiceFinalized();
	        }
	        closeWindow();
	    }
	    
	    

	    public void setOnInvoiceFinalizedListener(OnInvoiceFinalizedListener listener) {
	        this.onInvoiceFinalizedListener = listener;
	    }

	    private void closeWindow() {
	        Stage stage = (Stage) cashAmountField.getScene().getWindow();
	        stage.close();
	    }

    private void setupCashAmountListener() {
        cashAmountField.textProperty().addListener((observable, oldValue, newValue) -> {
        	updateTotalAndChangeLabels();
        });
    }
    
    
    public void insertInvoiceIntoDatabase(int currentInvoiceNumber, ObservableList<Item> invoiceItems) throws SQLException {
        Connection connection = DatabaseHelper.getConnection();
        try (connection) {
            // Disable auto-commit mode
            connection.setAutoCommit(false);

            // Insert invoice
            String insertInvoiceQuery = "INSERT INTO Invoices (InvoiceNumber, InvoiceTime, PaidAmount, InvoiceTotal) VALUES (?, ?, ?, ?)";
            PreparedStatement invoiceStatement = connection.prepareStatement(insertInvoiceQuery);

            // Check if cashAmountField is empty or not
            double paidAmount = cashAmountField.getText().isEmpty() ? 0.0 : Double.parseDouble(cashAmountField.getText());
            invoiceStatement.setInt(1, currentInvoiceNumber);
            invoiceStatement.setObject(2, LocalDateTime.now());
            invoiceStatement.setDouble(3, paidAmount);

            // Check if invoiceItems is empty or not
            double invoiceTotal = invoiceItems.isEmpty() ? 0.0 : calculateInvoiceTotal();
            invoiceStatement.setDouble(4, invoiceTotal);

            invoiceStatement.executeUpdate();
            invoiceStatement.close();

            // Insert invoice details
            String insertDetailsQuery = "INSERT INTO InvoiceDetails (InvoiceNumber, InvoiceTime, ItemIndex, ItemID, ItemPrice) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement detailsStatement = connection.prepareStatement(insertDetailsQuery);
            for (int i = 0; i < invoiceItems.size(); i++) {
                detailsStatement.setInt(1, currentInvoiceNumber);
                detailsStatement.setObject(2, LocalDateTime.now());
                detailsStatement.setInt(3, i + 1); // Item index starts from 1
                detailsStatement.setString(4, invoiceItems.get(i).getItemID());
                detailsStatement.setDouble(5, invoiceItems.get(i).getItemPrice());
                detailsStatement.executeUpdate();
            }
            detailsStatement.close();

            // Commit the transaction
            connection.commit();


            
            // Rollback the transaction in case of an error
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private double calculateInvoiceTotal() {
        double total = 0.0;
        for (Item item : Controller.invoiceItems) {
            total += item.getItemPrice();
        }
        return total;
    }



}
