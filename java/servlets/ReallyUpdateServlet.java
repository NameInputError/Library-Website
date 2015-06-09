/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import business.Record;
import business.Store;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author TR
 */
public class ReallyUpdateServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String msg="", URL="/Update.jsp", sql;
        
        sql = "UPDATE bookinv SET OnHand = " + request.getParameter("quantity") + 
                " where storeID = " + request.getParameter("storeid") + 
                " and bookID = '" + request.getParameter("bookcd") + "'";
        
        String dbURL = "jdbc:mysql://localhost:3306/henrybooks_is252";
        String dbUser ="root";
        String dbPwd = "sesame";
        
        try {
        Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPwd);
        Statement s = conn.createStatement();
        
        int r = s.executeUpdate(sql);
        
        if (r == 0) {
                msg += "Error: update was not successful";
            } else if (r == 1) {
                msg += "Update Successful.";
                URL = "/StoreSelection.jsp";
            } else {
                msg += "Warning: " + r + " records updated.<br>";
            }
        
        } catch (SQLException e) {
            msg = "Sql Exception: " + e.getMessage();
        } catch (Exception e) {
            msg = "General error: " + e.getMessage();
        }
        
        request.setAttribute("msg", msg);
        RequestDispatcher disp = getServletContext().getRequestDispatcher(URL);
        disp.forward(request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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
