/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

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
import models.Users;
import services.AccountService;
import services.GmailService;

/**
 *
 * @author 797138
 */
public class ForgotPasswordServlet extends HttpServlet {

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
            getServletContext().getRequestDispatcher("/WEB-INF/forgotPassword.jsp").forward(request, response);

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
        String action = (String) request.getParameter("action");
        if (action.equals("forgotPassword")){
            String email = request.getParameter("emailForgot");
            AccountService as = new AccountService();
            List<Users> users = new ArrayList<>();
            boolean emailValid = false;
            String name = null;
            String username = null;
            try {
                users = as.getAll();
                for(Users u: users){
                    if (u.getEmail().equals(email)){
                        name = u.getFirstName();
                        username = u.getUsername();
                        emailValid = true;
                    }
                }
                if (emailValid){
                   String subject = "Forgot Password Request";
                   String template = getServletContext().getRealPath("/WEB-INF/emailtemplates/forgotPasswordReset.html");

                   HashMap<String, String> tags = new HashMap<>();
                   tags.put("firstname", name);
                   tags.put("resetPassword", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/resetPassword?Account=" + username);
                   tags.put("date", ((new java.util.Date())).toString());
                   GmailService.sendMail(email, subject, template, tags);
                   
                   request.setAttribute("message", "Please check your email for a link to change your password!");
                }else{
                    request.setAttribute("message", "The email provided is not registered with Nventory!");
                }
                getServletContext().getRequestDispatcher("/WEB-INF/forgotPassword.jsp").forward(request, response);

            } catch (Exception ex) {
                Logger.getLogger(ForgotPasswordServlet.class.getName()).log(Level.SEVERE, null, ex);
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
