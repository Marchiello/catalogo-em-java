package com.projetododevsuperior.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projetododevsuperior.dscatalog.dto.CategoryDTO;
import com.projetododevsuperior.dscatalog.entities.Category;
import com.projetododevsuperior.dscatalog.repositories.CategoryRepository;
import com.projetododevsuperior.dscatalog.services.exceptions.EntityNotFoundException;

@Service
public class CategoryService {

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
		Category entity = obj.orElseThrow(() -> new EntityNotFoundException("Entity not found."));
		return new CategoryDTO(entity);
	}
	
}
