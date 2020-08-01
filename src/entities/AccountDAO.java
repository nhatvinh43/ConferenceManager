/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import conference.HibernateUtil;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author VinhCute
 */
public class AccountDAO{
    
    public Session session ;
    public Transaction tx;

    public AccountDAO() {
        
        
    }
    
    public ObservableList<Account> getAll()
    {
       ObservableList<Account> list = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            list = FXCollections.observableArrayList(session.createQuery("from Account").list());
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace(); 
        }
        finally{
            session.close();
            return list;
        }
    }
    
    public void setBlocked(int id, boolean status)
    {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.createQuery("update Account set blocked = :status where id = :id").setParameter("id", id).setParameter("status", status).executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace(); 
        }
        finally{
            session.close();
        }
    }
    
    public Account queryAccount(String username, String password)
    {
        List<Account> tempAccount = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            tempAccount = session.createQuery("from Account where username = :username").setParameter("username", username).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace(); 
        }
        finally{
            session.close();
            if(tempAccount.isEmpty())
            {
                return null;
            }
            else 
            {
                if(password.equals(tempAccount.get(0).getPassword()))
                {
                    return tempAccount.get(0);
                }
                else{
                    return null;
                }
            }
        }
    }
    
    public long addAccount(String fullname, String username, String password, String email)
    {
        long result = -1;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            Account account = new Account(fullname, username, password, email);
            account.setAdmin(false);
            account.setBlocked(false);
            session.persist(account);
            result = account.getId();
            tx.commit();
            session.flush();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace(); 
        }
        finally
        {
            session.close();
            return result;
        }
    }
    
    public void updateAccountInfo(Account account, String fullname, String email)
    {
        int result = 0;
        Account newAccount = account;
        newAccount.setFullname(fullname);
        newAccount.setEmail(email);
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.update(newAccount);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace(); 
        }
        finally{
            session.close();
        }
    }
    
    public void updateAccountPassword(Account account, String newPassword)
    {
        Account newAccount = account;
        newAccount.setPassword(newPassword);
         try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
           session.update(newAccount);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace(); 
        }
        finally{
            session.close();
        }
    }
    
    
}
