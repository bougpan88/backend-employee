package com.boug.employee.endpoints;

import com.boug.employee.domain.Attribute;
import com.boug.employee.dto.AttributeDto;
import com.boug.employee.error.CustomError;
import com.boug.employee.repository.AttributeRepository;
import com.boug.employee.service.AttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@CrossOrigin(value = "http://localhost:4200",methods = {GET,POST,PUT,DELETE})
@RestController()
@RequestMapping("attributes")
public class AttributeController {

    private AttributeService attributeService;
    private AttributeRepository attributeRepository;

    @Autowired
    public AttributeController(AttributeService attributeService){
        this.attributeService = attributeService;
    }

    @PostMapping()
    public ResponseEntity createAttribute(@Valid @RequestBody AttributeDto attributeDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            List<String> message = new ArrayList<>();
            for (FieldError e : errors) {
                message.add("@" + e.getField() + ":" + e.getDefaultMessage());
            }
            CustomError customError = new CustomError(400, "Bad Request", message.toString());
            return ResponseEntity.badRequest().body(customError);
        } else {
            attributeService.createAttribute(attributeDto);
            return ResponseEntity.accepted().build();
        }
    }

    @GetMapping
    public ResponseEntity getAllAttributes(){
        List<AttributeDto> attributeDtos = attributeService.getAllAttributes();
        if (attributeDtos.isEmpty()){
            return ResponseEntity.noContent().build();
        } else{
            return ResponseEntity.ok(attributeDtos);
        }
    }

    @GetMapping(path = "/names")
    public ResponseEntity getAllAttributeNames(){
        List<AttributeDto> attributeDtos = attributeService.getAllAttributes();
        List<String> attributeNames = attributeDtos.stream().map(AttributeDto::getName).collect(Collectors.toList());
        return ResponseEntity.ok(attributeNames);
    }

    @GetMapping(path = "/{attributeName}")
    public ResponseEntity getAttribute(@PathVariable String attributeName){
        Attribute attribute = attributeService.getAttribute(attributeName);
        return ResponseEntity.ok(attribute);
    }

    @DeleteMapping(path = "/{attributeName}")
    public ResponseEntity deleteAttribute(@PathVariable String attributeName){
        attributeService.deleteAttribute(attributeName);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "{oldAttributeName}")
    public ResponseEntity updateAttribute(@PathVariable String oldAttributeName, @RequestBody AttributeDto attributeDto,
                                          BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            List<String> message = new ArrayList<>();
            for (FieldError e : errors) {
                message.add("@" + e.getField() + ":" + e.getDefaultMessage());
            }
            CustomError customError = new CustomError(400, "Bad Request", message.toString());
            return ResponseEntity.badRequest().body(customError);
        } else {
            Attribute updated = attributeService.updateAttribute(oldAttributeName, attributeDto.getName());
            return ResponseEntity.ok(updated);
        }
    }


}
