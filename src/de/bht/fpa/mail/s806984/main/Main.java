package de.bht.fpa.mail.s806984.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class of mailer project
 * 
 * @author Jules Doering
 * @author Alexander Buyanov
 */
public class Main extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        
        Parent root = FXMLLoader.load(getClass().getResource("/de/bht/fpa/mail/s806984/view/FPAMailerLayout.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setTitle("Mailer App");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}