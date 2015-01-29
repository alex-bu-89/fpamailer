package de.bht.fpa.mail.s806984.model.appLogic.imap;

import de.bht.fpa.mail.s806984.model.appLogic.Account;
import de.bht.fpa.mail.s806984.model.appLogic.EmailManagerIF;
import de.bht.fpa.mail.s806984.model.data.Folder;

/**
 *
 * @author Jules Doering
 * @author Alexander Buyanov
 */
public class IMapEmailManager implements EmailManagerIF {
    
    private Account account;

    public IMapEmailManager(Account account) {
        this.account = account;
    }       
    
    @Override
    public Folder getFolder() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadEmails(Folder f) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
