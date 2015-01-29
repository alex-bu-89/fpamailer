package de.bht.fpa.mail.s806984.model.appLogic.imap;

import de.bht.fpa.mail.s806984.model.appLogic.Account;
import de.bht.fpa.mail.s806984.model.appLogic.EmailManagerIF;
import de.bht.fpa.mail.s806984.model.data.Email;
import de.bht.fpa.mail.s806984.model.data.Folder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;

/**
 *
 * @author Jules Doering
 * @author Alexander Buyanov
 */
public class IMapEmailManager implements EmailManagerIF {
    
    private Account account;
    private Store store;

    public IMapEmailManager(Account account) {
        this.account = account;
        this.store = IMapConnectionHelper.connect(account);
    }       
    
    @Override
    public Folder getFolder() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadEmails(Folder f) {
        
        try {
            javax.mail.Folder iFolder = store.getFolder(f.getName());
            iFolder.open(javax.mail.Folder.READ_ONLY);
            
            System.out.println(store.getFolder(f.getName()).getMessages());
            
//        if(!f.getEmails().isEmpty()){
//            throw new IllegalArgumentException("The emails in folder must not be empty.");
//        }
//        try{
//
//            Message[] ms = store.getFolder(f.getName()).getMessages();
//            System.out.println("test");
//            for(Message m : ms){
//                Email mail = IMapEmailConverter.convertMessage(m);
//                f.addEmail(mail);
//                System.out.println(mail);
//            }
//        }catch(MessagingException e){
//            System.err.println(e.getMessage());
//        }    
        } catch (MessagingException ex) {
            Logger.getLogger(IMapEmailManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
