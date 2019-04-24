package pl.coderslab.web;

import pl.coderslab.dao.PlanDao;
import pl.coderslab.dao.RecipeDao;
import pl.coderslab.model.Admins;
import pl.coderslab.model.Recipe;
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

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RecipeDao recipeDao = new RecipeDao();

        PlanDao planDao = new PlanDao();
        HttpSession httpSession = req.getSession();
        int recipeQuantity = recipeDao.recipeQuantity(((Admins) httpSession.getAttribute("loggedAdmin")).getId());
        int planQuantity = planDao.planQuantity(((Admins) httpSession.getAttribute("loggedAdmin")).getId());
        httpSession.setAttribute("recipeQuantity", recipeQuantity);
        httpSession.setAttribute("planQuantity", planQuantity);

        List<RecipePlan> recipePlanList = new ArrayList<>();
        recipePlanList.addAll(planDao.findLastPlan(((Admins) (httpSession.getAttribute("loggedAdmin"))).getId()));
        if (!recipePlanList.isEmpty()) {

            httpSession.setAttribute("recipePlanList", recipePlanList);

        }
        httpSession.setAttribute("recipePlanName",planDao.getLastPlanName(((Admins) (httpSession.getAttribute("loggedAdmin"))).getId()));
        req.getServletContext().getRequestDispatcher("/dashboard.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
