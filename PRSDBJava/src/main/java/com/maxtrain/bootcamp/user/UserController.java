package com.maxtrain.bootcamp.user;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UserController {
	 
	@Autowired
	private UserRepository useRepo;
	
	@GetMapping
	public ResponseEntity<Iterable<User>> GetUser() {
		var use = useRepo.findAll();
		return new ResponseEntity<Iterable<User>>(use, HttpStatus.OK);
	}
	
	@GetMapping("{username}/{password}")
	public ResponseEntity<User> getUserByUsernameAndPassword(@PathVariable String username,@PathVariable String password) {
		var use = useRepo.findByUsernameAndPassword(username, password);
		if(use.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<User>(use.get(), HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<User> GetUser(@PathVariable int id) {
		var use = useRepo.findById(id);
		if(use.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<User>(use.get(), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<User> postUser(@RequestBody User user) {
		if(user == null || user.getId() != 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var use = useRepo.save(user);
		return new ResponseEntity<User>(use, HttpStatus.CREATED);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("{id}")
	public ResponseEntity putUser(@PathVariable int id, @RequestBody User user) {
		if(user == null || user.getId() == 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var use = useRepo.findById(user.getId());
		if(use.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		useRepo.save(user);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("{id}")
	public ResponseEntity deleteUser(@PathVariable int id) {
		var user = useRepo.findById(id);
		if(user.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		useRepo.delete(user.get());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	

}
