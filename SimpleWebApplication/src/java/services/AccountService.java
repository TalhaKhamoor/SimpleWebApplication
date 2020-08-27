/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import dataaccess.UsersDB;
import java.util.List;
import models.Users;

/**
 *
 * @author 797138
 */
public class AccountService {
    
    private UsersDB userDB;
    
    public AccountService(){
        userDB = new UsersDB();
    }
    
    public Users getUser(String username) throws Exception{
        return userDB.getUser(username);
    }
    
    public List<Users> getAll() throws Exception{
        return userDB.getAll();
    }
    
    public int deleteUser(String username)throws Exception{
        Users user = userDB.getUser(username);
        int deleted = userDB.deleteUser(user);
        if (deleted == 1){
            return 1;
        }else {
            return 0;
        }
    }
    
    public int updateUser(String username, String password, String email, String firstname, String lastname, boolean active, boolean isadmin) throws Exception{
        Users user = getUser(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setFirstName(firstname);
        user.setLastName(lastname);
        user.setActive(active);
        user.setIsAdmin(isadmin);
        
        int updated = userDB.updateUser(user);
        if (updated ==1 ){
            return 1;
        }else{
            return 0;
        }
    }
    
    public int insertUser(String username, String password, String email, String firstname, String lastname, boolean active, boolean isadmin) throws Exception{
        Users user = new Users();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setFirstName(firstname);
        user.setLastName(lastname);
        user.setActive(active);
        user.setIsAdmin(isadmin);
        
        int inserted = userDB.insertUser(user);
        if (inserted ==1){
            return 1;
        }else {
            return 0;
        }
    }
    
}
