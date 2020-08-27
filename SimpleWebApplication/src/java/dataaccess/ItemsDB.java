/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccess;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import models.Items;

/**
 *
 * @author 797138
 */
public class ItemsDB {
    
    public int insertItem(Items item) throws DBException {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        int numberOfRows = 0;
        
        try{
            trans.begin();
            em.merge(item);
            trans.commit();
            numberOfRows = 1;
        } catch (Exception e){
            trans.rollback();
            Logger.getLogger(ItemsDB.class.getName()).log(Level.SEVERE, "Cannot insert " + item.toString(), e);
            throw new DBException("Error inserting new item!");
        }
        return numberOfRows;
    }
    
    public Items getItem(int itemID){
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        try{
            Items item = em.find(Items.class, itemID);
            return item;
        } finally{
            em.close();
        }
    }
    
    public List<Items> getAll() throws Exception{
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        try{
            List<Items> items = em.createNamedQuery("Items.findAll", Items.class).getResultList();
            return items;
        } finally{
            em.close();
        }
    }
    
    public int updateItem(Items item) throws DBException{
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        int numberOfRows = 0;
        try{
            trans.begin();
            em.merge(item);
            trans.commit();
            numberOfRows =1;
        } catch (Exception ex){
            trans.rollback();
            Logger.getLogger(UsersDB.class.getName()).log(Level.SEVERE, "Cannot update "  + item.toString(), ex);
            throw new DBException("Error updating item");
        } finally{
            em.close();
        }
        return numberOfRows;
    }
    
    public int deleteItem(Items item) throws DBException{
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        int numberOfRows = 0;
        
        try{
            trans.begin();
            em.remove(em.merge(item));
            trans.commit();
            numberOfRows = 1;
        } catch (Exception e){
            trans.rollback();
            Logger.getLogger(ItemsDB.class.getName()).log(Level.SEVERE, "Cannot delete " + item.toString(), e);
        } finally{
            em.close();
        }
        return numberOfRows;
    }
}
