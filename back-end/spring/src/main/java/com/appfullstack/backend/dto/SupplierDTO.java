package com.appfullstack.backend.dto;

import com.appfullstack.backend.entities.Supplier;
import com.appfullstack.backend.enums.Sector;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class SupplierDTO {

	private Long id;
	
	@Size(min = 3, max = 20, message = "Name must have between 3 and 20 characters.")
	@NotBlank(message = "Supplier name cannot be empty.")
	private String name;

	@Size(min = 3, max = 80, message = "Contact info must have between 3 and 80 characters.")
	@NotBlank(message = "Supplier contact cannot be empty.")
	private String contactInfo;

	@Positive(message = "Foundation year must be positive.")
	private Integer foundationYear;

	@NotNull(message = "Supplier sector cannot be empty.")
	private Sector sector;
	
	public SupplierDTO() {
	}

	public SupplierDTO(Long id, String name, String contactInfo, Integer foundationYear, Sector sector) {
		this.id = id;
		this.name = name;
		this.contactInfo = contactInfo;
		this.foundationYear = foundationYear;
		this.sector = sector;
	}
	
	public SupplierDTO(Supplier entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.contactInfo = entity.getContactInfo();
		this.foundationYear = entity.getFoundationYear();
		this.sector = entity.getSector();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}

	public Integer getFoundationYear() {
		return foundationYear;
	}

	public void setFoundationYear(Integer foundationYear) {
		this.foundationYear = foundationYear;
	}

	public Sector getSector() {
		return sector;
	}

	public void setSector(Sector sector) {
		this.sector = sector;
	}
}
