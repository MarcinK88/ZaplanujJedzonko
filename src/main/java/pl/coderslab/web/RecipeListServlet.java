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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/app/recipe/list/")
public class RecipeListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AdminDao adminDao = new AdminDao();
        HttpSession session = req.getSession();
        Admins admin = adminDao.readByEmail(session.getAttribute("loggedUserMail").toString());
        List<Recipe> recipies = new ArrayList<>();
        RecipeDao recipeDao = new RecipeDao();
//        recipies.addAll(recipeDao.findAll());

        recipies.addAll(recipeDao.findAllByAdminId(admin.getId()));

        req.setAttribute("recipies", recipies);


//        req.setAttribute("userName", session.getAttribute("loggedUserName"));
//        System.out.println(session.getAttribute("loggedUserMail"));

        session.setAttribute("loggedName", admin.getFirstName());
        session.setAttribute("loggedId", admin.getId());
        req.getServletContext().getRequestDispatcher("/recipeList.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
