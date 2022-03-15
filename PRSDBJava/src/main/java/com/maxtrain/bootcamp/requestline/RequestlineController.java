package com.maxtrain.bootcamp.requestline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.maxtrain.bootcamp.request.RequestRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/requestlines")
public class RequestlineController {
	
	@Autowired
	private RequestlineRepository reqlineRepo;
	@Autowired
	private RequestRepository reqRepo;
	
	@SuppressWarnings("rawtypes")
	private ResponseEntity recalcRequestTotal(int requestId) {
		var reqOpt= reqRepo.findById(requestId);
		if(reqOpt.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		var request = reqOpt.get();
		var requestTotal = 0;
		for(var requestline : request.getRequestlines()) {
			requestTotal += requestline.getProduct().getPrice() * requestline.getQuantity();
		}
		request.setTotal(requestTotal);
		reqRepo.save(request);
		return new ResponseEntity<>(HttpStatus.OK); 
	}  
	
	@GetMapping
	public ResponseEntity<Iterable<Requestline>> GetRequestline() {
		var requestline = reqlineRepo.findAll();
		return new ResponseEntity<Iterable<Requestline>>(requestline, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Requestline> GetRequestline(@PathVariable int id) {
		var requestline = reqlineRepo.findById(id);
		if(requestline.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Requestline>(requestline.get(), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Requestline> postRequestline(@RequestBody Requestline requestline) throws Exception {
		if(requestline == null || requestline.getId() !=0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} 
		var reql = reqlineRepo.save(requestline);
		var respEntity = this.recalcRequestTotal(reql.getRequest().getId());
		if(respEntity.getStatusCode() !=HttpStatus.OK) {
			throw new Exception("Recalculate request total failed!");
		}
		return new ResponseEntity<Requestline>(reql, HttpStatus.CREATED);
	} 
	

	@SuppressWarnings("rawtypes")
	@PutMapping("{id}")
	public ResponseEntity putRequestline(@PathVariable int id, @RequestBody Requestline requestline) throws Exception {
		if(requestline == null || requestline.getId() == 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var reqline = reqlineRepo.findById(requestline.getId());
		if(reqline.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		var reql = reqline.get();
		reqlineRepo.save(reql);
		var respEntity = this.recalcRequestTotal(reql.getRequest().getId());
		if(respEntity.getStatusCode() != HttpStatus.OK) {
			throw new Exception("Recalculate request total failed!");
		}
		reqlineRepo.save(requestline);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	
	
	
	
	/*@SuppressWarnings("rawtypes")
	@PutMapping("{id}")
	public ResponseEntity putRequestline(@PathVariable int id, @RequestBody Requestline requestline) throws Exception {
		if(requestline == null || requestline.getId() == 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var reqlOpt = reqlineRepo.findById(requestline.getId());
		if(reqlOpt.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		var reql = reqlOpt.get();
		reqlineRepo.save(reql);
		var respEntity = this.recalcRequestTotal(reql.getRequest().getId());
		if(respEntity.getStatusCode() != HttpStatus.OK) {
			throw new Exception("Recalculate request total failed!");
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
	} */
	
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("{id}")
	public ResponseEntity deleteRequestline(@PathVariable int id) throws Exception {
		var requestline = reqlineRepo.findById(id);
		if (requestline.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		var reql = requestline.get();
		reqlineRepo.delete(reql);
		var respEntity = this.recalcRequestTotal(reql.getRequest().getId());
		if(respEntity.getStatusCode() != HttpStatus.OK) {
			throw new Exception("Recalculate request total failed!");
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	} 

}
