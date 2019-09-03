package brian.template.boot.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import brian.temp.spring.boot.model.Person;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class BootControllerIntgrationTest {
	
	RestTemplate restTemplate;

    @LocalServerPort
    int randomServerPort;
    
	@Before
	public void setup() {
		restTemplate = new RestTemplate();
	}

	// Search by name searchByNameUsingGET
	@Test
	public void testSearchByName_withBob_shouldReturnPersonBob() {
		// Given
		Person p1 = new Person();
		p1.setName("Bob");
		p1.setAge(20);
		Person p3 = new Person();
		p3.setAge(44);
		p3.setName("Barnie");
		List<Person> expected = new ArrayList<>();
		expected.add(p1);
		expected.add(p3);
		
		// Test
		ResponseEntity<List<Person>> response = restTemplate.exchange(
													"http://localhost:"+randomServerPort+"/search?name=b",
													HttpMethod.GET,
													null,
													new ParameterizedTypeReference<List<Person>>() {});
		
		// Assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).hasSize(2);
	}
	
	// addPersonUsingPOST
	@Test
	public void testPostPerson_withNewPerson_shouldReturnSuccess() {
		// Given
		Person p1 = new Person();
		p1.setName("SmartyPants");
		p1.setAge(27);
		
		// Test
		ResponseEntity<Person> response = restTemplate.postForEntity("http://localhost:"+randomServerPort+"/person", p1, Person.class);
		
		// Assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getName()).isEqualTo(p1.getName());
		
		// Clean up
		ResponseEntity<Person> deleteResponse = restTemplate.exchange(
											"http://localhost:"+randomServerPort+"/person/"+p1.getName(), 
											HttpMethod.DELETE,
											null,
											Person.class);
		
		assertThat(deleteResponse.getStatusCode().value()).isEqualTo(200);
	}
	
	// getPersonUsingGET
	
	// getHelloUsingGET
	@Test
	public void testGetHello_withNoParam_shouldReturnHelloObject() {
		
		// Given
		Person expected = new Person();
		expected.setName("Hello~");
		expected.setAge(99);
		
		// Test
		ResponseEntity<Person> response = restTemplate.getForEntity("http://localhost:"+randomServerPort+"/", Person.class);
		
		// Assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo(expected);
	}
}
