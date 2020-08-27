/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import dataaccess.UsersDB;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.Users;
import services.AccountService;

/**
 *
 * @author 797138
 */
public class ResetPasswordServlet extends HttpServlet {


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
            String username = request.getParameter("Account");
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            getServletContext().getRequestDispatcher("/WEB-INF/resetPassword.jsp").forward(request, response);
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
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("passwordConfirm");
            HttpSession session = request.getSession();
            AccountService as = new AccountService();
            UsersDB udb = new UsersDB();
            if (password.equals(confirmPassword)){
                try {
                    String username = (String) session.getAttribute("username");
                    Users u = as.getUser(username);
                    u.setPassword(password);
                    udb.updateUser(u);
                    request.setAttribute("logOutMessage", "Account password was reset!");
                    getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
                } catch (Exception ex) {
                    Logger.getLogger(ResetPasswordServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
               
            }else{
                request.setAttribute("Message", "Passwords do not match up!");
                getServletContext().getRequestDispatcher("/WEB-INF/resetPassword.jsp").forward(request, response);
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
