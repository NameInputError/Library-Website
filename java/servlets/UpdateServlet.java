
package servlets;

import business.Record;
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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author TR
 */
public class UpdateServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String msg="", URL="/StoreInfo.jsp", sql="";
        String bookcd = request.getParameter("bookcd");
        String storeId = request.getParameter("storeid");
        Store branch;
        
        sql = "SELECT bookinv.storeID, bookinv.bookID, title, author, price, OnHand " + 
        "FROM bookinv, booklist WHERE bookinv.bookID = booklist.bookID and bookinv.bookID = '" + 
                bookcd + "' and " + "bookinv.storeID = " + storeId + " ORDER BY bookID";
        
        String dbURL = "jdbc:mysql://localhost:3306/henrybooks_is252";
        String dbUser ="root";
        String dbPwd = "sesame";
        
        try {
        Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPwd);
        Statement s = conn.createStatement();
        
        
        ResultSet r = s.executeQuery(sql);
        
        if (r.next()){
            Record rec = new Record(r.getInt("StoreID"),
            r.getString("bookID"), r.getString("title"), 
            r.getDouble("price"), r.getInt("OnHand"), r.getString("author"));
            request.setAttribute("record", rec);
            URL = "/Update.jsp";
            for (Store st : (ArrayList<Store>) request.getSession().getAttribute("stores")) {
                if (st.getStoreID() == Integer.parseInt(storeId)) {
                    branch = st;
                    request.setAttribute("branch", branch);
                }
            }
        } else {
            msg = "No records found for book code: " + bookcd + ".";
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
