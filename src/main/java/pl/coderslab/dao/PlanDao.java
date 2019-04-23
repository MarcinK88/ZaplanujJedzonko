package pl.coderslab.dao;

import pl.coderslab.exception.NotFoundException;
import pl.coderslab.model.Plan;
import pl.coderslab.model.RecipePlan;
import pl.coderslab.utils.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlanDao {

    private static final String CREATE_PLAN_QUERY = "INSERT INTO plan(name,description,created,admin_id) VALUES (?,?,?,?);";
    private static final String DELETE_PLAN_QUERY = "DELETE FROM plan WHERE id = ?;";
    private static final String FIND_ALL_PLAN_QUERY = "SELECT * FROM plan;";
    private static final String UPDATE_PLAN_QUERY = "UPDATE plan SET name = ? , description = ?, created = ?, admin_id = ? where id = ?;";
    private static final String READ_PLAN_QUERY = "SELECT * FROM plan WHERE id = ?;";
    private static final String GET_PLAN_QUANTITY = "SELECT COUNT(*) as counter FROM plan WHERE admin_id = ?;";
    private static final String GET_LAST_PLAN_NAME = "select name from plan where id = (SELECT MAX(id) from plan WHERE admin_id = ?);";
    private static final String GET_LAST_PLAN = "SELECT day_name.name as day_name, meal_name,  recipe.name as recipe_name, recipe.description as recipe_description\n" +
            "FROM `recipe_plan`\n" +
            "JOIN day_name on day_name.id=day_name_id\n" +
            "JOIN recipe on recipe.id=recipe_id WHERE\n" +
            "recipe_plan.plan_id =  (SELECT MAX(id) from plan WHERE admin_id = ?)\n" +
            "ORDER by day_name.display_order, recipe_plan.display_order;";


    public Plan read(Integer planId) {
        Plan plan = new Plan();
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(READ_PLAN_QUERY)
        ) {
            statement.setInt(1, planId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    plan.setId(resultSet.getInt("id"));
                    plan.setName(resultSet.getString("name"));
                    plan.setDescription(resultSet.getString("description"));
                    plan.setCreated(resultSet.getString("created"));
                    plan.setAdminId(resultSet.getInt("admin_id"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return plan;
    }

    public Integer planQuantity(Integer adminID) {
        int quantity = 0;
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_PLAN_QUANTITY)) {
            statement.setInt(1, adminID);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    quantity = resultSet.getInt("counter");
                }

            }

            System.out.println(quantity);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return quantity;
    }

    public List<Plan> findAll() {
        List<Plan> planList = new ArrayList<>();
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_PLAN_QUERY);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Plan planToAdd = new Plan();
                planToAdd.setId(resultSet.getInt("id"));
                planToAdd.setName(resultSet.getString("name"));
                planToAdd.setDescription(resultSet.getString("desription"));
                planToAdd.setCreated(resultSet.getString("created"));
                planToAdd.setAdminId(resultSet.getInt("admin_id"));
                planList.add(planToAdd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return planList;
    }

    public String getLastPlanName (int adminID) {
        String lastPlanName = "";
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_LAST_PLAN_NAME);

        ) {
            statement.setInt(1, adminID);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            System.out.println(resultSet.getString("name"));
            lastPlanName = resultSet.getString("name");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lastPlanName;
    }

    public List<RecipePlan> findLastPlan(int adminID) {
        List<RecipePlan> recipePlanList = new ArrayList<>();

        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_LAST_PLAN);
             ) {
            statement.setInt(1, adminID);
            try (ResultSet resultSet = statement.executeQuery()) {
//                System.out.println(resultSet.getString("day_name"));
                while (resultSet.next()) {
                    RecipePlan recipePlanToAdd = new RecipePlan();
                    recipePlanToAdd.setDayName(resultSet.getString("day_name"));
                    recipePlanToAdd.setMealName(resultSet.getString("meal_name"));
                    recipePlanToAdd.setRecipeName(resultSet.getString("recipe_name"));
                    recipePlanToAdd.setRecipeDescription(resultSet.getString("recipe_description"));
                    recipePlanList.add(recipePlanToAdd);
                    System.out.println(recipePlanToAdd.toString());

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipePlanList;
    }

    public Plan create(Plan plan) {
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement insertStm = connection.prepareStatement(CREATE_PLAN_QUERY,
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            insertStm.setString(1, plan.getName());
            insertStm.setString(2, plan.getDescription());
            insertStm.setString(3, plan.getCreated());
            insertStm.setInt(4, plan.getAdminId());
            int result = insertStm.executeUpdate();

            if (result != 1) {
                throw new RuntimeException("Execute update returned " + result);
            }

            try (ResultSet generatedKeys = insertStm.getGeneratedKeys()) {
                if (generatedKeys.first()) {
                    plan.setId(generatedKeys.getInt(1));
                    return plan;
                } else {
                    throw new RuntimeException("Generated key was not found");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void delete(Integer planId) {
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_PLAN_QUERY)) {
            statement.setInt(1, planId);
            statement.executeUpdate();

            boolean deleted = statement.execute();
            if (!deleted) {
                throw new NotFoundException("Plan not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Plan plan) {
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_PLAN_QUERY)) {
            statement.setInt(5, plan.getId());
            statement.setString(1, plan.getName());
            statement.setString(2, plan.getDescription());
            statement.setString(3, plan.getCreated());
            statement.setInt(4, plan.getAdminId());

            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}





