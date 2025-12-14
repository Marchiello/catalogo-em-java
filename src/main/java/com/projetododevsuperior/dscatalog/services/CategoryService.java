package com.projetododevsuperior.dscatalog.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projetododevsuperior.dscatalog.entities.Category;
import com.projetododevsuperior.dscatalog.repositories.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository _repository;
	
	public List<Category> findAll(){
		return _repository.findAll();
	}
	
}
