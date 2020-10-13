package au.com.deepak.user.model;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Data;

//@DynamicUpdate
@Data
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
	
	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	@Min(1)
	private Long id;

	@NotEmpty(message = "Title is required")
	@Size(min = 2, max = 6)
	@Column(name = "title")
	private String title;
	
	@NotEmpty(message = "First name is required")
	@Size(min = 1, max = 15)
	@Column(name = "first_name")
	private String firstName;
	
	@NotEmpty(message = "Last name is required")
	@Size(min = 1, max = 15)
	@Column(name = "last_name")
	private String lastName;
	
	@NotEmpty(message = "Gender is required")
	@Size(min = 4, max = 6)
	@Column(name = "gender")
	private String gender;
	
	@Embedded
    /*@AttributeOverrides(value = {
        @AttributeOverride(name = "street", column = @Column(name = "street")),
        @AttributeOverride(name = "city", column = @Column(name = "city"))
    })*/
    private Address address;
	
	/*
	@CreationTimestamp
	private Date createdDt;
	
	@CreationTimestamp
	private Date updatedDt;*/
	
	public User() {}

}