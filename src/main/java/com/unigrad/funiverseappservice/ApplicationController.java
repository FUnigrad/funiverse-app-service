package com.unigrad.funiverseappservice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
@RequestMapping("/{tenant-id}/application")
public class ApplicationController {
    @GetMapping
    public ResponseEntity<String> getStringHello() throws UnknownHostException {
        return ResponseEntity.ok("Application: "+ InetAddress.getLocalHost().getHostName());
    }
}
