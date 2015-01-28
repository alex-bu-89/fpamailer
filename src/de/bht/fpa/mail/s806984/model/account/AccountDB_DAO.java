/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mail.s806984.model.account;

import de.bht.fpa.mail.s806984.model.appLogic.Account;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

/**
 * 
 * @author Jules Doering
 * @author Alexander Buyanov
 */
public class AccountDB_DAO implements AccountDAOIF {
    
    private static final String TESTDATA_PU = "test";
    private static EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction et;
    
    public AccountDB_DAO() {
        try{
            TestDBDataProvider.createAccounts();
            emf = Persistence.createEntityManagerFactory(TESTDATA_PU);
            em = emf.createEntityManager();
            et = em.getTransaction();
        }catch(PersistenceException ex){
            System.out.println(ex.getCause());
            System.out.println(ex.getStackTrace());       
        }
    }

    @Override
    public List<Account> getAllAccounts() {
        try{                      
            Query query = em.createQuery("SELECT a FROM Account a");
            List<Account> accList = (List<Account>) query.getResultList();
            //em.close(); // Attempting to execute an operation on a closed EntityManager. So i commented it
            return accList;         
        }catch(PersistenceException e){
            System.out.println(e.getCause());
        }
        return new ArrayList<Account>();
    }

    @Override
    public Account saveAccount(Account acc) {
        try{
            et.begin();
            em.merge(acc);
            et.commit();
            //em.close(); // Attempting to execute an operation on a closed EntityManager.
            return acc;
            
        }catch(PersistenceException e){
            System.out.println(e.getCause());
            
        }
        return acc;
    }

    @Override
    public boolean updateAccount(Account acc) {
        try{
            et.begin();
            em.merge(acc);
            et.commit();
            //em.close(); // Attempting to execute an operation on a closed EntityManager.
            return true;
            
        }catch(PersistenceException e){
            System.out.println(e.getCause());
            
        }
        return false;
    }
}
