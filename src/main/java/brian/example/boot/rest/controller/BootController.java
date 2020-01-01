package brian.example.boot.rest.controller;

import java.util.List;
import java.util.Optional;

import brian.example.boot.rest.exception.PersonNotFoundException;
import brian.example.boot.rest.service.BootService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import brian.example.boot.rest.api.DefaultApi;
import brian.example.boot.rest.api.PersonApi;
import brian.example.boot.rest.api.SearchApi;
import brian.example.boot.rest.model.Person;

@RestController
public class BootController implements DefaultApi, PersonApi, SearchApi
{
	private BootService service;
	
	@Autowired
	public BootController(BootService service) {
		this.service = service;
	}
	
	@Override
	public ResponseEntity<List<Person>> searchByNameUsingGET(String name) {
		System.out.println("Search name:"+name);
		
		List<Person> people = service.searchPersonalInfo(name);
		System.out.println("Count:"+people.size());
		return new ResponseEntity<>( people, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Person> addPersonUsingPOST(@RequestBody Person person) {
		return new ResponseEntity<>( service.addPersonalInfo(person), HttpStatus.OK );
	}

	@Override
	public ResponseEntity<Person> getPersonUsingGET(@PathVariable("name") String name) {
		System.out.println("PathVariable name:"+name);
		Optional<Person> person = service.getPerson(name);
		if( person.isPresent() ) {
			Person p = person.get();
			System.out.println("Person:"+p.getName()+","+p.getAge());
			return new ResponseEntity<>( person.get(), HttpStatus.OK );
		}else
			throw new PersonNotFoundException("Name:"+name+" was not found");
	}

	@Override
	public ResponseEntity<Person> getHelloUsingGET() {
		Person p = new Person();
		p.setName("Hello~");
		p.setAge(99);
		return new ResponseEntity<>( p, HttpStatus.OK );
	}

	@Override
	public ResponseEntity<Void> deletePersonUsingDELETE(String name) {
		service.deletePerson(name);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
