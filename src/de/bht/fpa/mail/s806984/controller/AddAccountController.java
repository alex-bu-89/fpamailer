package de.bht.fpa.mail.s806984.controller;

import de.bht.fpa.mail.s806984.model.appLogic.Account;
import de.bht.fpa.mail.s806984.model.appLogic.ApplicationLogicIF;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import jdk.nashorn.internal.objects.NativeArray;

/**
 *
 * @author Jules Doering
 * @author Alexander Buyanov
 */
class AddAccountController implements Initializable {
  
    private final String ERROR_MESSAGE = "All textfields must contain data";
    private final String ERROR_MESSAGE_FOLDER = "Account already exists, choose another name";
    private final String UPDATE_BUTTON = "Update";
    
    Account account;
    
    @FXML
    private TextField addName;
    @FXML
    private TextField addHost;
    @FXML
    private TextField addUsername;
    @FXML
    private TextField addPassword;
    @FXML
    private Button addCancel;
    @FXML
    private Button addSave;
    @FXML
    private Label addErrorMessage;

    
    FPAMailerLayoutController controller;
    ApplicationLogicIF applicationLogic;
    
    public AddAccountController(FPAMailerLayoutController controller, ApplicationLogicIF applicationLogic, Account account) {
        this.controller = controller;
        this.applicationLogic = applicationLogic;
        this.account = account;      
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        if(account != null){
            this.addName.setText(account.getName());
            this.addHost.setText(account.getHost());
            this.addUsername.setText(account.getUsername());
            this.addPassword.setText(account.getPassword());
            this.addSave.setText(UPDATE_BUTTON);
        }
        
        loadMenu(addCancel);
        loadMenu(addSave);
    }

    private void loadMenu(Button button) {
        button.setOnAction((ActionEvent event) -> {
            Button b = (Button) event.getSource();
            if(b == this.addCancel){
                Stage window = (Stage) addCancel.getScene().getWindow();
                window.close();
            }
            if(b == this.addSave){               
                if(account == null){
                    saveAccount();
                } else {
                    updateAccount();
                }             
            }
        });
    }

    private void saveAccount() {
        if( !addName.getText().isEmpty() && 
            !addHost.getText().isEmpty() && 
            !addUsername.getText().isEmpty() &&  
            !addPassword.getText().isEmpty() 
        ){
            addErrorMessage.setText("");
           
            for (String acc : applicationLogic.getAllAccounts()) {
                if(acc.equals(addName.getText())){
                    addErrorMessage.setText(ERROR_MESSAGE_FOLDER);
                    return;
                }
            }

            // creates new account fromt text fields
            Account newAcc = new Account(   addName.getText(),
                                            addHost.getText(),
                                            addUsername.getText(),
                                            addPassword.getText()
            );
            applicationLogic.saveAccount(newAcc);                  
            controller.loadSubMenu();

            // create folder for account. Not sure
            /*
            boolean success = (new File(controller.ACCOUNTS_DIR + "/" + newAcc.getTop())).mkdirs();                 
            if (!success) {
                System.out.println("File was not created: " + newAcc.getTop().getPath());
            }
            */

            Stage window = (Stage) addCancel.getScene().getWindow();
            window.close();
        } else {
            addErrorMessage.setText(ERROR_MESSAGE);
        }    
    } 

    private void updateAccount() {
        
        if( addName.getText().isEmpty() && 
            addHost.getText().isEmpty() && 
            addUsername.getText().isEmpty() &&  
            addPassword.getText().isEmpty() 
        ){
            addErrorMessage.setText(ERROR_MESSAGE);
            return;
        }
        
        // if there is no changes close the window
        if(     
            account.getName().equals(addName.getText()) &&
            account.getHost().equals(addHost.getText()) &&
            account.getUsername().equals(addUsername.getText()) &&
            account.getPassword().equals(addPassword.getText())
        ){
            Stage window = (Stage) addCancel.getScene().getWindow();
            window.close();
            return;
        }
        
        // creates new account fromt text fields
        Account newAcc = new Account(   addName.getText(),
                                        addHost.getText(),
                                        addUsername.getText(),
                                        addPassword.getText()
        );
        applicationLogic.updateAccount(newAcc);
        controller.loadSubMenu();
        
        System.out.println(newAcc);
        Stage window = (Stage) addCancel.getScene().getWindow();
        window.close();
        
    }
}
