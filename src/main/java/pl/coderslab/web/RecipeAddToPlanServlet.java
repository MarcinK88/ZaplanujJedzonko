package pl.coderslab.web;

import pl.coderslab.dao.DayNameDao;
import pl.coderslab.dao.PlanDao;
import pl.coderslab.dao.RecipeDao;
import pl.coderslab.dao.RecipePlanDao;
import pl.coderslab.model.*;

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

        //lista dni
        List<DayName> dayNames = new ArrayList<>();
        DayNameDao dayNameDao = new DayNameDao();
        dayNames = dayNameDao.findAll();
        httpSession.setAttribute("dayNames", dayNames);

        req.getServletContext().getRequestDispatcher("/recipeAddToPlan.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //przekazywane parametry:
        //req.getParameter("planId")
        //req.getParameter("mealName")
        //req.getParameter("displayOrder")
        //req.getParameter("recipeId")
        //req.getParameter("dayNameId")

        RecipePlanDao recipePlanDao = new RecipePlanDao();


        RecipePlan recipePlanToAdd = new RecipePlan();
//        = recipePlanDao.create(req.getParameter("planId"), req.getParameter("mealName"), req.getParameter("displayOrder"), req.getParameter("recipeId"),req.getParameter("dayNameId"));
        recipePlanToAdd.setPlanId(Integer.parseInt(req.getParameter("planId")));
        recipePlanToAdd.setMealName(req.getParameter("mealName"));
        recipePlanToAdd.setDisplayOrder(Integer.parseInt(req.getParameter("displayOrder")));
        recipePlanToAdd.setRecipeId(Integer.parseInt(req.getParameter("recipeId")));
        recipePlanToAdd.setDayNameId(Integer.parseInt(req.getParameter("dayNameId")));
        recipePlanDao.create(recipePlanToAdd);

        resp.sendRedirect("/app/recipe/plan/add/");
    }
}
