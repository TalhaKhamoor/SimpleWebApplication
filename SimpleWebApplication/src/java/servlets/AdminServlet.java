/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.Categories;
import models.Items;
import models.Users;
import services.AccountService;
import services.Inventory;

/**
 *
 * @author 797138
 */
@WebServlet(name = "AdminServlet", urlPatterns = {"/AdminServlet"})
public class AdminServlet extends HttpServlet {

    AccountService as = new AccountService();
    Inventory inv = new Inventory();
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            List<Users> users = new ArrayList<>();
            List<Categories> allCat = inv.getAllCategories();
            
            String action = request.getParameter("action");
            String username = (String) session.getAttribute("username");
            boolean goToAdmin = false;
            try {
                Users user = as.getUser(username);
                if (user.getIsAdmin()){
                    goToAdmin = true;
                }
            } catch (Exception ex) {
                Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            Users user1 = as.getUser(username);
            String firstName = user1.getFirstName();
            String lastName = user1.getLastName();
            String fullName = firstName + " " + lastName;
            request.setAttribute("fullName", fullName);
            if (goToAdmin){
                try{
                    if (action != null && action.equals("edit")){
                        String selectedUsername = request.getParameter("selectedUsername");
                        Users user = as.getUser(selectedUsername);
                        request.setAttribute("selectedUsername", user);
                        request.setAttribute("heading", "Edit User");
                    }else{
                        request.setAttribute("heading", "Add User");
                    }
                }catch(Exception e){
                    Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, e);
                }
                
                
                try {
                    users = as.getAll();
                } catch (Exception ex) {
                    Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                request.setAttribute("users", users);
                request.setAttribute("categories", allCat);
                getServletContext().getRequestDispatcher("/WEB-INF/admin.jsp").forward(request, response);
            }else{
                try {
                    List<Items> items = new ArrayList<>();
                    try {
                        Inventory inventory = new Inventory();
                        items = inventory.getAll();
                    } catch (Exception ex) {
                        Logger.getLogger(InventoryServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    ArrayList<Items> newItemList = new ArrayList<>();
                    for(Items i: items){
                        if (i.getOwner().getUsername().equals(username)){
                            newItemList.add(i);
                        }
                    }
                    request.setAttribute("itemList", newItemList);
                    
                    
                    request.setAttribute(username, as);
                    request.setAttribute("adminErrorMessage", "Only admins can access this page!");
                    request.setAttribute("addEditHeading", "Add Item");
                    getServletContext().getRequestDispatcher("/WEB-INF/inventory.jsp").forward(request, response);
                    
                } catch (IOException | ServletException ex) {
                    Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        } catch (Exception ex) {
            Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try{
            HttpSession session = request.getSession();
            String action = request.getParameter("action");
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String firstname = request.getParameter("firstname");
            String lastname = request.getParameter("lastname");
            String email = request.getParameter("email");
            String selectedUser = request.getParameter("selectedUsername");
            
            
            try{
                if (action.equals("delete")){
                    String sessionUserName = (String) session.getAttribute("username");
                    if(sessionUserName.equals(selectedUser)){
                        request.setAttribute("errorMessage", "An admin can not delete themself!");
                    }else{
                        as.deleteUser(selectedUser);
                    }
                }else if (action.equals("save")){
                    as.insertUser(username, password, email, firstname, lastname, true, true);
                }else if(action.equals("makeAdmin")){
                    Users toUpdate = as.getUser(selectedUser);
                    as.updateUser(toUpdate.getUsername(), toUpdate.getPassword(), toUpdate.getEmail(), toUpdate.getFirstName(), toUpdate.getLastName(), toUpdate.getActive(), true);
                }else if (action.equals("demoteAdmin")){
                    Users toUpdate = as.getUser(selectedUser);
                    as.updateUser(toUpdate.getUsername(), toUpdate.getPassword(), toUpdate.getEmail(), toUpdate.getFirstName(), toUpdate.getLastName(), toUpdate.getActive(), false);
                }else if (action.equals("activate")){
                    Users toUpdate = as.getUser(selectedUser);
                    as.updateUser(toUpdate.getUsername(), toUpdate.getPassword(), toUpdate.getEmail(), toUpdate.getFirstName(), toUpdate.getLastName(), true, toUpdate.getIsAdmin());
                }else if (action.equals("editCategory")){
                    inv.updateCategory(Integer.parseInt(request.getParameter("selectedCategory")),request.getParameter("newCategoryName"));
                }else if(action.equals("deleteCategory")){
                    inv.deleteCategory(Integer.parseInt(request.getParameter("selectedCategory")));
                }else if(action.equals("add")){
                    inv.insertCategory(request.getParameter("addCategory"));
                }else if (action.equals("search")){
                    String searchItem = request.getParameter("searchItem");
                    List<Items> items = inv.getAll();
                    for(Items i: items){
                        if (i.getItemName().equalsIgnoreCase(searchItem)){
                            String itemName = i.getItemName();
                            String itemOwner = i.getOwner().getUsername();
                            String searchOutput = "Item Name: " + itemName + " , Owned by user: " + itemOwner;
                            System.out.println(searchOutput);
                            request.setAttribute("searchResult", searchOutput);
                            break;
                        }else{
                            request.setAttribute("searchResult", "No such item found!");

                        }
                    }
                }
                
            }catch (Exception e){
                System.out.println(e);
                request.setAttribute("errorMessage", "Whoops could not perform that action!");
            }
            
            List<Users> users = null;
            List<Categories> cat = null;
            try {
                cat = inv.getAllCategories();
                users = as.getAll();
            } catch (Exception ex) {
                Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            Users user1 = as.getUser((String)session.getAttribute("username"));
            String firstName = user1.getFirstName();
            String lastName = user1.getLastName();
            String fullName = firstName + " " + lastName;
            request.setAttribute("fullName", fullName);
            request.setAttribute("heading", "Add User");
            request.setAttribute("categories", cat);
            request.setAttribute("users", users);
            getServletContext().getRequestDispatcher("/WEB-INF/admin.jsp").forward(request, response);
        }catch (Exception ex){
            Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null,ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
