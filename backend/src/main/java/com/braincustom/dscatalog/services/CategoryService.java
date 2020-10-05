package com.braincustom.dscatalog.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.braincustom.dscatalog.entities.Category;
import com.braincustom.dscatalog.repositories.CategoryRepository;

/*@Service registra a classe como um componente do sist de injeção do spring
 * Quem irá gerenciar as instâncias das dependências dos objetos do
 * tipo CategoryService vai ser o Spring*/
@Service //objeto da camada de serviço
public class CategoryService {

	@Autowired //injeta automaticamente a dependência
	private CategoryRepository repository;
	
	public List<Category> findAll(){
		return repository.findAll();
	}
}
