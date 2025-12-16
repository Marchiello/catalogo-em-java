package com.projetododevsuperior.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projetododevsuperior.dscatalog.dto.CategoryDTO;
import com.projetododevsuperior.dscatalog.entities.Category;
import com.projetododevsuperior.dscatalog.repositories.CategoryRepository;
import com.projetododevsuperior.dscatalog.resources.CategoryResource;
import com.projetododevsuperior.dscatalog.services.exceptions.DatabaseException;
import com.projetododevsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoryService {

	// Comentado pois causou erro de chamada ciclica.
	
//    private final CategoryResource categoryResource;

//
//    CategoryService(CategoryResource categoryResource) {
//        this.categoryResource = categoryResource;
//    }
	
	@Autowired
	private CategoryRepository _repository;
	
	@Transactional(readOnly = true) /* Transação com BD */
	// Isso, alinhado com o open-in-view=false, fazem com que as consultas do BD
	// estejam isoladas na camada de serviço. O Controlador apenas entrega a resposta.
	public List<CategoryDTO> findAll(){
		List<Category> list = _repository.findAll();
		
		return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
	}

	@Transactional(readOnly = true) 
	public CategoryDTO findById(Long id) {
		Optional<Category> obj = _repository.findById(id);
		Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found."));
		return new CategoryDTO(entity);
	}

	public CategoryDTO insert(CategoryDTO dto) {
		Category entity = new Category();
		entity.setName(dto.getName());
		entity = _repository.save(entity);
		
		return new CategoryDTO(entity);
	}

	public CategoryDTO update(Long id, CategoryDTO dto) {
		try {
			Category entity = _repository.getReferenceById(id);
			entity.setName(dto.getName());
			entity = _repository.save(entity);
			return new CategoryDTO(entity);			
			
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
	
}
