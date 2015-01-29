package de.bht.fpa.mail.s806984.model.appLogic.imap;

import de.bht.fpa.mail.s806984.model.appLogic.Account;
import de.bht.fpa.mail.s806984.model.appLogic.FolderManagerIF;
import de.bht.fpa.mail.s806984.model.data.Folder;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.Store;

/**
 *
 * @author Jules Doering
 * @author Alexander Buyanov
 */
public class IMapFolderManager implements FolderManagerIF {
    
    private Account account;
    private Folder topFolder;
    private Store store;

    public IMapFolderManager(Account account){
        this.account = account;
    }
    
    @Override
    public Folder getTopFolder() {
        Store store = IMapConnectionHelper.connect(account);
        try{
            javax.mail.Folder topFolder = store.getDefaultFolder();  
            Folder top = new Folder();
            top.setName(account.getName());
            top.setPath(topFolder.getFullName());
            return top;
        }catch(MessagingException e){
            System.err.println(e.getMessage());
        }
        return null;
    }

     @Override
   public void loadContent(Folder f) {
        if(!f.getComponents().isEmpty()){
            return;
        }
        Store store = IMapConnectionHelper.connect(account);
        try{
            for(javax.mail.Folder folder : store.getFolder(f.getName()).list()){
                Folder subFolder = new Folder();
                subFolder.setName(folder.getName());
                subFolder.setPath(folder.getFullName());
                subFolder.setExpandable(folder.list().length > 0);
                f.addComponent(subFolder);
            }
        }catch(MessagingException e){
            System.err.println(e.getMessage());
        }
    }
    
    public boolean isConnected(){      
        return this.store.isConnected();
    }

    
}
