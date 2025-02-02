package com.inventarios.prueba_tecnica.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import com.inventarios.prueba_tecnica.Entity.Ticket;
import com.inventarios.prueba_tecnica.Repository.TicketRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;


@Service
public class TicketService {
    @Autowired
    private TicketRepository repository;

    public Page<Ticket> getAll(Pageable pageable){
        return repository.findAll(pageable);
    }

    @Transactional
    public Ticket createTicket(Ticket ticket){
        try {
        return repository.save(ticket);
        }catch (Exception ex) {
        throw ex;
        }
    }

    public Page<Ticket> getTicketsByUserId(Integer userId, Pageable pageable) {
        return repository.findByUserId(userId, pageable);
    }

    public void deleteTicket(Integer id){
        repository.deleteById(id);
    }   

    @Transactional
    public Ticket updateTicket(Ticket ticket, Integer id){
        Optional<Ticket> ExistingTicket = repository.findById(id);

        if(ExistingTicket.isPresent()){
            Ticket updateTicket = ExistingTicket.get();
            updateTicket.setTitle(ticket.getTitle());
            updateTicket.setDescription(ticket.getDescription());
            updateTicket.setStatus(ticket.getStatus());

            return repository.save(updateTicket);
        }else {
            throw new EntityNotFoundException("Ticket not found");
        }
    }
}
