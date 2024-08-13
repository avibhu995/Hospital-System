package HospitalMananagementSystem;

import java.sql.*;
import java.util.Scanner;
import java.sql.Connection;

public class HospitalManagementSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "Awanish@123";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
        Scanner scanner = new Scanner(System.in);
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Patient patient;
            patient = new Patient(connection, scanner);
            Doctor doctor = new Doctor(connection, scanner);
            while (true) {
                System.out.println("Hospital Management System");
                System.out.println("1. Add Patient");
                System.out.println("2. view Patient ");
                System.out.println("3. Appointment ");
                System.out.println("5.Exit");
                System.out.println("Enter your choice :");
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        //Add patient
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        // View Patient
                        patient.viewPatients();
                        System.out.println();
                        break;
                    case 3:
                        // view doctors
                        doctor.viewDoctors();
                        System.out.println();
                        break;
                    case 4:

                        // Book  Appointment
                        bookAppointment(patient, doctor, connection, scanner);
                        System.out.println();
                        break;
                    case 5:
                        System.out.println("THANK YOU! FOR USING HOSPITAL MANAGEMENT SYSTEM!!");
                        return;
                    default:
                        System.out.println("Enter valid choice !!!");
                        break;
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static boolean bookAppointment(Patient patient, Doctor doctor, Connection connection, Scanner scanner) {
        System.out.println("Enter Patient Id:");
        int patientId = scanner.nextInt();
        System.out.println("Enter Doctor Id:");
        int doctorId = scanner.nextInt();
        System.out.println("Enter appointment data(YYYY-MM-DO):");
        String appointmentDate = scanner.next();
        if (patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)) {
            if (checkDoctorAvailability(doctorId, appointmentDate, connection)) {
                String appointmentQuery = "INSERT INTO appointments(patient_id, doctor_id, appointment_date) VALUES(?, ?, ?)";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1, patientId);
                    preparedStatement.setInt(2, doctorId);
                    preparedStatement.setString(3, appointmentDate);
                    int rowAffect = preparedStatement.executeUpdate();
                    if (rowAffect > 0) {
                        System.out.println("Appointment Booked!");
                    } else {
                        System.out.println("Failed to Book Appointment!! ");
                    }
                } catch (SQLException e) {
                    System.out.println(e);
                }
            } else {
                System.out.println("Doctor not Available on this date!!!");
            }
        } else {
            System.out.println("Either doctor or patient doesn't exist!!!");
        }
        return true;
    }
        public static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection connection){
            String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, doctorId);
                preparedStatement.setString(2, appointmentDate);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count == 0;
                }
            }catch(SQLException e){
                   e.printStackTrace();
            }
            return false;
        }
    }

