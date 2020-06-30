package com.bluebook.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/permission-denied")
    public String permissionDenied() {
        return "permission-denied";
    }

    @GetMapping("/server-problem")
    public String serverProblem() {
        return "server-problem";
    }
}