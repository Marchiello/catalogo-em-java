package com.projetododevsuperior.dscatalog.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projetododevsuperior.dscatalog.entities.Category;
import com.projetododevsuperior.dscatalog.repositories.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository _repository;
	
	@Transactional(readOnly = true) /* Transação com BD */
	// Isso, alinhado com o open-in-view=false, fazem com que as consultas do BD
	// estejam isoladas na camada de serviço. O Controlador apenas entrega a resposta.
	public List<Category> findAll(){
		return _repository.findAll();
	}
	
}
