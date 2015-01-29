package de.bht.fpa.mail.s806984.model.appLogic.imap;

import de.bht.fpa.mail.s806984.model.appLogic.Account;
import de.bht.fpa.mail.s806984.model.appLogic.EmailManagerIF;
import de.bht.fpa.mail.s806984.model.data.Email;
import de.bht.fpa.mail.s806984.model.data.Folder;
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
        if(!f.getEmails().isEmpty()){
            return;
        }
        try{
            Message[] ms = store.getFolder(f.getName()).getMessages();
            for(Message m : ms){
                Email mail = IMapEmailConverter.convertMessage(m);
                f.addEmail(mail);
                System.out.println(mail);
            }
        }catch(MessagingException e){
            System.err.println(e.getMessage());
        }    
    }
    
}
