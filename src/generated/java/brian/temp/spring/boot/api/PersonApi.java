package brian.temp.spring.boot.api;

import brian.temp.spring.boot.model.Person;

import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2019-08-27T11:01:17.125-04:00")

@Api(value = "person", description = "the person API")
public interface PersonApi {

    @ApiOperation(value = "addPerson", notes = "", response = Person.class, tags={ "boot-controller", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = Person.class),
        @ApiResponse(code = 201, message = "Created", response = Person.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Person.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Person.class),
        @ApiResponse(code = 404, message = "Not Found", response = Person.class) })
    @RequestMapping(value = "/person",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<Person> addPersonUsingPOST(@ApiParam(value = "" ,required=true ) @RequestBody Person post);


    @ApiOperation(value = "getPerson", notes = "", response = Person.class, tags={ "boot-controller", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = Person.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Person.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Person.class),
        @ApiResponse(code = 404, message = "Not Found", response = Person.class) })
    @RequestMapping(value = "/person/{name}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<Person> getPersonUsingGET(@ApiParam(value = "name",required=true ) @PathVariable("name") String name);

}
