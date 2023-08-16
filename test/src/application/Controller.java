package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Controller {
	
	private SecondWindowController secondWindowController;

    @FXML
    private Label invoiceNumberLabel;
    @FXML
    private ListView<Item> itemList;
    @FXML
    private TextField barcodeField;

    public static ObservableList<Item> invoiceItems = FXCollections.observableArrayList();
    private int currentInvoiceNumber = 1;

    // Initialize method for setup
    public void initialize() {
        itemList.setItems(invoiceItems);
        updateInvoiceNumberLabel();
        setupBarcodeFieldListener();

        itemList.setCellFactory(param -> new ListCell<Item>() {
            @Override
            protected void updateItem(Item item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getItemName()); // Set the text of the cell to the item name
                }
            }
        });
    }

    @FXML
    private void openSecondWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SecondWindowFXML.fxml"));
            Parent root = loader.load();
            secondWindowController = loader.getController(); // Use the instance variable
            secondWindowController.initialize(calculateInvoiceTotal());
            secondWindowController.setOnInvoiceFinalizedListener(this::handleInvoiceFinalized);
            Stage secondStage = new Stage();
            secondStage.setTitle("Second Window");
            secondStage.setScene(new Scene(root));
            secondStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

/////////////////////####################################################################
/////////////////////####################################################################
    private void handleInvoiceFinalized() {
        // Perform database update and UI clearing here
    	
    	
        try {
        	secondWindowController.insertInvoiceIntoDatabase(currentInvoiceNumber, invoiceItems);
            showAlertt("Success", "Invoice has been successfully inserted into the database.", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while inserting the invoice into the database.");
        }
        
        // Create a invoice window show its details
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Finalize Invoice");
        dialog.setHeaderText("Invoice Details");
        
        VBox dialogContent = new VBox();
        dialogContent.getChildren().addAll(
            new Label("Invoice Number: " + currentInvoiceNumber),
            new Label("Total: " + calculateInvoiceTotal() + " EGP"),
            new Label("Cash Amount: " + secondWindowController.cashAmountField.getText() + " EGP"),
            new Label("Change: " + secondWindowController.changeLabel.getText())
        );
        dialog.getDialogPane().setContent(dialogContent);

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(okButtonType);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                // This callback will be executed when you click the "OK" button
                // You can put any additional logic here if needed
            }
            return null;
        });

        dialog.showAndWait();
        
        printInvoiceDetails();
        
        // Clear invoice items and reset UI
        invoiceItems.clear();
        itemList.getItems().clear();
        barcodeField.clear();
        secondWindowController.updateTotalAndChangeLabels();
        currentInvoiceNumber++;
        updateInvoiceNumberLabel();
    }
    LocalTime currentTime = LocalTime.now();
    public int  hourr = currentTime.getHour();
    public int minutee = currentTime.getMinute();
    public int secondd = currentTime.getSecond();
    
    LocalDate currentDate = LocalDate.now();
    int year = currentDate.getYear();
    int month = currentDate.getMonthValue();
    int day = currentDate.getDayOfMonth();


    
    private Node createInvoicePrintPage() {
        int numberOfItems = invoiceItems.size();
        List<String> individualNames = new ArrayList<>();
        
        for (int i = 0; i < numberOfItems; i++) {
            individualNames.add(itemList.getItems().get(i).getItemName());
        }
        
        // Merge individual names into a single string
        String mergedNames = String.join(",", individualNames);
        
        Map<String, Integer> itemCounts = new HashMap<>();
        
        for (String item : individualNames) {
            itemCounts.put(item, itemCounts.getOrDefault(item, 0) + 1);
        }
        
        
        // New map to store item prices
        Map<String, Double> itemPrices = new HashMap<>();

        // Database connection and query
        String url = "jdbc:sqlite:path_to_your_database.db";
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement()) {

            for (String item : individualNames) {
                String query = "SELECT ItemPrice FROM Items WHERE ItemName = '" + item + "'";
                try (ResultSet rs = stmt.executeQuery(query)) {
                    if (rs.next()) {
                        double price = rs.getDouble("ItemPrice");
                        itemPrices.put(item, price);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // New map to store item totals (quantity * price)
        Map<String, Double> itemTotals = new HashMap<>();

        // Calculate item totals and store in the new map
        for (Map.Entry<String, Integer> entry : itemCounts.entrySet()) {
            String itemName = entry.getKey();
            int itemCount = entry.getValue();
            double itemPrice = itemPrices.getOrDefault(itemName, 0.0);
            double itemTotal = itemCount * itemPrice;
            itemTotals.put(itemName, itemTotal);
        }

     
        

        VBox printContent = new VBox();
        
        printContent.getChildren().addAll(
        	new Label("*****************************"),
            new Label("AHMED SHAMAA MARKET"),
            new Label("*****************************"),
            new Label("Invoice Number: " + currentInvoiceNumber),
            new Label("Time: " + hourr + ":" + minutee + ":" + secondd),
            new Label("Date: " + year + "/" + month + "/" + day),
            new Label("Items quantity: "+itemCounts),
            new Label("Items price"+itemPrices),
            new Label("Item total price"+itemTotals),
            new Label("____________________________________"),
            new Label("Total Amount: " + calculateInvoiceTotal() + " EGP"),
            new Label("Cash Amount: " + secondWindowController.cashAmountField.getText() + " EGP"),
            new Label("Change Amount: " + secondWindowController.changeLabel.getText()),
            new Label("********************"),
            new Label("THANK YOU"),
            new Label("********************")
            
            
        );
        
        return printContent;
    }

    private void printInvoiceDetails() {
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null) {
            boolean printed = printerJob.printPage(createInvoicePrintPage());
            if (printed) {
                printerJob.endJob();
            }
        }
    }
    private void addItemToInvoice() {
        String barcode = barcodeField.getText().trim();
        Item item = fetchItemByBarcode(barcode);

        if (item != null) {
            invoiceItems.add(item);
            
        } else {
            showAlert("Error", "Item not found for the given barcode.");
        }
    }

    private Item fetchItemByBarcode(String barcode) {
        try (Connection connection = DatabaseHelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Items WHERE ItemBarcode = ?")) {
            preparedStatement.setString(1, barcode);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String itemID = resultSet.getString("ItemID");
                    String itemName = resultSet.getString("ItemName");
                    double itemPrice = resultSet.getDouble("ItemPrice");
                    return new Item(itemID, itemName, barcode, itemPrice);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    private double calculateInvoiceTotal() {
        double total = 0.0;
        for (Item item : invoiceItems) {
            total += item.getItemPrice();
        }
        return total;
    }

    private void showAlertt(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(String title, String message) {
        showAlertt(title, message, Alert.AlertType.ERROR);
    }

    private void setupBarcodeFieldListener() {
        barcodeField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                addItemToInvoice();
            }
        });
    }

    private void updateInvoiceNumberLabel() {
        invoiceNumberLabel.setText("Invoice Number: " + currentInvoiceNumber);
    }
}