package com.txst.restapi.controller;

import com.mysql.cj.util.StringUtils;
import com.txst.restapi.model.Checklist;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChecklistController {

    @GetMapping("/checklist")
    public Checklist getChecklist(@RequestParam("id") int cId) {
        return new Checklist(cId);
    }

    @GetMapping("/project/{pid}/checklists")
    public List<Checklist> getChecklists(@PathVariable("pid") int pId) {
        return Checklist.getChecklists(pId);
    }

    @GetMapping("/project/{pid}/checklist/create")
    public Checklist createChecklist(@RequestParam("item_Name") String item_Name, @RequestParam("date") String date, @PathVariable("pid") int pId) {
        Checklist checklist = new Checklist(-1);
        if (StringUtils.isEmptyOrWhitespaceOnly(item_Name)
                || StringUtils.isEmptyOrWhitespaceOnly(date)
                || pId == 0 || pId == -1) {
            return checklist;
        }

        checklist.createChecklist(item_Name,date,pId);
        return checklist;
    }

}
