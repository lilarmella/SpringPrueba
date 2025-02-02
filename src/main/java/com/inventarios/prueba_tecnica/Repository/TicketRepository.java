package com.inventarios.prueba_tecnica.Repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import com.inventarios.prueba_tecnica.Entity.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    Page<Ticket> findByUserId(Integer userId, Pageable pageable);
}