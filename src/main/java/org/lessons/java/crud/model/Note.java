package org.lessons.java.crud.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Note {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotNull
    @Size(min=2, max=255)
	@Column(name = "descrizione", nullable = false)
    private String descrizione;
    
    @Column(name = "created_at")
	private LocalDateTime createdAt;

    @ManyToOne
	@JoinColumn(name="ticket_id", nullable = false) 
    @JsonBackReference
    
    private Ticket ticket;
    
    @ManyToOne
	@JoinColumn(name="user_id", nullable = false)
    @JsonBackReference
	private User user; 
    
    public Note() {
    	this.createdAt = LocalDateTime.now();
    }
    
    public Note(User user, Ticket ticket) {
        this.createdAt = LocalDateTime.now();
        this.user = user;
        this.ticket = ticket;
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@JsonIgnore
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime dataCreazione) {
		this.createdAt = dataCreazione;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}
	
	@JsonIgnore
    public String getDataCreazioneFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return createdAt.format(formatter);
    }
}