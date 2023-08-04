import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.config.Task;

import java.sql.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try (
                Connection con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/Engeto_Project2_Registration",
                        "root",
                        "EngetoJavaHeslo123*"))
        {
            Statement statement = con.createStatement();
            statement.executeQuery(
                    "SELECT * FROM registrationinfo;");
            ResultSet result = statement.getResultSet();
            while (result.next()) {
                System.out.println(
                        result.getString("name")
                                +": "
                                + result.getInt("ID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}