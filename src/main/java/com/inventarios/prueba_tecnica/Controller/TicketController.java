package com.inventarios.prueba_tecnica.Controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.inventarios.prueba_tecnica.Dto.TicketDTO;
import com.inventarios.prueba_tecnica.Entity.Ticket;
import com.inventarios.prueba_tecnica.Entity.User;
import com.inventarios.prueba_tecnica.Repository.UserRepository;
import com.inventarios.prueba_tecnica.Service.TicketService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/ticket")
@Tag(name = "Tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserRepository userRepository;

    @Operation(summary = "Get all tickets in DB, only for ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/all")
    public Page<TicketDTO> getTicketsOnPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Ticket> ticketPage = ticketService.getAll(pageable);

        return ticketPage.map(TicketDTO::new);
    }

    @Operation(summary = "Get tickets by id, only for USER")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/user-tickets")
    public Page<TicketDTO> getUserTickets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();


        boolean isUserRole = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER"));

        if (!isUserRole) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }

        User user = userRepository.findByName(username).get();
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Ticket> ticketPage = ticketService.getTicketsByUserId(user.getId(), pageable);

        return ticketPage.map(TicketDTO::new);
    }


    @Operation(summary = "Create a ticket, only logged users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PostMapping("/create")
    public String registerTicket(Authentication authentication, @RequestBody Ticket ticket) {
    if (ticket.getTitle() == null || ticket.getTitle().trim().isEmpty() ||
            ticket.getDescription() == null || ticket.getDescription().trim().isEmpty()) {
        throw new IllegalArgumentException("Cannot be null");
    }
    if (ticket.getDate_creation() == null) {
        ticket.setDate_creation(new Date());
    }
    if (ticket.getStatus() == null) {
        ticket.setStatus("Pendiente");
    }
    UserDetails usuario = (UserDetails) authentication.getPrincipal();
    User user = userRepository.findByName(usuario.getUsername()).get();
    ticket.setUser(user);
    ticketService.createTicket(ticket);
    return "OK";
    }

    @Operation(summary = "Update a ticket, only ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PutMapping("/update/{id}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable Integer id, @RequestBody Ticket ticket) {
        try {
            Ticket updatedTicket = ticketService.updateTicket(ticket, id);
            return ResponseEntity.ok(updatedTicket);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a ticket, only ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @DeleteMapping("/remove/{id}")
    public void deleteTicket(@PathVariable("id") Integer id){
        ticketService.deleteTicket(id);
    }
}