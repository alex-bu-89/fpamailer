package de.bht.fpa.mail.s806984.controller;

import de.bht.fpa.mail.s806984.model.account.TestDBDataProvider;
import de.bht.fpa.mail.s806984.model.appLogic.Account;
import de.bht.fpa.mail.s806984.model.appLogic.ApplicationLogic;
import de.bht.fpa.mail.s806984.model.appLogic.ApplicationLogicIF;
import de.bht.fpa.mail.s806984.model.data.Component;
import de.bht.fpa.mail.s806984.model.data.Folder;
import de.bht.fpa.mail.s806984.model.data.Email;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventType;
import javafx.scene.control.Menu;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

/**
 * Main controller class of Mailer project
 *
 * @author Jules Doering
 * @author Alexander Buyanov
 */
public class FPAMailerLayoutController implements Initializable {

    //private static final File USER_DIR = new File(System.getProperty("user.home"));
    private static final File USER_DIR = new File("emails/default");
    public static final File ACCOUNTS_DIR = new File("emails/Account");
    private final Image FILE_ICON = new Image(getClass().getResourceAsStream("/de/bht/fpa/mail/s806984/images/file.png"));
    private final Image FOLDER_ICON = new Image(getClass().getResourceAsStream("/de/bht/fpa/mail/s806984/images/folder.png"));
    private final Image FOLDER_OPEN_ICON = new Image(getClass().getResourceAsStream("/de/bht/fpa/mail/s806984/images/folder-open.png"));

    private ApplicationLogicIF applicationLogic;
    //private FolderManagerIF fileManager;
    private ArrayList<String> history;

    @FXML
    private TreeView<Component> treeViewBox;
   
    @FXML
    private MenuItem openFileChooser;
    @FXML
    private MenuItem saveFileChooser;
    @FXML
    private MenuItem folderChooserHistory;
    
    @FXML
    private Menu openAccount;
    @FXML
    private Menu editAccount;
    @FXML
    private MenuItem newAccount;
    
    @FXML
    private TableView<Email> table;
    @FXML
    private TextField filter;

    TableColumn<Email, String> t_importance;
    TableColumn<Email, String> t_received;
    TableColumn<Email, String> t_read;
    TableColumn<Email, String> t_sender;
    TableColumn<Email, String> t_recipients;
    TableColumn<Email, String> t_subject;

    @FXML
    private Text text_sender;
    @FXML
    private Text text_subject;
    @FXML
    private Text text_received;
    @FXML
    private Text text_receiver;
    @FXML
    private TextArea text_message;

    private ObservableList<Email> emailData = FXCollections.observableArrayList();
    FilteredList<Email> filteredData;

    @FXML
    private Text countEmails;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
                   
        history = new ArrayList<String>();
        Folder folder = new Folder(USER_DIR, true);
        history.add(folder.getPath());
        // ??
        applicationLogic = new ApplicationLogic(folder, this);
        //applicationLogic.openAccount("google-test");
        // loads treeview
        //loadTree(folder);
        
        // loads menu
        loadMenuBar();
                
