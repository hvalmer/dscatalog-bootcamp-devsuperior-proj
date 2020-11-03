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
import com.braincustom.dscatalog.dto.ProductDTO;
import com.braincustom.dscatalog.entities.Category;
import com.braincustom.dscatalog.entities.Product;
import com.braincustom.dscatalog.repositories.CategoryRepository;
import com.braincustom.dscatalog.repositories.ProductRepository;
import com.braincustom.dscatalog.services.exceptions.DatabaseException;
import com.braincustom.dscatalog.services.exceptions.ResourceNotFoundException;

/*@Service registra a classe como um componente do sist de injeção do spring
 * Quem irá gerenciar as instâncias das dependências dos objetos do
 * tipo ProductService vai ser o Spring*/
@Service // objeto da camada de serviço
public class ProductService {

	@Autowired // injeta automaticamente a dependência
	private ProductRepository repository;
	
	/*criando uma dependência para o category repository*/
	@Autowired
	private CategoryRepository categoryRepository;
	

	// busca paginada
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
		Page<Product> list = repository.findAll(pageRequest);
		// lista com expressão Lambda
		return list.map(x -> new ProductDTO(x));
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> obj = repository.findById(id);// traz os dados procurados no BD
		Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found!"));
		return new ProductDTO(entity, entity.getCategories());
	}

	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product entity = new Product();
		/*Método auxiliar para insert e update*/
		copyDtoToEntity(dto, entity);
		// salvando...
		entity = repository.save(entity);
		return new ProductDTO(entity);
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			// atualizando um registro na JPA
			Product entity = repository.getOne(id);// não toca no BD, instancia um objeto provisório. Atualiza os dados
													// com getOne()
			/*Método auxiliar para insert e update*/
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);// salvando no BD
			return new ProductDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} 
		catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		} 
		catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation!");
		}
	}

	private void copyDtoToEntity(ProductDTO dto, Product entity) {
		
		/*atributos da entidade*/
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setDate(dto.getDate());
		entity.setImgUrl(dto.getImgUrl());
		entity.setPrice(dto.getPrice());
		
		/*limpando lista das categorias que por ventura estarão na entidade*/
		entity.getCategories().clear();
		
		/*percorrendo as categorias dto que estão assoc. ao dto*/
		for(CategoryDTO catDto : dto.getCategories()) {
			Category category = categoryRepository.getOne(catDto.getId());
			entity.getCategories().add(category);
		}
	}
}
