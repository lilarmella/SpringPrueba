package com.inventarios.prueba_tecnica.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventarios.prueba_tecnica.Entity.User;
import com.inventarios.prueba_tecnica.Service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "Register and Login")
public class UserController {

    @Autowired
    private UserService service;

    @Operation(
      summary = "Register a user with a role")
    @ApiResponses({
      @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json") }),
      @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
      @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return service.register(user);
    }

    @Operation(summary = "Login with a existing user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PostMapping("/login")
    public String login(@RequestBody User user) {
        return service.verify(user);
    }
}