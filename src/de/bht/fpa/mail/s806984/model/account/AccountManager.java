package de.bht.fpa.mail.s806984.model.account;

import de.bht.fpa.mail.s806984.model.account.AccountFileDAO;
import de.bht.fpa.mail.s806984.model.account.AccountManagerIF;
import de.bht.fpa.mail.s806984.model.appLogic.Account;

import java.util.List;

/**
 *
 * @author Jules Doering
 * @author Alexander Buyanov
 */
public class AccountManager implements AccountManagerIF {
    
    private AccountFileDAO accountFileDAO;
    
    public AccountManager(){
        this.accountFileDAO = new AccountFileDAO();
    }
    
    @Override
    public Account getAccount(String name) {
        for(Account acc : accountFileDAO.getAllAccounts()){
            if(name.equals(acc.getName())){
                return acc;
            }
        }
        return null;    
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountFileDAO.getAllAccounts();
    }

    @Override
    public boolean saveAccount(Account acc) {
        accountFileDAO.saveAccount(acc);
        return true;
    }

    @Override
    public boolean updateAccount(Account account) {
        return accountFileDAO.updateAccount(account);
    }    
}

