package com.neology.parking.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neology.parking.dao.NeoAdminService;
import com.neology.parking.util.exception.CommonUnathorizedException;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("neo")
public class NeoAdminController {

    private final NeoAdminService adminService;

    public NeoAdminController(NeoAdminService adminService) {
        this.adminService = adminService;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/mes/reiniciar")
    public String postMethodName(@RequestHeader(name = "Operation-Auth", required = false) String operationToken) {
        if (!"Neo-Approved".equals(operationToken)) {
            throw new CommonUnathorizedException("La opperacion necesita una autorizacion especial");
        }
        adminService.restartTracking();
        return "{\"operationSucess\":true}";
    }

}