        addSelectEvent();
        loadTableView();
        addTableViewSelectEvent();
        
    }

    /**
     * Sets the root Element for main TreeView and attaches Eventhandler
     *
     * @param f Folder that becomes the root element.
     */
    public void loadTree(Folder f) {
        TreeItem<Component> root = new TreeItem<>(f);

        // add events
        makeExpandable(root);
        makeCollapse(root);

        root.getChildren().add(new TreeItem());
        root.setExpanded(true);
        treeViewBox.setRoot(root);
    }
    
    private void loadMenuBar() {
        loadMenu(openFileChooser);
        loadMenu(saveFileChooser);
        loadMenu(folderChooserHistory);
        loadSubMenu();        
    }

    public void loadSubMenu() {
        openAccount.getItems().clear();
        editAccount.getItems().clear();
        for(String acc : applicationLogic.getAllAccounts()){
            openAccount.getItems().add(new MenuItem(acc));          
            editAccount.getItems().add(new MenuItem(acc));
        }
        
        // add events to subitem
        for(MenuItem item : openAccount.getItems()){
            loadMenu(item);
        }
        for(MenuItem item : editAccount.getItems()){
            loadMenu(item);
        }
        
        loadMenu(newAccount);
    }
    
    /**
     * Handles menu events. Creates corresponding event to clicked menu item
     *
     * @param item - menu item that was clicked
     */
    private void loadMenu(MenuItem item) {
        item.setOnAction((ActionEvent event) -> {
            
            MenuItem source = (MenuItem) event.getSource();

            if (source == this.openFileChooser) {
                try {
                    DirectoryChooser directoryChooser = new DirectoryChooser();
                    directoryChooser.setTitle("File chooser");
                    //Show open file dialog
                    File file = directoryChooser.showDialog(null);
                    if (file != null) {
                        history.add(file.getAbsolutePath());
                        //loadTree(new Folder(file, true));
                        applicationLogic.changeDirectory(file);
                        Folder folder = new Folder(file, true);
                        loadTree(folder);
                        addHistory(folder.getPath());
                    }
                } catch (NullPointerException e) {
                    System.out.println("Something wrong width filechooser. Maybe folder is not accessible");
                    e.getStackTrace();
                }
            }

            // Save is clicked
            if (source == this.saveFileChooser) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle("File chooser");
                //Show open file dialog
                File file = directoryChooser.showDialog(null);
                if (file != null) {
                    System.out.println("saving at: " + file.toPath());
                    applicationLogic.saveEmails(file);
                }
            }
            if (source == this.folderChooserHistory) {
                Stage editStage = new Stage(StageStyle.UTILITY);
                editStage.setTitle("History of folders");
                URL location = getClass().getResource("/de/bht/fpa/mail/s806984/view/historyLayout.fxml");
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(location);
                fxmlLoader.setController(new HistoryController(this, history, applicationLogic));
                try {
                    Pane myPane = (Pane) fxmlLoader.load();
                    Scene myScene = new Scene(myPane);
                    editStage.setScene(myScene);
                    editStage.show();
                } catch (IOException ex) {
                    System.err.println("Scene error" + ex.getLocalizedMessage());
                }
            }
            
            // submenu events
            if (source.getParentMenu() == this.openAccount) {
                System.out.println("Open acc: " + source.getText());
                System.out.println(applicationLogic.getAccount(source.getText()));
                applicationLogic.openAccount(source.getText());
                System.out.println("Top Folder ist " + applicationLogic.getTopFolder());                              
//                loadTree(applicationLogic.getTopFolder());
//                addHistory(applicationLogic.getTopFolder().getPath());
            }
            if (source.getParentMenu() == this.editAccount) {
                
                System.out.println("Edit acc: " + source.getText());
                Account account = applicationLogic.getAccount(source.getText());
                
                Stage editStage = new Stage(StageStyle.UTILITY);
                editStage.setTitle("Add account");
                URL location = getClass().getResource("/de/bht/fpa/mail/s806984/view/addAccountView.fxml");
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(location);
                fxmlLoader.setController(new AddAccountController(this, applicationLogic, account));
                try {
                    Pane myPane = (Pane) fxmlLoader.load();
                    Scene myScene = new Scene(myPane);
                    editStage.setScene(myScene);
                    editStage.show();
                } catch (IOException ex) {
                    System.err.println("Scene error" + ex.getLocalizedMessage());
                }
                
            }
            if (source == this.newAccount) {
                
                Stage editStage = new Stage(StageStyle.UTILITY);
                editStage.setTitle("Add account");
                URL location = getClass().getResource("/de/bht/fpa/mail/s806984/view/addAccountView.fxml");
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(location);
                fxmlLoader.setController(new AddAccountController(this, applicationLogic, null));
                try {
                    Pane myPane = (Pane) fxmlLoader.load();
                    Scene myScene = new Scene(myPane);
                    editStage.setScene(myScene);
                    editStage.show();
                } catch (IOException ex) {
                    System.err.println("Scene error" + ex.getLocalizedMessage());
                }
                
            }

        });
    }

    /**
     * Filles Tree or subtree with Items where they belong to.
     *
     * @param item
     */
    private void configureTree(TreeItem<Component> item) {
        // load content for TreeItem item
        loadContent(item);

        //set open icon
        item.setGraphic(new ImageView(FOLDER_OPEN_ICON));

        //set dummy tree-items for child elements
        for (TreeItem<Component> temp : item.getChildren()) {
            temp.setGraphic(new ImageView(FOLDER_ICON));
            if (temp.getValue().isExpandable()) {
                temp.getChildren().add(new TreeItem());
            }
            temp.setExpanded(false);
        }
    }

    /**
     * Attaches sub components in treeview and datamodel
     *
     * @param item
     */
    private void loadContent(TreeItem<Component> item) {
        item.getChildren().clear();
        Folder f = (Folder) item.getValue();
        //f.getComponents().clear();
        applicationLogic.loadContent(f);
        for (Component c : f.getComponents()) {
            item.getChildren().add(new TreeItem(c));
        }
    }

    /**
     * Attaches branchExpandedEvent() handler to TreeItem
     *
     * @param item Item that will become expandable.
     */
    private void makeExpandable(TreeItem item) {
        item.addEventHandler(TreeItem.branchExpandedEvent(), new EventHandler() {
            @Override
            public void handle(Event event) {
                TreeItem<Component> source = (TreeItem) event.getSource();
                configureTree(source);
            }
        });
    }

    /**
     * Attaches branchCollapseEvent handler to Treeitem
     *
     * @param item Item that will become collapseable.
     */
    private void makeCollapse(TreeItem<Component> item) {
        item.addEventHandler(TreeItem.branchCollapsedEvent(), new EventHandler() {
            @Override
            public void handle(Event event) {
                TreeItem<Component> source = (TreeItem) event.getSource();
                source.setGraphic(new ImageView(FOLDER_ICON));
            }
        });
    }

    /**
     * Handles select action if user selects a tree-item
     */
     private void addSelectEvent() {
        // EventHandler prints Emails for current selected folder
       // treeViewBox.getSelectionModel().selectedItemProperty().addListener((ObservableValue observable, Object oldValue, Object newValue) -> handleSelectItem(observable, oldValue, newValue));
        
        treeViewBox.getSelectionModel().selectedItemProperty().addListener( new ChangeListener() {
        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            TreeItem<Component> selectedItem = (TreeItem<Component>) newValue;
                if (newValue != null){
                    try {
                        // clear old email info
                        emailData.clear();
                        filteredData.clear();
                        countEmails.setText("");
                        applicationLogic.getTopFolder().getEmails().clear();

                        File file = new File(selectedItem.getValue().getPath());
                        Folder folder = new Folder(file, hasSubfolder(file));
                        //EmailManagerIF emailManager = new EmailManager(folder);

                        applicationLogic.loadEmails(folder);

                        // counts emails and set it in current treeItem 
                        folder.setName(folder.getName()+" ("+ applicationLogic.getTopFolder().getEmails().size() +")");
                        selectedItem.setValue(folder);

                        System.out.println("Selected Directory: " + applicationLogic.getTopFolder().getPath());
                        System.out.println("Number of Emails: " + applicationLogic.getTopFolder().getEmails().size());
                        
//                        for(int i = 0; i < applicationLogic.getTopFolder().getEmails().size(); i++){
//                            Email email = applicationLogic.getTopFolder().getEmails().get(i);
//                            System.out.println(email.toString());
//                            emailData.add(email);               
//                            table.setItems(emailData);                    
//                        }
                        
                        List<Email> emailList = applicationLogic.getTopFolder().getEmails();
                        
                        for(int i = 0; i < emailList.size(); i++){
                            Email email = emailList.get(i);
                            System.out.println(email.toString());
                            emailData.add(email);               
                        }
                        
                        table.setItems(emailData);                    

                        // set received column sorted by default
                        t_received.setSortType(TableColumn.SortType.ASCENDING);
                        table.getSortOrder().add(t_received); 

                        System.out.println("--------------------\n");
                    } catch (Exception e) {
                        System.out.println("The File could not be created by selected item");
                        e.getStackTrace();
                    }
                }
            }
      });
        
        treeViewBox.getSelectionModel().clearSelection();
    }

    /**
     * Returns true if folder contains subfolders. False if not.
     *
     * @param folder
     * @return Boolean
     */
    private Boolean hasSubfolder(File file) {
        for (File f : file.listFiles()) {
            if (f.isDirectory()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the Columns of the Main Tableview
     */
    private void loadTableView() {

        t_importance = new TableColumn("Importance");
        t_importance.prefWidthProperty().bind(table.widthProperty().divide(6));
        t_received = new TableColumn("Received");
        t_received.prefWidthProperty().bind(table.widthProperty().divide(6));
        t_read = new TableColumn("Read");
        t_read.prefWidthProperty().bind(table.widthProperty().divide(6));
        t_sender = new TableColumn("Sender");
        t_sender.prefWidthProperty().bind(table.widthProperty().divide(6));
        t_recipients = new TableColumn("Recipients");
        t_recipients.prefWidthProperty().bind(table.widthProperty().divide(6));
        t_subject = new TableColumn("Subject");
        t_subject.prefWidthProperty().bind(table.widthProperty().divide(6));

        t_importance.setCellValueFactory(new PropertyValueFactory<>("importance"));
        t_received.setCellValueFactory(new PropertyValueFactory<>("received"));      
        t_received.setComparator((String s1, String s2) -> {
            try{
                DateFormat format = new SimpleDateFormat("EEEE, dd. MMM yyyy HH:mm");
                Date date1 = format.parse(s1);
                Date date2 = format.parse(s2);
                return date1.compareTo(date2);
            }catch(ParseException e){
                System.out.println(e.getMessage());
            }
            return -1;
        });       
        t_read.setCellValueFactory(new PropertyValueFactory<>("read"));
        t_sender.setCellValueFactory(new PropertyValueFactory<>("sender"));
        t_recipients.setCellValueFactory(new PropertyValueFactory<>("receiverTo")); // not sure because we have 3 arrays (receiverTo, receiverCC, receiverBCC)
        t_subject.setCellValueFactory(new PropertyValueFactory<>("subject"));

        table.getColumns().addAll(t_importance, t_received, t_read, t_sender, t_recipients, t_subject);
       
        loadFilter();           

    }
    /**
     * Loads a search filter. 
     * If anything is entered in the search form, an event is executed.
     */
    private void loadFilter() {
        // search filter
        filteredData = new FilteredList<>(emailData, p -> true);       
        filter.textProperty().addListener((observable, oldValue, newValue) -> {         
            ObservableList<Email> filter = FXCollections.observableArrayList();          
            List<Email> sortedList = applicationLogic.search(newValue);
            filter.addAll(sortedList);
            table.getItems().clear();
            table.setItems(filter);           
            if(sortedList.size() >= 0){
                Integer temp = sortedList.size();
                countEmails.setText(temp.toString());
            } else {
                countEmails.setText("");
            } 
        });         
    }
    /**
     * When an email is clicked, detailed information about it is shown.
     */
    private void addTableViewSelectEvent() {
        //Add change listener
        table.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (table.getSelectionModel().getSelectedItem() != null) {
                System.out.println((Email) newValue);
                Email email = (Email) newValue;
                text_sender.setText(email.getSender());
                text_subject.setText(email.getSubject());
                text_received.setText(email.getReceived());
                text_receiver.setText(email.getReceiverTo());
                text_message.setText(email.getText());

            } else {
                text_sender.setText(null);
                text_subject.setText(null);
                text_received.setText(null);
                text_receiver.setText(null);
                text_message.setText(null);
            }
        });
    }
    
    public void addHistory(String history) {
        this.history.add(history);
    }
    
}
