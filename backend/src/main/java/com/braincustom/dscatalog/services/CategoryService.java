package com.braincustom.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.braincustom.dscatalog.dto.CategoryDTO;
import com.braincustom.dscatalog.entities.Category;
import com.braincustom.dscatalog.repositories.CategoryRepository;
import com.braincustom.dscatalog.services.exceptions.DatabaseException;
import com.braincustom.dscatalog.services.exceptions.ResourceNotFoundException;

/*@Service registra a classe como um componente do sist de injeção do spring
 * Quem irá gerenciar as instâncias das dependências dos objetos do
 * tipo CategoryService vai ser o Spring*/
@Service //objeto da camada de serviço
public class CategoryService {

	@Autowired //injeta automaticamente a dependência
	private CategoryRepository repository;
	
	//busca paginada
	@Transactional(readOnly = true)
	public Page<CategoryDTO> findAllPaged(PageRequest pageRequest){
		Page<Category> list = repository.findAll(pageRequest);
		//lista com expressão Lambda
		return list.map(x -> new CategoryDTO(x));
	}

	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> obj = repository.findById(id);//traz os dados procurados no BD 
		Category entity = obj.orElseThrow(() -> new EntityNotFoundException("Entity not found!"));
		return new CategoryDTO(entity);
	}

	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		Category entity = new Category();
		entity.setName(dto.getName());
		//salvando...
		entity = repository.save(entity);
		return new CategoryDTO(entity);
	}

	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		try {
			//atualizando um registro na JPA
			Category entity = repository.getOne(id);//não toca no BD, instancia um objeto provisório. Atualiza os dados com getOne()
			entity.setName(dto.getName());
			entity = repository.save(entity);//salvando no BD
			return new CategoryDTO(entity);
		}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		}
		catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
		catch(DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation!");
		}
		
	}
}
