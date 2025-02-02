package com.inventarios.prueba_tecnica.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.Date;
import com.inventarios.prueba_tecnica.Entity.Ticket;
import lombok.AllArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketDTO {
    private Integer id;
    private String title;
    private String description;
    private String status;
    private Date dateCreation;
    private Integer user_id;

    public TicketDTO(Ticket ticket) {
        this.id = ticket.getId();
        this.title = ticket.getTitle();
        this.description = ticket.getDescription();
        this.status = ticket.getStatus();
        this.dateCreation = ticket.getDate_creation();
        this.user_id = ticket.getUser().getId();
    }

}
