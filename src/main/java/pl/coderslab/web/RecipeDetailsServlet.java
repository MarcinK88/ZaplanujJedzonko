package pl.coderslab.web;

import pl.coderslab.dao.RecipeDao;
import pl.coderslab.model.Recipe;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/app/recipe/details")
public class RecipeDetailsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        RecipeDao recipeDao = new RecipeDao();
        Recipe recipeDetails = recipeDao.read(id);
        req.setAttribute("recipeDetails", recipeDetails);
        getServletContext().getRequestDispatcher("/WEB-INF/app/recipeDetails.jsp").forward(req,resp);
    }
}
