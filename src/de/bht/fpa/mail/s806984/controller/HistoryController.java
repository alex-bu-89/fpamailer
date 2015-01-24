package de.bht.fpa.mail.s806984.controller;

import de.bht.fpa.mail.s806984.model.appLogic.ApplicationLogic;
import de.bht.fpa.mail.s806984.model.appLogic.ApplicationLogicIF;
import de.bht.fpa.mail.s806984.model.data.Folder;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Controller class that manages history
 * 
 * @author Jules Doering
 * @author Alexander Buyanov
 */
class HistoryController implements Initializable  {
       
    @FXML
    private ListView listHistory;  
    @FXML
    private Button cancelButton;   
    @FXML
    private Button okButton;    
    private final FPAMailerLayoutController controller;
    private final ArrayList<String> history;
    private File pathFromHistory;
    private final String NO_HISTORY = "No base directories in history";
    private ApplicationLogicIF applicationLogic; 
    
    public HistoryController(FPAMailerLayoutController contr, ArrayList<String> history, ApplicationLogicIF applicationLogic) {
         controller = contr;
         this.history = history;
         this.applicationLogic = applicationLogic;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadHistory();
        cancelButton.setOnAction((ActionEvent event)->
        {
            System.out.println("Cancel clicked");
            Stage window = (Stage) cancelButton.getScene().getWindow();
            window.close(); 
        });
        okButton.setOnAction((ActionEvent event)->
        {
            System.out.println("OK clicked");
            if(pathFromHistory.isDirectory()){
                controller.loadTree(new Folder(pathFromHistory, true));
            }
            Stage window = (Stage) okButton.getScene().getWindow();
            window.close();
        });
        
    }
    
    /**
     * Load history array in ListView. 
     * 
     * Disable "ok button" if array is empty
     * Add action and create file when ListViewItem is selected
     */
    private void loadHistory() {
        if(history.isEmpty()){
            ObservableList items = FXCollections.observableArrayList(NO_HISTORY);
            listHistory.setItems(items); 
            okButton.setDisable(true);
        } else {
            ObservableList items = FXCollections.observableArrayList(history);
            listHistory.setItems(items);  
            okButton.setDisable(false);
        }
         
        listHistory.getSelectionModel().selectedItemProperty()
        .addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) {
                if(new_val == NO_HISTORY){
                    return;
                }
                pathFromHistory = new File(new_val);
            }
        });    
    }
}