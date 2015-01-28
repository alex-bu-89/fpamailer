package de.bht.fpa.mail.s806984.model.account;

import de.bht.fpa.mail.s806984.model.account.AccountFileDAO;
import de.bht.fpa.mail.s806984.model.account.AccountManagerIF;
import de.bht.fpa.mail.s806984.model.appLogic.Account;

import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Jules Doering
 * @author Alexander Buyanov
 */
public class AccountManager implements AccountManagerIF {
    
    //private AccountFileDAO accountFileDAO;
    private AccountDB_DAO accountDBDAO;
    
    public AccountManager(){
        //this.accountFileDAO = new AccountFileDAO();
        this.accountDBDAO = new AccountDB_DAO();    
    }
    
    @Override
    public Account getAccount(String name) {
        for(Account acc : accountDBDAO.getAllAccounts()){
            if(name.equals(acc.getName())){
                return acc;
            }
        }
        return null;    
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountDBDAO.getAllAccounts();
    }

    @Override
    public boolean saveAccount(Account acc) {
        accountDBDAO.saveAccount(acc);
        return true;
    }

    @Override
    public boolean updateAccount(Account account) {
        return accountDBDAO.updateAccount(account);
    }    
}

