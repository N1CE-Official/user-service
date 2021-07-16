package ru.ws.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ru.ws.model.User;

public interface UserRepository extends CrudRepository<User,Integer> {
	
	@SuppressWarnings("unchecked")
	User save(User user);   
    User findUserById(Integer id); 
    void deleteUserById(Integer id);
    User findUserByUsername(String username);
    List<User> findAll();    
}