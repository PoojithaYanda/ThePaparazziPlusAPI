package com.txst.restapi.controller;

import com.mysql.cj.util.StringUtils;
import com.txst.restapi.model.Project;

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

}
