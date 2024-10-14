package org.lessons.java.crud.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {

	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@JsonIgnore
	@NotNull
	@Size(min=2, max=50)
	@Column(name = "email", nullable = false, unique = true)
	private String email;
	
     @NotNull
     @Size(min=2, max=25)
	 @Column(name = "username", nullable = false, unique = true)
	 private String username;
	
     @JsonIgnore
	@Size(min=2, max=25)
	@Column(name = "nome", nullable = false)
	private String nome;
	
     @JsonIgnore
	@Size(min=2, max=25)
	@Column(name = "cognome", nullable = false)
	private String cognome;
	
	@NotNull
	@JsonIgnore
	@Size(min=2, max=255)
	@Column(name = "password")
	private String password;
	
	@JsonIgnore
	@Column(name = "status", nullable = false)
	private Boolean status;
	
	@JsonIgnore
	@UpdateTimestamp
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@JsonBackReference
	@OneToMany(mappedBy = "user", cascade = { CascadeType.REMOVE})
	private List<Ticket> tickets;
	
	@JsonBackReference
	 @OneToMany(mappedBy = "user",  cascade = { CascadeType.REMOVE})
	    private List<Note> note;
	
	@JsonBackReference
	 @ManyToMany(fetch = FetchType.EAGER)
		private Set<Role> roles;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime updatedAt) {
		this.createdAt = updatedAt;
	}

	public List<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(List<Ticket> ticket) {
		this.tickets = ticket;
	}

	
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Note> getNote() {
		return note;
	}

	public void setNote(List<Note> note) {
		this.note = note;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	
	
}
