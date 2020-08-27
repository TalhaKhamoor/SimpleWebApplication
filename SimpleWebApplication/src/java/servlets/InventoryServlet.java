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
@WebServlet(name = "InventoryServlet_1", urlPatterns = {"/InventoryServlet_1"})
public class InventoryServlet extends HttpServlet {
    
    AccountService as = new AccountService();
    Inventory inventory = new Inventory();
    List<Categories> categories = new ArrayList<>();
    List<Items> items = new ArrayList<>();
    
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
            String username = (String) session.getAttribute("username");
            request.setAttribute("selectedUsername", as.getUser(username));
            Users user = null;
            try {
                user = as.getUser(username);
                String firstName = user.getFirstName();
                String lastName = user.getLastName();
                String fullName = firstName + " " + lastName;
                request.setAttribute("fullName", fullName);
            } catch (Exception ex) {
                Logger.getLogger(InventoryServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try {
                categories = inventory.getAllCategories();
                items = inventory.getAll();
            } catch (Exception ex) {
                Logger.getLogger(InventoryServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(user);
            ArrayList<Items> newItemList = loadNewItemList(items, username);
            request.setAttribute("categoryList", categories);
            request.setAttribute("itemList", newItemList);
            request.setAttribute("addEditHeading", "Add Item");
            getServletContext().getRequestDispatcher("/WEB-INF/inventory.jsp").forward(request, response);
        } catch (Exception ex) {
            Logger.getLogger(InventoryServlet.class.getName()).log(Level.SEVERE, null, ex);
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
        try {
            HttpSession session = request.getSession();
            Users actualUser = as.getUser((String) session.getAttribute("username"));
            String action = request.getParameter("action");
            
            request.setAttribute("addEditHeading", "Add Item");
            if (action.equals("delete")){
                String selectedItem = request.getParameter("selectedItem");
                inventory.deleteItem(Integer.parseInt(selectedItem), actualUser.getUsername());
            }else if (action.equals("edit")){
                    String userNameEdit = request.getParameter("userNameEdit");
                    String oldPassEdit = request.getParameter("passwordOldEdit");
                    String newPassEdit = request.getParameter("passwordNewEdit");
                    String confirmPassEdit = request.getParameter("passwordConfirmEdit");
                    String firstNameEdit = request.getParameter("firstNameRegister");
                    String lastNameEdit = request.getParameter("lastNameRegister");
                    String emailEdit = request.getParameter("emailRegister");
                    
                    
                    if(oldPassEdit.equals(actualUser.getPassword())){
                        // other checks
                        if(newPassEdit.equals(confirmPassEdit)){
                            as.updateUser(userNameEdit, newPassEdit, emailEdit, firstNameEdit, lastNameEdit, true, actualUser.getIsAdmin());
                            request.setAttribute("fullName", firstNameEdit + " " + lastNameEdit);
                            request.setAttribute("userEdited", true);
                        } else{
                            request.setAttribute("editOldNewPassUnequal", true);
                        }
                    }else{
                        request.setAttribute("editWrongPass", true);
                    }
                getServletContext().getRequestDispatcher("/WEB-INF/inventory.jsp").forward(request, response);
            } else if(action.equals("editItem")){
                String selectedItemID = request.getParameter("selectedItem");
                Items item = inventory.getItem(Integer.parseInt(selectedItemID));
                request.setAttribute("categorySelected", item.getCategory().getCategoryName());
                request.setAttribute("itemName", item.getItemName());
                request.setAttribute("price", (int)item.getPrice());
                request.setAttribute("addEditHeading", "Edit Item");
                int itemID = item.getItemID();
                session.setAttribute("itemID", itemID);
                session.setAttribute("editBoolean", true);
                
            } else if(action.equals("save")){
                Categories itemCategory = inventory.getCategory(Integer.parseInt(request.getParameter("category")));
                String itemName = request.getParameter("itemName");
                double itemPrice = Double.parseDouble(request.getParameter("price"));
                try{
                    if ((boolean)session.getAttribute("editBoolean")){
                        int itemID = (int) session.getAttribute("itemID");
                        inventory.updateItem(itemCategory, itemID, itemName, itemPrice);
                        session.removeAttribute("itemID");
                        session.removeAttribute("editBoolean");                
                    }
                } catch(NullPointerException e){
                    inventory.insertItem(itemCategory, itemName, itemPrice, actualUser);
                }
            } else if(action.equals("deactivate")){
                String password = request.getParameter("deactivePass");
                if(actualUser.getPassword().equals(password)){
                    as.updateUser(actualUser.getUsername(), actualUser.getPassword(), actualUser.getEmail(), actualUser.getFirstName(), actualUser.getLastName(), false, false);
                    session.removeAttribute("username");
                    request.setAttribute("deactivated", true);
                    getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
                    return;
                }else{
                    request.setAttribute("deactivateError", true);
                }
            }
            request.setAttribute("selectedUsername", actualUser);
            categories = inventory.getAllCategories();
            items = inventory.getAll();
            ArrayList<Items> newItemList = loadNewItemList(items, actualUser.getUsername());
            request.setAttribute("categoryList", categories);
            request.setAttribute("itemList", newItemList);
            actualUser = as.getUser((String)session.getAttribute("username"));
            String firstName = actualUser.getFirstName();
            String lastName = actualUser.getLastName();
            String fullName = firstName + " " + lastName;
            request.setAttribute("fullName", fullName);
            getServletContext().getRequestDispatcher("/WEB-INF/inventory.jsp").forward(request, response);
        } catch (Exception ex) {
            Logger.getLogger(InventoryServlet.class.getName()).log(Level.SEVERE, null, ex);
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

    private ArrayList<Items> loadNewItemList(List<Items> items, String username) {
        ArrayList<Items> newItemList = new ArrayList<>();
        for(Items i: items){
                if (i.getOwner().getUsername().equals(username)){
                    newItemList.add(i);
                }
            }
        return newItemList;
    }

}
