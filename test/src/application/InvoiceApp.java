package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class InvoiceApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
    	 // Test the database connection
        DatabaseHelper.testConnection();
        // Initialize the database schema
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main_view.fxml"));
        
        Parent root = loader.load();
        
        // Get the controller instance from the FXMLLoader
        //Controller controller = loader.getController();
        
        Scene scene = new Scene(root);
        primaryStage.setTitle("Invoice Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

