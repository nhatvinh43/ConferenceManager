/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import conference.HibernateUtil;
import java.util.ArrayList;
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
public class ConferenceDAO {
    public Session session ;
    public Transaction tx;

    public ConferenceDAO() {
        
    }
    
    public ObservableList<Conference> getAllActive()
    {
       ObservableList<Conference> list = null;
       try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            list = FXCollections.observableArrayList(session.createQuery("select cf from Conference as cf inner join cf.location where time_start >=NOW()").list());
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
    
    public ObservableList<Conference> getAll()
    {
       ObservableList<Conference> list = null;
       try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            list = FXCollections.observableArrayList(session.createQuery("select cf from Conference cf").list());
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
    
    public ObservableList<Attend> getAttendees(String id)
    {
       ObservableList<Attend> list = null;
       try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            list = FXCollections.observableArrayList(session.createQuery("select attd from Attend attd inner join attd.user where attd.conference.id = :id and attd.accepted=1").setParameter("id", Integer.valueOf(id)).list());
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
    
    public Conference getConferenceByID(String id)
    {
       ObservableList<Conference> list = null;
       
       try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            list = FXCollections.observableArrayList(session.createQuery("select cf from Conference cf inner join cf.location where cf.id= :id").setParameter("id", Integer.valueOf(id)).list());
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace(); 
        }
        finally{
            session.close();
            if(list.isEmpty())
            {
                return null;
            }
            else
            {
                return list.get(0);
            }
        }
    }
    
    public long addAttendee (Account account, Conference conference)
    {
        long result = -1;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            Attend attend = new Attend(new AttendPK(account.getId(), conference.getId()),account, conference);
            session.saveOrUpdate(attend);
            result = attend.getId().getConf_id();
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace(); 
        }
        finally
        {
            session.flush();
            session.close();
            return result;
        }
    }
    
    public Attend getAttendeeByID (String conf_id, String user_id)
    {
        ObservableList<Attend> list = null;
       
       try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            list = FXCollections.observableArrayList(session.createQuery("select attd from Attend attd where attd.id.user_id= :user_id and attd.id.conf_id = :conf_id").setParameter("user_id", Integer.valueOf(user_id)).setParameter("conf_id", Integer.valueOf(conf_id)).list());
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace(); 
        }
        finally{
            session.close();
            if(list.isEmpty())
            {
                return null;
            }
            else
            {
                return list.get(0);
            }
        }
    }
    
    public ObservableList<Conference> getAllUnstarted()
    {
       ObservableList<Conference> list = null;
       try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            list = FXCollections.observableArrayList(session.createQuery("select cf from Conference as cf inner join cf.location where cf.time_start > NOW()").list());
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
    
    public ObservableList<Conference> getAllByUser(String user_id)
    {
       ObservableList<Conference> list = null;
       try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            list = FXCollections.observableArrayList(session.createQuery("select attd.conference from Attend attd inner join attd.conference where attd.user.id =:id").setParameter("id", Integer.valueOf(user_id)).list());
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
    
    public ObservableList<Attend> getRequests (String conf_id)
    {
        ObservableList<Attend> list = null;
       
       try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            list = FXCollections.observableArrayList(session.createQuery("select attd from Attend attd where attd.id.conf_id = :conf_id and accepted=0").setParameter("conf_id", Integer.valueOf(conf_id)).list());
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
    
    public void acceptRequest(int conf_id, int user_id)
    {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            FXCollections.observableArrayList(session.createQuery("update Attend attd set attd.accepted = 1 where attd.user.id = :user_id and attd.conference.id = :conf_id ").setParameter("conf_id", conf_id).setParameter("user_id", user_id).executeUpdate());
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace(); 
        }
        finally{
            session.close();
        }
    }
    
    public void declineRequest(int conf_id, int user_id)
    {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            FXCollections.observableArrayList(session.createQuery("delete from Attend attd where attd.user.id = :user_id and attd.conference.id = :conf_id ").setParameter("conf_id", conf_id).setParameter("user_id", user_id).executeUpdate());
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace(); 
        }
        finally{
            session.close();
        }
    }
    
    public void update(Conference cf)
    {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.update(cf);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace(); 
        }
        finally{
            session.close();
        }
    }
    
    public int add(Conference cf)
    {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.save(cf);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace(); 
        }
        finally{
            session.close();
            return cf.getId();
        }
    }
    
    public void deleteAttend (int conf_id, int user_id)
    {
        
       try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.createQuery("delete from Attend attd where attd.user.id = :user_id and attd.conference.id = :conf_id").setParameter("user_id", user_id).setParameter("conf_id", conf_id).executeUpdate();
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
