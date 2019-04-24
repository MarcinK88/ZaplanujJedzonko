package pl.coderslab.web;

import pl.coderslab.dao.PlanDao;
import pl.coderslab.model.Plan;
import pl.coderslab.model.RecipePlan;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/app/plan/details")
public class PlanDetailsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession httpSession = req.getSession();
        List<RecipePlan> planToPrint = new ArrayList<>();
        PlanDao planDao = new PlanDao();
        planToPrint = planDao.readRecipePlan(Integer.parseInt(req.getParameter("planId")));
        httpSession.setAttribute("planToPrint",planToPrint);
        Plan plan = new Plan();
        plan = planDao.read(Integer.parseInt(req.getParameter("planId")));
        httpSession.setAttribute("planDescription",plan);
        req.getServletContext().getRequestDispatcher("/detailsSchedules.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
