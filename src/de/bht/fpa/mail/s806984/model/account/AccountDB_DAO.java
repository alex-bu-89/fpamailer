/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mail.s806984.model.account;

import de.bht.fpa.mail.s806984.model.appLogic.Account;
import java.util.List;

/**
 *
 * @author Jules Doering
 */
public class AccountDB_DAO implements AccountDAOIF {

    public AccountDB_DAO() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Account> getAllAccounts() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Account saveAccount(Account acc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean updateAccount(Account acc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void saveAccounts(List<Account> acclist) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void remove(String name, List<Account> list) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
