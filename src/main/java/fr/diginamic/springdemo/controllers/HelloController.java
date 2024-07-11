package fr.diginamic.springdemo.controllers;

import fr.diginamic.springdemo.services.HelloService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A controller for the Hello entity
 * @see HelloService
 * @see fr.diginamic.springdemo.services.HelloService
 * @author AyoubBenziza
 */
@RestController
@RequestMapping("/")
public class HelloController {

    /**
     * The HelloService instance
     * @see HelloService
     */
    @Autowired
    private HelloService service;

    /**
     * A simple hello world message
     * @return a string
     */
    @Operation(summary = "Hello world message")
    @ApiResponse(responseCode = "200", description = "Hello world message", content = @Content)
    @GetMapping
    public String helloWorld() {
        return service.salutations();
    }
}
