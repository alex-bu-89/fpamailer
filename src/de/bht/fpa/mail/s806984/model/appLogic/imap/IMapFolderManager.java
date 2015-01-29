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
        this.topFolder = account.getTop();
        this.store = IMapConnectionHelper.connect(account);
    }
    
    @Override
    public Folder getTopFolder() {
        try{
            javax.mail.Folder topFolder = store.getDefaultFolder();  
            Folder f = new Folder(new File(topFolder.getName()), true);
            f.setPath(topFolder.getFullName());
            return f;
        }catch(MessagingException e){
            System.err.println(e.getMessage());
        }
        return topFolder;
    }

     @Override
   public void loadContent(Folder f) {
        if(!f.getComponents().isEmpty()){
            throw new IllegalArgumentException("The Commponent must not be empty.");
        }
        try{
            for(javax.mail.Folder folder : store.getFolder(f.getPath()).list()){
                Folder sf = new Folder(new File(folder.getName()), folder.list().length > 0);
                sf.setPath(folder.getFullName());
                f.addComponent(sf);
            }
        }catch(MessagingException e){
            System.err.println(e.getMessage());
        }
    }  
}
