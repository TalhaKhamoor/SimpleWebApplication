/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import dataaccess.CategoriesDB;
import dataaccess.DBException;
import dataaccess.ItemsDB;
import java.util.List;
import models.Categories;
import models.Items;
import models.Users;

/**
 *
 * @author 797138
 */
public class Inventory {
    private ItemsDB itemDB;
    private CategoriesDB categoriesDB;
    
    public Inventory(){
        itemDB = new ItemsDB();
        categoriesDB = new CategoriesDB();
    }
    
    public Items getItem(int itemId) throws Exception{
        return itemDB.getItem(itemId);
    }
    
    public List<Items> getAll() throws Exception{
        return itemDB.getAll();
    }
    
    public Categories getCategory(int categoryID) throws Exception{
        return categoriesDB.getCategory(categoryID);
    }
    
    public List<Categories> getAllCategories() throws Exception{
        return categoriesDB.getAll();
    }
    
    public int deleteItem (int itemId, String username) throws Exception{
        Items item = itemDB.getItem(itemId);
        int deleted = 0;
        if(item.getOwner().getUsername().equals(username)){
            deleted = itemDB.deleteItem(item);
        }
        if (deleted == 1 ){
            return 1;
        }else{
            return 0;
        }
    }
    
    public int insertItem(Categories category, String itemName, double price, Users owner) throws Exception{
        Items item = new Items();
        item.setCategory(category);
        item.setItemName(itemName);
        item.setPrice(price);
        item.setOwner(owner);
 
        int inserted = itemDB.insertItem(item);
        if (inserted == 1){
            return 1;
        }else {
            return 0;
        }
    }
    
    public int insertCategory(String categoryName) throws DBException{
        Categories cat = new Categories();
        cat.setCategoryName(categoryName);
        int inserted = categoriesDB.insertCategory(cat);
        if (inserted==1){
            return 1;
        }else {
            return 0;
        }
    }
    
    public int updateCategory(int categoryID, String categoryName) throws DBException, Exception{
        Categories cat = getCategory(categoryID);
        cat.setCategoryName(categoryName);
        int inserted = categoriesDB.insertCategory(cat);
        if (inserted==1){
            return 1;
        }else {
            return 0;
        }
    }
    
    public int deleteCategory(int categoryID) throws DBException{
        Categories cat = categoriesDB.getCategory(categoryID);
        int deleted = categoriesDB.deleteCategory(cat);
        if (deleted == 1 ){
            return 1;
        }else{
            return 0;
        }
    }
    
    public int updateItem(Categories category, int itemID, String itemName, double price) throws Exception{
        Items item = getItem(itemID);
        item.setCategory(category);
        item.setItemName(itemName);
        item.setPrice(price);
        
        int updated = itemDB.updateItem(item);
        if (updated ==1){
            return 1;
        }else{
            return 0;
        }
    }
}
