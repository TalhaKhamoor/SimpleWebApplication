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
import models.Users;

/**
 *
 * @author 797138
 */
public class UsersDB {
    
    public int insertUser(Users user) throws DBException{
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        
        int numberOfRows = 0;
        try{
            trans.begin();
            em.merge(user);
            trans.commit();
            numberOfRows = 1;
        } catch (Exception ex){
            trans.rollback();
            Logger.getLogger(UsersDB.class.getName()).log(Level.SEVERE, "Cannot insert" + user.toString(), ex);
            throw new DBException("Error inserting user");
        } finally {
            em.close();
        }
        return numberOfRows;
    }
    
    public int updateUser(Users user) throws DBException {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        int numberOfRows =0;
        try {
            trans.begin();
            em.merge(user);
            trans.commit();
            numberOfRows =1;
        } catch (Exception ex){
            trans.rollback();
            Logger.getLogger(UsersDB.class.getName()).log(Level.SEVERE, "Cannot update "  + user.toString(), ex);
            throw new DBException("Error updating user");
        } finally{
            em.close();
        }
        return numberOfRows;
    }
    
    public Users getUser(String username){
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        try{
            Users user = em.find(Users.class, username);
            return user;
        } finally{
            em.close();
        }
    }
    
    public List<Users> getAll() throws Exception{
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        try{
            List<Users> users = em.createNamedQuery("Users.findAll", Users.class).getResultList();
            return users;
        }finally{
            em.close();
        }
    }
    
    public int deleteUser(Users user){
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        int numberOfRows = 0;
        
        try{
            trans.begin();
            em.remove(em.merge(user));
            trans.commit();
            numberOfRows = 1; 
        }catch (Exception e){
            trans.rollback();
            Logger.getLogger(UsersDB.class.getName()).log(Level.SEVERE, "Cannot delete " + user.toString(), e);
        }finally{
            em.close();
        }
        return numberOfRows;
    }
}
