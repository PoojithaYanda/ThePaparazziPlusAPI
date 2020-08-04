package com.txst.restapi.model;

import com.txst.restapi.lib.DBModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class User extends DBModel {

    private int userId;
    private String userName;
    private String emailAddress;
    private int status = 1;
    private String errorMessage;

    public String getUserName() {
        return this.userName;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public int getUserId() {
        return this.userId;
    }

    public int getStatus() {
        return this.status;
    }

    public User(int userId) {
        this.userId = userId;
        if (userId != -1) {
            populateUser(userId);
        }
    }

    public String getErrorMessage(){
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage){
        this.errorMessage = errorMessage;
    }


    private void populateUser(final int userId) {
        final String userQuery = String.format("SELECT * FROM user WHERE user_id = %s", userId);
        try {
            ResultSet resultSet = executeQuery(userQuery);
            if (resultSet.next()) {
                this.userName = resultSet.getString("Full_Name");
                this.emailAddress = resultSet.getString("Email");
                this.status = resultSet.getInt("Status");
            } else {
                System.out.println("Unable to find user with id = " + userId);
                return;
            }

            if (!resultSet.isClosed()) {
                resultSet.close();
            }
        } catch (Exception e) {
            System.out.println("Exception while getting user : " + e);
        } finally {
            close();
        }
    }

    public int createUser(final String userName, final String emailAddress, final String password) {
        final String insertQuery = "INSERT INTO paparazzi_db.user (Full_Name,Email,Password) VALUES (?, ?, ?)";
        try {

            PreparedStatement preparedStatement
                    = getConnection().prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, emailAddress);
            preparedStatement.setString(3, password);
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if(resultSet.next()) {
                this.userId = resultSet.getInt(1);
                this.userName = userName;
                this.emailAddress = emailAddress;
                this.errorMessage = "";
            } else {
                System.out.println("Unable to create User");
                return -1;
            }
        } catch(Exception e){
            System.out.println("Exception while creating a user: " + e);
        }finally {
            close();
        }
        return this.userId;
    }

    public User validateLogin(final String userName, final String password) {
        User user= new User(-1);
        final String validateQuery = String.format("Select * from paparazzi_db.user where email ='%s' and password ='%s'", userName, password);
        try{
            ResultSet resultSet = executeQuery(validateQuery);
            if(resultSet.next()){
                System.out.println("Login validation is successful.");
                this.userId = resultSet.getInt("User_id");
                this.userName = resultSet.getString("Full_Name");
                this.emailAddress= resultSet.getString("Email");
                this.status = resultSet.getInt("Status");
                this.errorMessage = "";
            } else
            {
                this.setErrorMessage("Invalid User Name and Password");
                System.out.println("Invalid User Name and Password");
            }

            if(!resultSet.isClosed()){
                resultSet.close();
            }

        }catch (Exception e){
            System.out.println("Exception while validating user login: " + e);
        } finally {
            close();
        }
        return user;
    }

}
