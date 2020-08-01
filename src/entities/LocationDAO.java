/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import conference.HibernateUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author VinhCute
 */
public class LocationDAO {
    
    public Session session ;
    public Transaction tx;

    public ObservableList<Location> getAllById(int id)
    {
        ObservableList<Location> list = null;
       
       try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            list = FXCollections.observableArrayList(session.createQuery("from Location where id=:id").setParameter("id", id).list());
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
    
    public ObservableList<Location> getAll()
    {
        ObservableList<Location> list = null;
       
       try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            list = FXCollections.observableArrayList(session.createQuery("from Location").list());
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
    
    public int add(Location location)
    {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.save(location);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace(); 
        }
        finally{
            session.close();
            return location.getId();
        }
    }
    
}
