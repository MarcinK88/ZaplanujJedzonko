package pl.coderslab.web;

import pl.coderslab.dao.AdminDao;
import pl.coderslab.dao.RecipeDao;
import pl.coderslab.model.Admins;
import pl.coderslab.model.Recipe;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;

@WebServlet("/app/recipe/add")
public class RecipeAddServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getServletContext().getRequestDispatcher("/WEB-INF/app/recipeAdd.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("recipeName");
        String description = req.getParameter("description");
        int preparationTime = Integer.parseInt(req.getParameter("preparationTime"));
        String preparation = req.getParameter("preparation");
        String ingredients = req.getParameter("ingredients");
        String created = new Timestamp(System.currentTimeMillis()).toString();
        String updated = new Timestamp(System.currentTimeMillis()).toString();

        HttpSession session = req.getSession();
        String email = (String)session.getAttribute("loginUserMail");

        AdminDao adminDao = new AdminDao();
        Admins admins = adminDao.readByEmail(email);
        int id = admins.getId();

        Recipe recipe = new Recipe(name,ingredients,description,created,updated,preparationTime,preparation,id);
        RecipeDao recipeDao = new RecipeDao();
        recipeDao.create(recipe);

        resp.sendRedirect("/recipeList.jsp");

    }
}
