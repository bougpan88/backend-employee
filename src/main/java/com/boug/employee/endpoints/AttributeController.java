package com.boug.employee.endpoints;

import com.boug.employee.domain.Attribute;
import com.boug.employee.dto.AttributeDto;
import com.boug.employee.error.ApplicationException;
import com.boug.employee.error.CustomError;
import com.boug.employee.repository.AttributeRepository;
import com.boug.employee.service.AttributeService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@CrossOrigin(value = {"http://localhost:4200", "http://localhost", "http://localhost:4200" },methods = {GET,POST,PUT,DELETE})
@RestController()
@RequestMapping("attributes")
public class AttributeController {

    private AttributeService attributeService;
    private AttributeRepository attributeRepository;

    @Autowired
    public AttributeController(AttributeService attributeService){
        this.attributeService = attributeService;
    }

    @ApiOperation(value = "Used to create a new attribute")
    @ApiResponses(value = { @ApiResponse(code = 202 , message = "accepted"),
                            @ApiResponse(code = 400 , message = "bad request (attribute already exist or bad input data)"),
                            @ApiResponse(code = 401 , message = "unauthorized"),
                            @ApiResponse(code = 500 , message = "server error")})
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @PostMapping()
    public ResponseEntity<Void> createAttribute(@Valid @RequestBody AttributeDto attributeDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            List<String> message = new ArrayList<>();
            for (FieldError e : errors) {
                message.add("@" + e.getField() + ":" + e.getDefaultMessage());
            }
            throw new ApplicationException( new CustomError(400, "Bad Request", message.toString()));
        } else {
            attributeService.createAttribute(attributeDto);
            return ResponseEntity.accepted().build();
        }
    }

    @ApiOperation(value = "Used to retrieve all attributes")
    @ApiResponses(value = { @ApiResponse(code = 200 , message = ""),
            @ApiResponse(code = 204 , message = "No content"),
            @ApiResponse(code = 401 , message = "unauthorized"),
            @ApiResponse(code = 500 , message = "server error")})
    @GetMapping
    public ResponseEntity<List<AttributeDto>> getAllAttributes(){
        List<AttributeDto> attributeDtos = attributeService.getAllAttributes();
        if (attributeDtos.isEmpty()){
            return ResponseEntity.noContent().build();
        } else{
            return ResponseEntity.ok(attributeDtos);
        }
    }

    @ApiOperation(value = "Used to retrieve all attribute names")
    @ApiResponses(value = { @ApiResponse(code = 200 , message = "", response = String.class, responseContainer = "List"),
            @ApiResponse(code = 401 , message = "unauthorized"),
            @ApiResponse(code = 500 , message = "server error")})
    @GetMapping(path = "/names")
    public ResponseEntity<List<String>> getAllAttributeNames(){
        List<AttributeDto> attributeDtos = attributeService.getAllAttributes();
        List<String> attributeNames = attributeDtos.stream().map(AttributeDto::getName).collect(Collectors.toList());
        return ResponseEntity.ok(attributeNames);
    }

    @ApiOperation(value = "Used to delete an attribute")
    @ApiResponses(value = { @ApiResponse(code = 204 , message = "deleted"),
            @ApiResponse(code = 400 , message = "bad request (attribute does not exist or bad input data)"),
            @ApiResponse(code = 401 , message = "unauthorized"),
            @ApiResponse(code = 500 , message = "server error")})
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{attributeName}")
    public ResponseEntity<Void> deleteAttribute(@PathVariable String attributeName){
        attributeService.deleteAttribute(attributeName);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Used to update an attribute")
    @ApiResponses(value = { @ApiResponse(code = 200 , message = "updated"),
            @ApiResponse(code = 400 , message = "bad request (attribute does not exist or bad input data)"),
            @ApiResponse(code = 401 , message = "unauthorized"),
            @ApiResponse(code = 500 , message = "server error")})
    @PutMapping(path = "{oldAttributeName}")
    public ResponseEntity<Void> updateAttribute(@PathVariable String oldAttributeName, @RequestBody @ApiParam(value = "The new attribute name") AttributeDto attributeDto,
                                          BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            List<String> message = new ArrayList<>();
            for (FieldError e : errors) {
                message.add("@" + e.getField() + ":" + e.getDefaultMessage());
            }
            throw new ApplicationException( new CustomError(400, "Bad Request", message.toString()));
        } else {
            attributeService.updateAttribute(oldAttributeName, attributeDto.getName());
            return ResponseEntity.ok().build();
        }
    }


}
