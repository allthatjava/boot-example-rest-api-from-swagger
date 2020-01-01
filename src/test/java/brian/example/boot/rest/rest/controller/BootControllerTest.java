package brian.example.boot.rest.rest.controller;

import brian.example.boot.rest.model.Person;
import brian.example.boot.rest.controller.BootController;
import brian.example.boot.rest.controller.BootControllerAdvice;
import brian.example.boot.rest.exception.PersonNotFoundException;
import brian.example.boot.rest.service.BootService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class BootControllerTest extends AbstractRestControllerTest{

    @Mock
    BootService service;

    @InjectMocks
    BootController controller;

    MockMvc mockMvc;

    @Before
    public void setUp(){

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new BootControllerAdvice()) // required if @ControllerAdvice is used
                .build();
    }

    @Test
    public void testGetHelloUsingGET() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/")
                    .content(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", Matchers.is("Hello~") ));
    }

    // Single return JSON object
    @Test
    public void testGetPersonUsingGET() throws Exception {
        // Given
        Person p = new Person();
        p.setName("Brian");
        p.setAge(44);

        // When
        when(service.getPerson("Brian")).thenReturn(java.util.Optional.of(p));

        mockMvc.perform(MockMvcRequestBuilders.get("/person/Brian")
                .content(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is("Brian") ))
                .andExpect(jsonPath("$.age", Matchers.is(44) ));
    }

    @Test
    public void testGetPersonUsingGET_ErrorHandling() throws Exception {

        // When
        when(service.getPerson("abc")).thenThrow(new PersonNotFoundException("Name:abc was not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/person/abc")
                .content(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isNotFound());
    }

    // Multiple JSON objects
    @Test
    public void testSearchByNameUsingGET() throws Exception {

        // Given
        List<Person> expected = new ArrayList<>();
        Person p1 = new Person();
        p1.name("Bob");
        p1.age(20);
        expected.add(p1);
        Person p2 = new Person();
        p2.name("Barnie");
        p2.age(44);
        expected.add(p2);

        // When
        when(service.searchPersonalInfo("b")).thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders.get("/search?name=b")
                    .content(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2) ))
                .andExpect(jsonPath("$[0].name", Matchers.is("Bob")))
                .andExpect(jsonPath("$[1].name", Matchers.is("Barnie")))
        ;
    }

    // Any Order
    @Test
    public void testSearchByNameUsingGET_inAnyOrder() throws Exception {

        // Given
        List<Person> expected = new ArrayList<>();
        Person p1 = new Person();
        p1.name("Bob");
        p1.age(20);
        expected.add(p1);
        Person p2 = new Person();
        p2.name("Barnie");
        p2.age(44);
        expected.add(p2);

        // When
        when(service.searchPersonalInfo("b")).thenReturn(expected);

        // Test
        mockMvc.perform(MockMvcRequestBuilders.get("/search?name=b")
                .content(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2) ))
                .andExpect(jsonPath("$[*].name",
                        Matchers.containsInAnyOrder("Barnie", "Bob")));
    }

    // Saving new object
    @Test
    public void testAddPersonUsingPOST() throws Exception {

        // Given
        Person person = new Person();
        person.setName("Brian");
        person.setAge(44);

        // When
        when(service.addPersonalInfo(person)).thenReturn(person);

        // Test and Assert
        mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJasonString(person)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is("Brian")));
    }

    // Delete
    @Test
    public void testDeletePersonUsingDELETE() throws Exception {

        mockMvc.perform(delete("/person/Brian"))
                .andExpect(status().isOk());

        verify(service).deletePerson(anyString());
    }
}
