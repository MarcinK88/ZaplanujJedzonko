package pl.coderslab.web;

import pl.coderslab.dao.AdminDao;
import pl.coderslab.dao.PlanDao;
import pl.coderslab.model.Admins;
import pl.coderslab.model.Plan;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;

@WebServlet("/app/plan/add")
public class PlanAddServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getServletContext().getRequestDispatcher("/WEB-INF/app/planAdd.jsp").forward(req,resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String planName = req.getParameter("planName");
        String planDescription = req.getParameter("planDescription");
        String created = new Timestamp(System.currentTimeMillis()).toString();

        HttpSession session = req.getSession();
        String email = (String)session.getAttribute("loggedUserMail");

        AdminDao adminDao = new AdminDao();
        Admins admins = adminDao.readByEmail(email);
        int adminId = admins.getId();

        Plan plan = new Plan(planName, planDescription, created, adminId);

        PlanDao planDao = new PlanDao();
        planDao.create(plan);

        resp.sendRedirect("/app/plan/list");
    }
}
