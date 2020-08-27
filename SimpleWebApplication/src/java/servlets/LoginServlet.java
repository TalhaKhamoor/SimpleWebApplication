/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import dataaccess.UsersDB;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.Users;
import services.AccountService;
import services.GmailService;

/**
 *
 * @author 797138
 */
public class LoginServlet extends HttpServlet {
    AccountService as = new AccountService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String logoutClicked = request.getParameter("logout");
        String registrating = request.getParameter("registerAccount");
        System.out.println(registrating);
        if (registrating !=null){
            try {
                Users u = as.getUser(registrating);
                String email = u.getEmail();
                String subject = "Welcome to Nventory";
                String body = "Welcome to Nvnentoy " + u.getFirstName() + " Hope you enjoy my services!";
                GmailService.sendMail(email, subject, body, false);
                AccountService as = new AccountService();
                u.setActive(true);
                UsersDB udb = new UsersDB();
                udb.updateUser(u);
                request.setAttribute("activatedMessage", registrating + " account activated");
            } catch (Exception ex) {
                Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (logoutClicked != null){
            session.removeAttribute("username");
            request.setAttribute("logOutMessage", "log out successfull!");
            getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
        }else if (logoutClicked == null && session.getAttribute("username") == null){
            getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
        }else if (logoutClicked == null && session.getAttribute("username") != null){
            response.sendRedirect(request.getContextPath() + "/inventory");
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
        
       HttpSession session = request.getSession();
       String username = request.getParameter("username");
       String password = request.getParameter("password");
       String registerClicked = request.getParameter("register");
       
       boolean userNameIncorrect = false;
       
       List<Users> users = new ArrayList<>();
       
       try{
           users = as.getAll();
       }catch (Exception ex){
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
       }
        System.out.println(registerClicked);
       if (registerClicked == null){
           for (Users u: users){
                if (u.getUsername().equals(username)){
                    session.setAttribute("username", username);
                    if(u.getPassword().equals(password)){
                         if (u.getActive()){
                             if (u.getIsAdmin()){
                                 response.sendRedirect("admin");
                                 return;
                             } else{
                                
                                 response.sendRedirect("inventory");
                                 return; 
                             }  
                         } else{
                             session.setAttribute("username", username);
                             request.setAttribute("userNameIncorrect", false);
                             request.setAttribute("passIncorrect", false);
                             request.setAttribute("isActive", true);
                             getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request,response);
                             return;
                         }
                    } else{
                        session.setAttribute("username", username);
                        request.setAttribute("userNameIncorrect", false);
                        request.setAttribute("passIncorrect", true);
                        getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
                        return;
                    }   
                 } else{
                     userNameIncorrect = true;
                     session.setAttribute("username", "");
                     request.setAttribute("userNameIncorrect", userNameIncorrect);
                 }
            }
            if (userNameIncorrect){
                getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);

       }
       }else{
           
           String userNameRegister = request.getParameter("userNameRegister");
           String passwordRegister = request.getParameter("passwordRegister");
           String passwordConfirmRegister = request.getParameter("passwordRegisterConfirm");
           String firstNameRegister = request.getParameter("firstNameRegister");
           String lastNameRegister = request.getParameter("lastNameRegister");
           String emailRegister = request.getParameter("emailRegister");
           boolean goThrough = true;
           for (Users u: users){
               
               if (u.getUsername().equals(userNameRegister)){
                   goThrough = false;
                   request.setAttribute("userNameUsed", true);
               }
               if (u.getEmail().equals(emailRegister)){
                   goThrough = false;
                   request.setAttribute("emailUsed", true);
               }
           }
           
           if (!passwordRegister.equals(passwordConfirmRegister)){
                   goThrough = false;
                   request.setAttribute("passwordUnequal", true);
           }
           if (goThrough){
               try {
                   as.insertUser(userNameRegister, passwordRegister, emailRegister, firstNameRegister, lastNameRegister, false, false);
                   String email = emailRegister;
                   String subject = "Activate Nventory Account";
                   String template = getServletContext().getRealPath("/WEB-INF/emailtemplates/login.html");

                   HashMap<String, String> tags = new HashMap<>();
                   tags.put("firstname", firstNameRegister);
                   tags.put("registration", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/login?registerAccount=" + userNameRegister);
                   tags.put("date", ((new java.util.Date())).toString());
                   GmailService.sendMail(email, subject, template, tags);
                   request.setAttribute("activatedMessage", "Please check your email to activate your account!");
                   getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
               } catch (Exception ex) {
                   Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
               }

           }else{
               System.out.println();
                getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
           }
           
           
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
