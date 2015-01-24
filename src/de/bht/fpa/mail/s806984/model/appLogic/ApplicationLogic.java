package de.bht.fpa.mail.s806984.model.appLogic;

import de.bht.fpa.mail.s806984.controller.FPAMailerLayoutController;
import de.bht.fpa.mail.s806984.model.account.AccountManager;
import de.bht.fpa.mail.s806984.model.data.Email;
import de.bht.fpa.mail.s806984.model.data.Folder;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * ApplicationLogic executes facade pattern
 *
 * @author Jules Doering
 * @author Alexander Buyanov
 */
public class ApplicationLogic implements ApplicationLogicIF {

    private Folder topFolder;
    private FileManager fileManager;
    private EmailManager mailManager;
    private FPAMailerLayoutController controller;
    private AccountManager accountManager;

    public ApplicationLogic(Folder f, FPAMailerLayoutController controller) {             
        this.topFolder = f;
        this.fileManager = new FileManager(new File(f.getPath()));
        this.mailManager = new EmailManager(f);
        this.controller = controller;
        this.accountManager = new AccountManager();
    }

    /**
     * Get current root folder.
     *
     * @return current root folder.
     */
    @Override
    public Folder getTopFolder() {
        return this.topFolder;
    }

    /**
     * Loads all relevant content in the directory path of a folder into the
     * folder.
     *
     * @param folder the folder into which the content of the corresponding
     * directory should be loaded
     */
    @Override
    public void loadContent(Folder folder) {
        this.fileManager.loadContent(folder);
    }

    /**
     * Searches for all emails in the selected folder that contain the given
     * pattern.
     *
     * @return a list of all emails that contain the pattern
     * @param pattern contains a string for comparison with email content
     */
    @Override
    public List<Email> search(String pattern) {
        List<Email> emails = new ArrayList<>();
        pattern = pattern.toLowerCase();
        for (Email email : getTopFolder().getEmails()) {
           if (
                   email.getSubject().toLowerCase().contains(pattern)   || 
                   email.getText().toLowerCase().contains(pattern)      ||
                   email.getReceived().toLowerCase().contains(pattern)  ||
                   email.getSent().toLowerCase().contains(pattern)      ||
                   email.getReceiver().toLowerCase().contains(pattern)  ||
                   email.getSender().toLowerCase().contains(pattern)
            ){
               emails.add(email);
           }
        }
        return emails;
    }
    
    /**
     * Loads all emails in the directory path of the given folder
     * as objects of the class Email into the folder.
     * @param folder    the folder into which the emails of the corresponding 
     *                  directory should be loaded
     */
    @Override
    public void loadEmails(Folder folder) {
        this.mailManager.loadEmails(folder);
    }
    
    /**
     * Changes the root directory of the application, and initializes
     * the folder manager with the new root directory.
     * @param file  the path to the directory which was selected as 
     *              the new root directory of the application.
     */
    @Override
    public void changeDirectory(File file) {
        Folder folder = new Folder(file, true);
        this.topFolder = folder;
        this.fileManager = new FileManager(file);
        this.mailManager = new EmailManager(folder);
        this.controller.loadTree(folder);
        controller.addHistory(folder.getPath());
    }

    /**
     * Saves the email objects of the selected folder into the given
     * directory.
     * @param file  the path to the directory in which the email objects
     *              should be saved.
     */
    @Override
    public void saveEmails(File file) {
        int count = 1;
        for (Email email : this.topFolder.getEmails()) {
            File newFileName = new File(file + "/email-" + count + ".xml");
            mailManager.writeEmail(email, newFileName);
            count++;
        }
    }

    @Override
    public void openAccount(String name) {
        changeDirectory(new File(getAccount(name).getTop().getPath()));
    }

    @Override
    public List<String> getAllAccounts() {
        List<String> list = new ArrayList();
        for(Account acc : accountManager.getAllAccounts()){
            list.add(acc.getName());
        }
        return list;
    }

    @Override
    public Account getAccount(String name) {
        return accountManager.getAccount(name);   
    }

    @Override
    public boolean saveAccount(Account account) {
        return accountManager.saveAccount(account);
    }

    @Override
    public void updateAccount(Account account) {
        accountManager.updateAccount(account);
    }
    
}