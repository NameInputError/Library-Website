/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import business.Store;
import business.User;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author TR
 */
public class LogonServlet extends HttpServlet {

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String sql="", msg="", userid="";
        String URL = "/Logon.jsp";
        long pwdattempt = 0;
        User u = new User(); //fixed for John Smith, must make dynamic
        
        request.getSession().setAttribute("user", u);
        //lots of authentication code here...
        
        //after authentication success build store list for selection screen
        //going to build a collection of store objects that he can use in the
        //store selection screen. but if we build an arraylist now, then we can use
        //the foreach in the jstl once we build the dropdown selection jsp page.
        ArrayList<Store> stores = new ArrayList<Store>();
        
        try {
            userid = request.getParameter("userid").trim();
            pwdattempt = Long.parseLong(request.getParameter("password"));
            
            
            String dbURL = "jdbc:mysql://localhost:3306/henrybooks_is252";
            String dbUser ="root";
            String dbPwd = "sesame";
            Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPwd);
            Statement s = conn.createStatement();
           
            sql = "SELECT * FROM users " +
                    " WHERE UserID = '" + userid + "'";
            
            ResultSet q = s.executeQuery(sql);
            if (q.next()) {
                u = new User();
                u.setUserID(q.getInt("UserID"));
                u.setPassword(q.getInt("UserPassword"));
                u.setPwdAttempt(pwdattempt);
                if (!u.isAuthenticated()) {
                    msg += "Unable to authenticate.<br>";
                } else {
                    u.setUserName(q.getString("UserName"));
                    u.setStoreID(q.getInt("StoreID"));
                    u.setAdminLevel(q.getString("AdminLevel"));
                    msg += "Authenticated!<br>";
                    URL = "/StoreSelection.jsp";
                }
                
                request.getSession().setAttribute("user", u);
                
                if (u.isAuthenticated()) {
                    sql = "SELECT * FROM stores ORDER BY StoreName";
                    ResultSet r = s.executeQuery(sql);
                    while (r.next()) {
                        Store st = new Store();
                        st.setStoreID(r.getInt("StoreID"));
                        st.setStoreName(r.getString("StoreName"));
                        st.setStoreAddr(r.getString("StoreAddr"));
                        st.setStoreEmp(r.getInt("StoreEmp"));
                        stores.add(st);
                    }
                    if (stores.size() > 0) {
                        URL = "/StoreSelection.jsp";
                        request.getSession().setAttribute("stores", stores);
                    } else {
                        msg += "No store recovered from the stores table.<br>";
                    }
                }
                
                } else {
                    msg += "No data returned for user: " + userid + "<br>";
                }
        
        } catch (SQLException e) {
            msg += "SQL Error: " + e.getMessage() + " " + sql + "<br>";
        } catch (Exception e) {
            msg += "General Error: " + e.getMessage() + "<br>";
        }
            
        request.setAttribute("msg", msg);
        Cookie uid = new Cookie("userid", userid);
            uid.setMaxAge(60*10);
            uid.setPath("/");
            response.addCookie(uid);
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
