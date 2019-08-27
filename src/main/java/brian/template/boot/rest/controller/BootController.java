package brian.template.boot.rest.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import brian.temp.spring.boot.api.DefaultApi;
import brian.temp.spring.boot.api.PersonApi;
import brian.temp.spring.boot.api.SearchApi;
import brian.temp.spring.boot.model.Person;
import brian.template.boot.rest.controller.exception.PersonNotFoundException;
import brian.template.boot.rest.service.BootService;

@RestController
public class BootController implements //DefaultApi, PersonApi, 
SearchApi
{
	private BootService service;
	
	@Autowired
	public BootController(BootService service) {
		this.service = service;
	}
	
	@Override
	public ResponseEntity<List<Person>> searchByNameUsingGET(String name) {
		return new ResponseEntity<>( service.searchPersonalInfo(name), HttpStatus.OK);
	}

//	@Override
//	public ResponseEntity<Person> addPersonUsingPOST(Integer age, String name) {
//		Person p = new Person();
//		p.setAge(age);
//		p.setName(name);
//		return new ResponseEntity<>( service.addPersonalInfo(p), HttpStatus.OK );
//	}
//
//	@Override
//	public ResponseEntity<Person> getPersonUsingGET(String name) {
//		Optional<Person> person = service.getPerson(name);
//		if( person.isPresent() )
//			return new ResponseEntity<>( person.get(), HttpStatus.OK );
//		else
//			throw new PersonNotFoundException("Name:"+name+" was not found");
//	}
//
//	@Override
//	public ResponseEntity<Person> getHelloUsingGET() {
//		Person p = new Person();
//		p.setName("Hello~");
//		p.setAge(99);
//		return new ResponseEntity<>( p, HttpStatus.OK );
//	}
}
