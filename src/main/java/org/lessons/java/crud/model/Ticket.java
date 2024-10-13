package org.lessons.java.crud.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tickets")
public class Ticket {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotNull
	@Size(min=2, max=80)
	@Column(name = "titolo", nullable = false)
	private String titolo;

	@NotNull
	@Size(min=2, max=255)
	@Column(name = "descrizione", nullable = false)
	private String descrizione;
	
	@NotNull
	@Size(min=2, max=50)
	@Column(name = "stato", nullable = false)
	private String stato;
	
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	@ManyToOne
	@JoinColumn(name="user_id", nullable = false)
	private User user; 
	
	@OneToMany(mappedBy = "ticket")
	private List<Note> note;
	 
	 @ManyToMany()
		@JoinTable(
				name= "ticket_category",
				joinColumns = @JoinColumn(name="ticket_id"),
				inverseJoinColumns = @JoinColumn(name= "category_id")
				)
		private List<Category> categories;

	@Transient
	private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-YYYY HH:mm");
	
	public Ticket () {
		this.stato = "Da fare";
		this.createdAt = LocalDateTime.now();
	}
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<Note> getNote() {
		return note;
	}

	public void setNote(List<Note> note) {
		this.note = note;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public User getUser() {
	    return user;
	}

	public void setUser(User user) {
	    this.user = user;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public DateTimeFormatter getDateFormatter() {
		return dateFormatter;
	}

	public void setDateFormatter(DateTimeFormatter dateFormatter) {
		this.dateFormatter = dateFormatter;
	}
	
//	public String getFormattedUpdatedAt() {
//		if (updatedAt != null)
//		{
//		return updatedAt.toLocalDateTime().format(dateFormatter);
//		}
	
	
}
