package HospitalManagement;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagement {
    private static final String url="jdbc:mysql://localhost:3306/hospital";
    private static final String userName="root";
    private static final String password="root";
    public static void main(String[] args){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

        }  catch(ClassNotFoundException e){
            e.printStackTrace();
        }

        Scanner scanner=new Scanner(System.in);
        try{

            Connection connection= DriverManager.getConnection(url,userName,password);
            Patient patient = new Patient(connection,scanner);
             Doctor doctor=new Doctor(connection);
             while(true){
                 System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                 System.out.println("1. Add Patient");
                 System.out.println("2. view Patients");
                 System.out.println("3. view Doctors");
                 System.out.println("4. Book Appointment");
                 System.out.println("5. Exit");
                 System.out.println("Enter your choice");
                 int choice=scanner.nextInt();
                 switch(choice){
                     case 1:
                         patient.addPatient();
                         System.out.println();
                         break;


                     case 2:
                         patient.viewPatients();
                         System.out.println();
                         break;

                     case 3:
                         doctor.viewDoctors();
                         System.out.println();
                         break;

                     case 4:
                         bookAppointment(patient,doctor,connection,scanner);
                         System.out.println();
                         break;

                     case 5:
                         System.out.println("Thank you for using hospital management system");
                         return;
                     default:
                         System.out.println("Enter valid choice");

                 }


             }
        }
    catch(SQLException e){
            e.printStackTrace();
        }
    }
    public static void bookAppointment(Patient patient,Doctor doctor,Connection connection,Scanner scanner){
        System.out.println("Enter patient id:");
        int patientId=scanner.nextInt();
        System.out.println("Enter doctor id");
        int doctorId=scanner.nextInt();
        System.out.println("Enter appointment date (YYYY-MM-DD):");
        String appointmentDate=scanner.next();
        if(patient.getPatientBYId(patientId) && doctor.getDoctorBYId(doctorId)){
            if(checkDoctorForAvailability(doctorId,appointmentDate,connection)) {
                String appointmentQuery = "INSERT INTO appointments(patient_id,doctor_id,appointment_date) VALUES (?,?,?)";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1, patientId);
                    preparedStatement.setInt(2, doctorId);
                    preparedStatement.setString(3, appointmentDate);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Appointment booked");

                    } else {
                        System.out.println("FAiled to Book appointment");
                    }
                } catch (SQLException e) {
e.printStackTrace();
                }
            }
        else{
                System.out.println("Doctor not available on this date");

            }        }
        else{
            System.out.println("Either doctor or patient doesn't exist");
        }

    }

public static boolean checkDoctorForAvailability(int doctorId,String appointmentDate,Connection connection){
        String query="SELECT COUNT(*) FROM appointments WHERE doctor_id=? AND appointment_date=?";

        try{
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1,doctorId);
            preparedStatement.setString(2,appointmentDate);
            ResultSet resultSet=preparedStatement.executeQuery();
if(resultSet.next()){
    int count =resultSet.getInt(1);
    if(count==0){
        return true;
    }
    else{
        return false;
    }

}        }catch(SQLException e){
            e.printStackTrace();
        }


        return false;}


}
