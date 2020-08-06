package com.txst.restapi.controller;

import com.mysql.cj.util.StringUtils;
import com.txst.restapi.model.Project;
import com.txst.restapi.model.User;
import com.txst.restapi.model.Invoice;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProjectController {

    @GetMapping("/project")
    public Project getProject(@RequestParam("id") int pId) {
        return new Project(pId);
    }

    @GetMapping("/user/{id}/projects")
    public List<Project> getProjects(@PathVariable("id") int userId) {
        return Project.getProjects(userId);
    }

    @GetMapping("/user/{id}/project/create")
    public Project createProject(@RequestParam("projectName") String projectName, @RequestParam("customerName") String customerName, @RequestParam("customerEmail") String customerEmail, @RequestParam("date") String date, @PathVariable("id") int userId) {
        Project project = new Project(-1);
        if (StringUtils.isEmptyOrWhitespaceOnly(projectName)
            ||StringUtils.isEmptyOrWhitespaceOnly(customerName)
                || StringUtils.isEmptyOrWhitespaceOnly(customerEmail)
                || StringUtils.isEmptyOrWhitespaceOnly(date)
                || userId==0 || userId ==-1){
            return project;
        }

        project.createProject(projectName, customerName, customerEmail, date, userId);
        return project;
    }

    @GetMapping("/project/{id}/sendinvoice")
    public String sendInvoice(@PathVariable("id") int projectId) {
        Project project = new Project(projectId);
        User user = new User(project.getUserId());
        System.out.println("Getting project & user is successful.");
        String toEmail = project.getCustomerEmail();
        String fromEmail = user.getEmailAddress();

        // Generate invoice content
        List<Invoice> invoiceList = Invoice.getInvoices(projectId);
        System.out.println("Getting invoices is successful.");
        String subjectContent = String.format("ThePaparazziPlus : Photoshoot Invoice from %s", user.getUserName());
        String invoiceContent = String.format("Invoice for %s \n\n", project.getProjectName());

        String lineFormat = "%15s %15s %15s %15s \n";
        invoiceContent = invoiceContent + String.format(lineFormat, "Item Name", "Price", "Quantity", "Total");

        Double grandTotal = 0.0d, subTotal = 0.0d;
        for(int i = 0; i < invoiceList.size(); i++) {
            Invoice thisInvoice = invoiceList.get(i);
            subTotal = thisInvoice.getPrice() * thisInvoice.getQuantity();
            grandTotal += subTotal;
            invoiceContent += String.format(lineFormat,thisInvoice.getItem_Name(), thisInvoice.getPrice(), thisInvoice.getQuantity(), subTotal);
        }
        invoiceContent += String.format(lineFormat, "Grand Total", " ", " ", grandTotal);

        System.out.println("Ready to send email.");
        return sendEmail(fromEmail, toEmail, subjectContent, invoiceContent);
    }

    private String sendEmail(String from, String to, String subject, String content) {
        String username = "poojithayanda@gmail.com";
        String password = "P@s$w0rd";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS
        prop.put("mail.smtp.starttls.required", "true");
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.user", username);
        prop.put("mail.debug", "true");

        Session session = Session.getDefaultInstance(prop);

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(to));

            message.setSubject(subject);
            message.setSentDate(new Date());
            message.setText(content);

            Transport transport = session.getTransport("smtp");
            transport.connect(username, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

            System.out.println("Done");

            return "Email Sent Successfully";

        } catch (MessagingException e) {
            e.printStackTrace();
            return "Email Sending failed";
        }
    }

}
