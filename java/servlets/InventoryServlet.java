
package servlets;

import business.Record;
import business.Store;
import business.User;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
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
public class InventoryServlet extends HttpServlet {

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String msg="", URL="/StoreSelection.jsp", sql="";
        String storeId = request.getParameter("storeid");
        User u = (User) request.getSession().getAttribute("user");
        Store branch = new Store();
        
        sql = "SELECT bookinv.storeID, bookinv.bookID, title, author, price, OnHand " + 
        "FROM bookinv, booklist WHERE bookinv.bookID = booklist.bookID and " +
        "bookinv.storeID = " + storeId + " ORDER BY bookID";
        
        String dbURL = "jdbc:mysql://localhost:3306/henrybooks_is252";
        String dbUser ="root";
        String dbPwd = "sesame";
        
        try {
        Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPwd);
        Statement s = conn.createStatement();
        
        ArrayList<Record> list = new ArrayList<Record>();
        ResultSet r = s.executeQuery(sql);
        
        while (r.next()){
            Record rec = new Record(r.getInt("StoreID"),
            r.getString("bookID"), r.getString("title"), 
            r.getDouble("price"), r.getInt("OnHand"), r.getString("author"));
            list.add(rec);
        }
        
        if (list.isEmpty()) {
            msg += "No records could be read.";
        } else {
            for (Store st : (ArrayList<Store>) request.getSession().getAttribute("stores")) {
                if (st.getStoreID() == Integer.parseInt(storeId)) {
                    branch = st;
                }
                request.setAttribute("inv", list);
                request.setAttribute("branch", branch);
                URL = "/StoreInfo.jsp";
            }
        }
        } catch (Exception e) {
            msg += e.getMessage();
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
