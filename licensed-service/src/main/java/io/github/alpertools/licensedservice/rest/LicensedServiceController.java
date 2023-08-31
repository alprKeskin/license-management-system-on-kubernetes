package io.github.alpertools.licensedservice.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/licensed-service")
@Slf4j
public class LicensedServiceController {

    @GetMapping("/server-control")
    public ResponseEntity<String> serverControl() {
        System.out.println("Licensed service is accessible!");
        return ResponseEntity.ok("Licensed service is accessible!");
    }

}
