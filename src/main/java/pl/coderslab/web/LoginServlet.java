package pl.coderslab.web;

import pl.coderslab.dao.AdminDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getServletContext().getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AdminDao credentials = new AdminDao();
        Boolean isPasswordCorrect = credentials.checkPassword(req.getParameter("email"),req.getParameter("password"));
       // resp.getWriter().append(isPasswordCorrect.toString());
        if(isPasswordCorrect) {
            resp.sendRedirect("/");
        } else {

            resp.sendRedirect("/login");
        }
    }
}
