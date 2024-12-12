package org.example.elprisappbackend.controller;

import org.example.elprisappbackend.service.ElprisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Autowired
    private ElprisService elprisService;

    @GetMapping("/elpriser")
    public String getElpriser() {
        return elprisService.getElpriser();
    }
}
