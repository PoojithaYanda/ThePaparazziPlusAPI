package com.txst.restapi.model;

import com.txst.restapi.lib.DBModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Checklist extends DBModel {

    private int cId = -1;
    private int pId = -1;
    private String item_Name = "";
    private String date = "";

    public int getcId() { return this.cId; }

    public int getpId() { return  this.pId; }

    public String getItem_Name() { return this.item_Name; }

    public String getDate() { return  this.date; }

    public Checklist(int cId){
        this.cId = cId;
        if(cId != -1){
            populateChecklist(cId);
        }
    }

    public void populateChecklist(final int cId){
        final String checklistQuery = String.format("SELECT * FROM checklist WHERE checklist_id= %s", cId);
        try{
            ResultSet resultSet = executeQuery(checklistQuery);
            if(resultSet.next()){
                this.item_Name = resultSet.getString("Item_name");
                this.date = resultSet.getString("Date");
                this.pId = resultSet.getInt("Project_id");
            } else {
                System.out.println("Unable to find checklist with id: "+ cId);
                return;
            }

            if(!resultSet.isClosed()) {
                resultSet.close();
            }
        }catch (Exception e) {
            System.out.println("Exception while getting the checklist: " + e);
        } finally {
            close();
        }
    }

    public int createChecklist(final String item_Name, final String date, final int pId){
        final String createQuery = "INSERT INTO paparazzi_db.checklist (Item_name, Date, Project_id) VALUES(?,?,?)";
        try{
            PreparedStatement preparedStatement
                    = getConnection().prepareStatement(createQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, item_Name);
            preparedStatement.setString(2,date);
            preparedStatement.setInt(3, pId);
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if(resultSet.next()){
                this.cId = resultSet.getInt(1);
                this.item_Name = item_Name;
                this.date = date;
                this.pId = pId;
            } else {
                System.out.println(("Unable to create a checklist"));
                return -1;
            }

        }catch (Exception e){
            System.out.println("Exception while create a checklist" + e);
        }finally {
            close();
        }
        return this.cId;
    }

    public static List<Checklist> getChecklists(int pId){
        List<Checklist> checklists = new ArrayList<>();
        final String checklistQuery = String.format("SELECT * FROM checklist WHERE Project_id = %s", pId);
        try {
            ResultSet resultSet = executeQuery(checklistQuery);
            while(resultSet.next()){
                Checklist checklist = new Checklist(-1);
                checklist.cId = resultSet.getInt("Checklist_id");
                checklist.item_Name = resultSet.getString("Item_name");
                checklist.date = resultSet.getString("Date");
                checklist.pId = resultSet.getInt("Project_id");
                checklists.add(checklist);
            }

        } catch (Exception e) {
            System.out.println("Exception while displaying the list of checklists" + e);
        } finally {
            close();
        }
        return checklists;
    }

}
