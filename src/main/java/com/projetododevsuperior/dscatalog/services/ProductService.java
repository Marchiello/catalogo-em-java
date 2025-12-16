package com.projetododevsuperior.dscatalog.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projetododevsuperior.dscatalog.dto.CategoryDTO;
import com.projetododevsuperior.dscatalog.dto.ProductDTO;
import com.projetododevsuperior.dscatalog.entities.Category;
import com.projetododevsuperior.dscatalog.entities.Product;
import com.projetododevsuperior.dscatalog.repositories.CategoryRepository;
import com.projetododevsuperior.dscatalog.repositories.ProductRepository;
import com.projetododevsuperior.dscatalog.services.exceptions.DatabaseException;
import com.projetododevsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {

	// Comentado pois causou erro de chamada ciclica.
	
//    private final ProductResource ProductResource;

//
//    ProductService(ProductResource ProductResource) {
//        this.ProductResource = ProductResource;
//    }
	
	@Autowired
	private ProductRepository _repository;
	
	@Autowired
	private CategoryRepository _categoryRepository;
	
	@Transactional(readOnly = true) /* Transação com BD */
	// Isso, alinhado com o open-in-view=false, fazem com que as consultas do BD
	// estejam isoladas na camada de serviço. O Controlador apenas entrega a resposta.
	
	
	// Versão sem paginação
//	public List<ProductDTO> findAll(){
//		List<Product> list = _repository.findAll();
//		
//		return list.stream().map(x -> new ProductDTO(x)).collect(Collectors.toList());
//	}

	public Page<ProductDTO> findAllPaged(PageRequest pageRequest){
		Page<Product> list = _repository.findAll(pageRequest);
		
		return list.map(x -> new ProductDTO(x));
	}
	
	@Transactional(readOnly = true) 
	public ProductDTO findById(Long id) {
		Optional<Product> obj = _repository.findById(id);
		Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found."));
		return new ProductDTO(entity, entity.getCategories());
	}

	public ProductDTO insert(ProductDTO dto) {
		Product entity = new Product();

		entity = _repository.save(entity);
		
		return new ProductDTO(entity);
	}

	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			Product entity = _repository.getReferenceById(id);
			entity.setName(dto.getName());
			entity = _repository.save(entity);
			return new ProductDTO(entity);			
			
		}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}	
	}

	public void delete(Long id) {
		try {
			_repository.deleteById(id);
			
		}
		catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
		catch(DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
				
	}
	
	private void copyDtoToEntity(ProductDTO dto, Product entity) {
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setDate(dto.getDate());
		entity.setImgUrl(dto.getImgUrl());
		entity.setPrice(dto.getPrice());

		entity.getCategories().clear();
		for(CategoryDTO catDto : dto.getCategories()) {
			Category category = _categoryRepository.getOne(catDto.getId());
			entity.getCategories().add(category);
		}
	}
	
}
