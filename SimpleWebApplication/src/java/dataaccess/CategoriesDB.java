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
import models.Categories;

/**
 *
 * @author 797138
 */
public class CategoriesDB {
    
    public Categories getCategory(int categoryID){
        
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        try{
            Categories category = em.find(Categories.class, categoryID);
            return category;
        } finally{
            em.close();
        }
        
    }
    
    public int insertCategory(Categories cat) throws DBException{
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        int numberOfRows = 0;
        try{
            trans.begin();
            em.merge(cat);
            trans.commit();
            numberOfRows = 1;
        } catch (Exception ex){
            trans.rollback();
            Logger.getLogger(UsersDB.class.getName()).log(Level.SEVERE, "Cannot insert" + cat.toString(), ex);
            throw new DBException("Error inserting user");
        } finally {
            em.close();
        }
        return numberOfRows;
    }
    
    public int deleteCategory(Categories cat) throws DBException{
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        int numberOfRows = 0;
        
        try{
            trans.begin();
            em.remove(em.merge(cat));
            trans.commit();
            numberOfRows = 1;
        } catch (Exception e){
            trans.rollback();
            Logger.getLogger(ItemsDB.class.getName()).log(Level.SEVERE, "Cannot delete " + cat.toString(), e);
        } finally{
            em.close();
        }
        return numberOfRows;
    }
    
    public List<Categories> getAll() throws Exception{
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        try{
            List<Categories> categories = em.createNamedQuery("Categories.findAll", Categories.class).getResultList();
            return categories;
        } finally{
            em.close();
        }
    }
}
