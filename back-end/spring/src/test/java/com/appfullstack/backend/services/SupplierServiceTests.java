package com.appfullstack.backend.services;

import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.appfullstack.backend.dto.SupplierDTO;
import com.appfullstack.backend.entities.Supplier;
import com.appfullstack.backend.repositories.SupplierRepository;
import com.appfullstack.backend.services.exceptions.DatabaseException;
import com.appfullstack.backend.services.exceptions.ResourceNotFoundException;
import com.appfullstack.backend.tests.SupplierFactory;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class SupplierServiceTests {

	@InjectMocks
	private SupplierService service;
	
	@Mock
	private SupplierRepository repository;
	
	private long existingSupplierId, nonExistingSupplierId, dependentSupplierId;
	private String supplierName;
	private Supplier supplier;
	private SupplierDTO supplierDTO;
	private List<Supplier> list;
	
	@BeforeEach
	void setUp() throws Exception {
		existingSupplierId = 1L;
		nonExistingSupplierId = 2L;
		dependentSupplierId = 3L;
		
		supplierName = "Aliança ME";
		
		supplier = SupplierFactory.createSupplier(supplierName);
		supplierDTO = new SupplierDTO(supplier);
		list = new ArrayList<>(List.of(supplier));
		
		Mockito.when(repository.findById(existingSupplierId)).thenReturn(Optional.of(supplier));
		Mockito.when(repository.findById(nonExistingSupplierId)).thenReturn(Optional.empty());
		
		Mockito.when(repository.findAll()).thenReturn(list);
		
		Mockito.when(repository.save(any())).thenReturn(supplier);
		
		Mockito.when(repository.getReferenceById(existingSupplierId)).thenReturn(supplier);
		Mockito.when(repository.getReferenceById(nonExistingSupplierId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(repository.existsById(existingSupplierId)).thenReturn(true);
		Mockito.when(repository.existsById(nonExistingSupplierId)).thenReturn(false);
		Mockito.when(repository.existsById(dependentSupplierId)).thenReturn(true);
		
		Mockito.doNothing().when(repository).deleteById(existingSupplierId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentSupplierId);
	}
	
	@Test
	public void findByIdShouldReturnSupplierDTOWhenIdExists() {
		
		SupplierDTO dto = service.findById(existingSupplierId);
		
		Assertions.assertNotNull(dto);
		Assertions.assertEquals(dto.getId(), existingSupplierId);
		Assertions.assertEquals(dto.getName(), supplier.getName());
		Assertions.assertEquals(dto.getFoundationYear(), supplier.getFoundationYear());
	}
	
	@Test
	public void findByIdShouldReturnResourceNotFoundExceptionWhenIdDoesNotExists() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingSupplierId);
		});
	}
	
	@Test
	public void findAllShouldReturnListOfSupplierDTO() {
		
		List<SupplierDTO> list = service.findAll();
		
		Assertions.assertNotNull(list);
		Assertions.assertEquals(list.size(), 1);
		Assertions.assertEquals(list.iterator().next().getName(), supplierName);
	}
	
	@Test
	public void insertShouldReturnSupplierDTO() {
		
		SupplierDTO dto = service.insert(supplierDTO);
		
		Assertions.assertNotNull(dto);
		Assertions.assertEquals(dto.getId(), supplier.getId());
	}
	
	@Test
	public void updateShouldReturnSupplierDTOWhenIdExists() {
		
		SupplierDTO dto = service.update(existingSupplierId, supplierDTO);
		
		Assertions.assertNotNull(dto);
		Assertions.assertEquals(dto.getId(), existingSupplierId);
		Assertions.assertEquals(dto.getName(), supplierDTO.getName());
	}
	
	@Test
	public void updateShouldReturnResourceNotFoundExceptionWhenIdDoesNotExists() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistingSupplierId, supplierDTO);
		});
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingSupplierId);
		});
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingSupplierId);
		});
	}
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(DatabaseException.class, () -> {
			service.delete(dependentSupplierId);
		});
	}
}
