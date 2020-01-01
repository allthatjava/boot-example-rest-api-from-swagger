package brian.template.boot.rest.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import brian.temp.spring.boot.model.Person;
import brian.template.boot.rest.exception.SamePersonAlreadyExistException;

@Service
public class BootService {

	List<Person> people = null;
	
	public BootService() {
		people = new ArrayList<>();
		Person p1 = new Person();
		p1.setAge(20);
		p1.setName("Bob");
		Person p2 = new Person();
		p2.setAge(35);
		p2.setName("Harry");
		Person p3 = new Person();
		p3.setAge(44);
		p3.setName("Barnie");
		
		people.add(p1);
		people.add(p2);
		people.add(p3);
	}
	
	public List<Person> searchPersonalInfo(String name) {
		
		return people.stream()
				.filter(h -> h.getName().toLowerCase().indexOf(name.toLowerCase()) > -1 )
				.collect(Collectors.toList());
	}
	
	public Person addPersonalInfo(Person person) throws SamePersonAlreadyExistException{
		
		boolean alreadyExist = people.stream()
			.anyMatch(p -> p.getName().equals(person.getName()) && p.getAge() == person.getAge());
		
		if( alreadyExist )
			throw new SamePersonAlreadyExistException("Name:"+person.getAge()+",Age:"+person.getAge()+" already exists");
		else
			people.add(person);
		
		return person;
	}
	
	public Optional<Person> getPerson(String name) {
		return people.stream()
				.filter(p-> name.equals(p.getName()))
				.findFirst();
	}

	/**
	 * Return type is void since JPA object must be removed after delete
	 * @param name
	 */
	public void deletePerson(String name) {
		
		Person reducedPeople = people.stream().filter(p -> p.getName().equals(name)).findFirst().get();
		
		people.removeIf(p -> p.getName().equals(name));
	}
}
