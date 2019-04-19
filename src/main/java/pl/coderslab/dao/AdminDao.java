package pl.coderslab.dao;

import org.mindrot.jbcrypt.BCrypt;
import pl.coderslab.exception.NotFoundException;
import pl.coderslab.model.Admins;
import pl.coderslab.utils.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminDao {

    private static final String CREATE_ADMIN_QUERY = "INSERT INTO admins(first_name, last_name, email, password, superadmin, enable) VALUES (?, ?, ?, ?, ?, ?);";
    private static final String DELETE_ADMIN_QUERY = "DELETE FROM admins WHERE id = ?;";
    private static final String FIND_ALL_ADMINS_QUERY = "SELECT * FROM admins;";
    private static final String READ_ADMINS_QUERY = "SELECT * FROM admins WHERE id = ?;";
    private static final String UPDATE_ADMINS_QUERY = "UPDATE admins SET first_name = ? last_name = ?, email = ?, password = ?, superadmin = ?, enable = ? WHERE id = ?;";
    private static final String READ_ADMIN_BY_EMAIL_QUERY = "SELECT * FROM admins WHERE email = ?;";

    public Admins read(Integer adminId) {
        Admins admins = new Admins();
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(READ_ADMINS_QUERY)
        ) {
            statement.setInt(1, adminId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    admins.setId(resultSet.getInt("id"));
                    admins.setFirstName(resultSet.getString("first_name"));
                    admins.setLastName(resultSet.getString("last_name"));
                    admins.setEmail(resultSet.getString("email"));
//                    admins.setPassword(BCrypt.hashpw(resultSet.getString("password"),BCrypt.gensalt()));
                    admins.setPassword(resultSet.getString("password"));
                    admins.setSuperadmin(resultSet.getInt("superadmin"));
                    admins.setEnable(resultSet.getInt("enable"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return admins;

    }

    public List<Admins> findAll() {
        List<Admins> adminList = new ArrayList<>();
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_ADMINS_QUERY);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Admins adminToAdd = new Admins();
                adminToAdd.setId(resultSet.getInt("id"));
                adminToAdd.setFirstName(resultSet.getString("first_name"));
                adminToAdd.setLastName(resultSet.getString("last_name"));
                adminToAdd.setEmail(resultSet.getString("email"));
                adminToAdd.setPassword(resultSet.getString("password"));
                adminToAdd.setSuperadmin(resultSet.getInt("superadmin"));
                adminToAdd.setEnable(resultSet.getInt("enable"));
                adminList.add(adminToAdd);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adminList;

    }

    public Admins create(Admins admins) {
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement insertStm = connection.prepareStatement(CREATE_ADMIN_QUERY,
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            insertStm.setString(1, admins.getFirstName());
            insertStm.setString(2, admins.getLastName());
            insertStm.setString(3, admins.getEmail());
            insertStm.setString(4, BCrypt.hashpw(admins.getPassword(),BCrypt.gensalt()));
            insertStm.setInt(5, admins.getSuperadmin());
            insertStm.setInt(6, admins.getEnable());
            int result = insertStm.executeUpdate();

            if (result != 1) {
                throw new RuntimeException("Execute update returned " + result);
            }

            try (ResultSet generatedKeys = insertStm.getGeneratedKeys()) {
                if (generatedKeys.first()) {
                    admins.setId(generatedKeys.getInt(1));
                    return admins;
                } else {
                    throw new RuntimeException("Generated key was not found");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void delete(Integer adminId) {
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_ADMIN_QUERY)) {
            statement.setInt(1, adminId);
            statement.executeUpdate();

            boolean deleted = statement.execute();
            if (!deleted) {
                throw new NotFoundException("Product not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Admins admins) {
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_ADMINS_QUERY)) {
            statement.setInt(7, admins.getId());
            statement.setString(1, admins.getFirstName());
            statement.setString(2, admins.getLastName());
            statement.setString(3, admins.getEmail());
            statement.setString(4, BCrypt.hashpw(admins.getPassword(),BCrypt.gensalt()));
            statement.setInt(5, admins.getSuperadmin());
            statement.setInt(6, admins.getEnable());

            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public Boolean checkPassword(String mail, String password) {
        Boolean isCorrect = false;
        Admins admins = new Admins();
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(READ_ADMIN_BY_EMAIL_QUERY)
        ) {

            statement.setString(1, mail);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
//                    admins.setPassword(BCrypt.hashpw(resultSet.getString("password"),BCrypt.gensalt()));
                    admins.setPassword(resultSet.getString("password"));

                }

                if (BCrypt.checkpw(password, admins.getPassword())){

                    isCorrect = true;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isCorrect;

    }

}
