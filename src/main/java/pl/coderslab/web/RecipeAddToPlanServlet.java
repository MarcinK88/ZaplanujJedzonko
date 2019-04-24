package pl.coderslab.web;

import pl.coderslab.dao.PlanDao;
import pl.coderslab.dao.RecipeDao;
import pl.coderslab.model.Admins;
import pl.coderslab.model.Plan;
import pl.coderslab.model.Recipe;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/app/recipe/plan/add/")
public class RecipeAddToPlanServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession httpSession = req.getSession();

        //lista planów
        List<Plan> plans = new ArrayList<>();
        PlanDao planDao = new PlanDao();
        plans = planDao.findAllByAdminId(((Admins) httpSession.getAttribute("loggedAdmin")).getId());
        httpSession.setAttribute("plans",plans);

        //lista przepisów
        List<Recipe> recipes = new ArrayList<>();
        RecipeDao recipeDao = new RecipeDao();
        recipes = recipeDao.findAllByAdminId(((Admins) httpSession.getAttribute("loggedAdmin")).getId());
        httpSession.setAttribute("recipes",recipes);

        req.getServletContext().getRequestDispatcher("/recipeAddToPlan.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
