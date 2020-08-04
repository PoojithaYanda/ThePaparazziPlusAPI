package com.txst.restapi.controller;

import com.mysql.cj.util.StringUtils;
import com.txst.restapi.model.Invoice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class InvoiceController {

    @GetMapping("/invoice")
    public Invoice getInvoice(@RequestParam("id") int invoiceId) {
        return new Invoice(invoiceId);
    }

    @GetMapping("/project/{pid}/invoices")
    public List<Invoice> getInvoices(@PathVariable("pid") int pId) {
        return Invoice.getInvoices(pId);
    }

    @GetMapping("/project/{pid}/invoice/create")
    public Invoice createInvoice(@RequestParam("item_Name") String item_Name, @RequestParam("price") double price, @RequestParam("quantity") int quantity, @PathVariable("pid") int pId) {
        Invoice invoice = new Invoice(-1);
        if (StringUtils.isEmptyOrWhitespaceOnly(item_Name)
                || price == 0 || price == -1
                || quantity == 0 || quantity == -1
                || pId == 0 || pId == -1) {
            return invoice;
        }

        invoice.createInvoice(item_Name,price,quantity,pId);
        return invoice;
    }

}
