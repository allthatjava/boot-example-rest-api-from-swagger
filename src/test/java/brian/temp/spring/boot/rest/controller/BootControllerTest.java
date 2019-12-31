package brian.temp.spring.boot.rest.controller;

import brian.temp.spring.boot.model.Person;
import brian.template.boot.rest.controller.BootController;
import brian.template.boot.rest.service.BootService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.results.ResultMatchers;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class BootControllerTest {

    @Mock
    BootService service;

    @InjectMocks
    BootController controller;

    MockMvc mockMvc;

    @Before
    public void setUp(){

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    // Single return JSON object
    @Test
    public void testGetPersonUsingGET() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/")
                    .content(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", Matchers.is("Hello~") ));
    }

    // Multiple JSON objects
    @Test
    public void testSearchByNameUsingGETGET() throws Exception {

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

    // Check the result in Any Order
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
}
