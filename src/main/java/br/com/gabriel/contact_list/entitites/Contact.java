package br.com.gabriel.contact_list.entitites;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_contact")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_contact;

    @Column(name = "contact_name") 
    private String name;

    @Column(name = "image_Url") 
    private String imageUrl;

    @Column(name = "telephone_number") 
    private String telephoneNumber;

    @Column(name = "contact_description", columnDefinition = "TEXT") 
    private String contactDescription;
    
    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;
    
    public Contact() {
    	
    }
    
	public Contact(String name, String imageUrl, String telephoneNumber, String contactDescription) {
		this.name = name;
		this.imageUrl = imageUrl;
		this.telephoneNumber = telephoneNumber;
		this.contactDescription = contactDescription;
	}
	
	public long getId_contact() {
		return id_contact;
	}

	public void setId_contact(long id_contact) {
		this.id_contact = id_contact;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public String getContactDescription() {
		return contactDescription;
	}

	public void setContactDescription(String contactDescription) {
		this.contactDescription = contactDescription;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
  }
