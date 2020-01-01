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
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2019-12-31T22:04:01.039-05:00")

@Api(value = "search", description = "the search API")
public interface SearchApi {

    @ApiOperation(value = "searchByName", notes = "", response = Person.class, responseContainer = "List", tags={ "boot-controller", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = Person.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Person.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Person.class),
        @ApiResponse(code = 404, message = "Not Found", response = Person.class) })
    @RequestMapping(value = "/search",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<Person>> searchByNameUsingGET( @NotNull @ApiParam(value = "name", required = true) @RequestParam(value = "name", required = true) String name);

}
