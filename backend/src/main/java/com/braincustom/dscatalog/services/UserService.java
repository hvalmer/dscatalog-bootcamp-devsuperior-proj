package com.braincustom.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.braincustom.dscatalog.dto.RoleDTO;
import com.braincustom.dscatalog.dto.UserDTO;
import com.braincustom.dscatalog.dto.UserInsertDTO;
import com.braincustom.dscatalog.dto.UserUpdateDTO;
import com.braincustom.dscatalog.entities.Role;
import com.braincustom.dscatalog.entities.User;
import com.braincustom.dscatalog.repositories.RoleRepository;
import com.braincustom.dscatalog.repositories.UserRepository;
import com.braincustom.dscatalog.services.exceptions.DatabaseException;
import com.braincustom.dscatalog.services.exceptions.ResourceNotFoundException;

/*@Service registra a classe como um componente do sist de injeção do spring
 * Quem irá gerenciar as instâncias das dependências dos objetos do
 * tipo UserService vai ser o Spring*/
@Service // objeto da camada de serviço
public class UserService {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired // injeta automaticamente a dependência
	private UserRepository repository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	// busca paginada
	@Transactional(readOnly = true)
	public Page<UserDTO> findAllPaged(PageRequest pageRequest) {
		Page<User> list = repository.findAll(pageRequest);
		// lista com expressão Lambda
		return list.map(x -> new UserDTO(x));
	}

	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		Optional<User> obj = repository.findById(id);// traz os dados procurados no BD
		User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found!"));
		return new UserDTO(entity);
	}

	@Transactional
	public UserDTO insert(UserInsertDTO dto) {
		User entity = new User();
		/*Método auxiliar para insert e update*/
		copyDtoToEntity(dto, entity);
		entity.setPassword(passwordEncoder.encode(dto.getPassword()));
		// salvando...
		entity = repository.save(entity);
		return new UserDTO(entity);
	}

	@Transactional
	public UserDTO update(Long id, UserUpdateDTO dto) {
		try {
			// atualizando um registro na JPA
			User entity = repository.getOne(id);// não toca no BD, instancia um objeto provisório. Atualiza os dados
													// com getOne()
			/*Método auxiliar para insert e update*/
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);// salvando no BD
			return new UserDTO(entity);
		} 
		catch (EntityNotFoundException e) {
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

	private void copyDtoToEntity(UserDTO dto, User entity) {
		
		/*atributos da entidade*/
		entity.setFirstName(dto.getFirstName());
		entity.setLastName(dto.getLastName());
		entity.setEmail(dto.getEmail());
		
		
		/*limpando lista das categorias que por ventura estarão na entidade*/
		entity.getRoles().clear();
		
		/*percorrendo as categorias dto que estão assoc. ao dto*/
		for(RoleDTO roleDto : dto.getRoles()) {
			Role role = roleRepository.getOne(roleDto.getId());
			entity.getRoles().add(role);
		}
	}
}
