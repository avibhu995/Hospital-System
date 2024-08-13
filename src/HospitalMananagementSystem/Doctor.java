package HospitalMananagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
public class Doctor{
    private Connection connection;

    public Doctor (Connection connection, Scanner scanner) {
        this.connection = connection;
    }

    public void viewDoctors() {
        String query = "select*from doctors" ;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Doctors:");
            System.out.println("| Doctor Id       |Name            | Specialization   |");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String specialization = resultSet.getString("specialization");
                System.out.printf("|%-12s|%-20s|%-10s|%-18s|\n",id , name,specialization);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public boolean getDoctorById(int id) {
        String sql = "SELECT*FROM doctor WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException exx) {
            exx.printStackTrace();
        }
        return false;
    }
}
