package com.txst.restapi.model;

import com.txst.restapi.lib.DBModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Project extends DBModel {

    private int pId = -1;
    private int userId = -1;
    private String projectName = "";
    private String customerName = "";
    private String customerEmail = "";
    private String date = "";

    public int getpId(){
        return this.pId;
    }

    public String getProjectName(){
        return this.projectName;
    }

    public String getCustomerName(){
        return this.customerName;
    }

    public String getCustomerEmail(){
        return this.customerEmail;
    }

    public String getDate(){
        return this.date;
    }


    public int getUserId() {
        return this.userId;
    }

    public Project(int pId){
        this.pId = pId;
        if(pId != -1){
            populateProject(pId);
        }
    }

    private void populateProject(final int pId) {
        final String projectQuery = String.format("SELECT * FROM project WHERE project_id = %s", pId);
        try {
            ResultSet resultSet = executeQuery(projectQuery);
            if (resultSet.next()) {
               this.projectName = resultSet.getString("Project_Name");
               this.customerName = resultSet.getString("Customer_Name");
               this.customerEmail = resultSet.getString("Customer_Email");
               this.date = resultSet.getString("Date");
               this.userId = resultSet.getInt("User_id");
            } else {
                System.out.println("Unable to find project with id = " + pId);
                return;
            }

            if (!resultSet.isClosed()) {
                resultSet.close();
            }
        } catch (Exception e) {
            System.out.println("Exception while getting project : " + e);
        } finally {
            close();
        }
    }

    public int createProject(final String projectName, final String customerName, final String customerEmail, final String date, final int userId){
        final String insertQuery = "INSERT INTO paparazzi_db.project (Project_Name, Customer_Name, Customer_Email, Date, User_id) VALUES(?,?,?,?,?)";
        try{
            PreparedStatement preparedStatement
                    = getConnection().prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, projectName);
            preparedStatement.setString(2,customerName);
            preparedStatement.setString(3, customerEmail);
            preparedStatement.setString(4,date);
            preparedStatement.setInt(5,userId);
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if(resultSet.next()){
                this.pId = resultSet.getInt(1);
                this.projectName = projectName;
                this.customerName = customerName;
                this.customerEmail = customerEmail;
                this.date = date;
                this.userId = userId;
            } else {
                System.out.println(("Unable to create a project"));
                return -1;
            }

        }catch (Exception e){
            System.out.println("Exception while create a project" + e);
        }finally {
            close();
        }
            return this.pId;
    }

        public static List<Project> getProjects(int UserId){
        List<Project> projects = new ArrayList<>();
        final String projectQuery = String.format("SELECT * FROM project WHERE User_id = %s", UserId);
            try {
                ResultSet resultSet = executeQuery(projectQuery);
                while(resultSet.next()){
                    Project project = new Project(-1);
                    project.pId = resultSet.getInt("Project_id");
                    project.projectName = resultSet.getString("Project_Name");
                    project.customerName = resultSet.getString("Customer_Name");
                    project.customerEmail = resultSet.getString("Customer_Email");
                    project.date = resultSet.getString("Date");
                    project.userId = resultSet.getInt("User_id");
                    projects.add(project);
                }

                } catch (Exception e) {
                System.out.println("Exception while displaying the list of projects" + e);
            } finally {
                close();
            }
            return projects;
        }
}
