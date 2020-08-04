package com.txst.restapi.model;

import com.txst.restapi.lib.DBModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Invoice extends DBModel {

    private int invoiceId;
    private int pId;
    private String item_Name;
    private double price;
    private int quantity;

    public int getinvoiceId() { return this.invoiceId; }

    public int getpId() { return  this.pId; }

    public String getItem_Name() { return this.item_Name; }

    public double getPrice() { return  this.price; }

    public int getQuantity() { return this.quantity; }

    public Invoice(int invoiceId){
        this.invoiceId = invoiceId;
        if(invoiceId != -1){
            populateInvoice(invoiceId);
        }
    }

    public void populateInvoice(final int invoiceId){
        final String invoiceQuery = String.format("SELECT * FROM invoice WHERE invoice_id= %s", invoiceId);
        try{
            ResultSet resultSet = executeQuery(invoiceQuery);
            if(resultSet.next()){
                this.item_Name = resultSet.getString("Item_name");
                this.price = resultSet.getDouble("Price");
                this.quantity = resultSet.getInt("Quantity");
                this.pId = resultSet.getInt("Project_id");
            } else {
                System.out.println("Unable to find invoice with id: "+ invoiceId);
                return;
            }

            if(!resultSet.isClosed()) {
                resultSet.close();
            }
        }catch (Exception e) {
            System.out.println("Exception while getting the invoice: " + e);
        } finally {
            close();
        }
    }

    public int createInvoice(final String item_Name, final double price,final int quantity ,final int pId){
        final String createQuery = "INSERT INTO paparazzi_db.invoice (Item_name, Price, Quantity, Project_id) VALUES(?,?,?,?)";
        try{
            PreparedStatement preparedStatement
                    = getConnection().prepareStatement(createQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, item_Name);
            preparedStatement.setDouble(2,price);
            preparedStatement.setInt(3,quantity);
            preparedStatement.setInt(4, pId);
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if(resultSet.next()){
                this.invoiceId = resultSet.getInt(1);
                this.item_Name = item_Name;
                this.price = price;
                this.quantity = quantity;
                this.pId = pId;
            } else {
                System.out.println(("Unable to create a invoice"));
                return -1;
            }

        }catch (Exception e){
            System.out.println("Exception while create a invoice" + e);
        }finally {
            close();
        }
        return this.invoiceId;
    }

    public static List<Invoice> getInvoices(int pId){
        List<Invoice> invoices = new ArrayList<>();
        final String invoiceQuery = String.format("SELECT * FROM invoice WHERE Project_id = %s", pId);
        try {
            ResultSet resultSet = executeQuery(invoiceQuery);
            while(resultSet.next()){
                Invoice invoice = new Invoice(-1);
                invoice.invoiceId = resultSet.getInt("Invoice_id");
                invoice.item_Name = resultSet.getString("Item_Name");
                invoice.price = resultSet.getDouble("Price");
                invoice.quantity = resultSet.getInt("Quantity");
                invoice.pId = resultSet.getInt("Project_id");
                invoices.add(invoice);
            }

        } catch (Exception e) {
            System.out.println("Exception while displaying the list of invoices" + e);
        } finally {
            close();
        }
        return invoices;
    }

}
