package com.boug.employee.service;

import com.boug.employee.domain.Attribute;
import com.boug.employee.domain.EmployeeAttribute;
import com.boug.employee.dto.AttributeDto;
import com.boug.employee.error.ApplicationException;
import com.boug.employee.error.CustomError;
import com.boug.employee.repository.AttributeRepository;
import com.boug.employee.repository.EmployeeAttributeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttributeService {

    private AttributeRepository attributeRepository;

    @Autowired
    public AttributeService(AttributeRepository attributeRepository){
        this.attributeRepository = attributeRepository;
    }

    @Transactional
    public void createAttribute(AttributeDto attributeDto) {

        Optional<Attribute> existingAttribute = attributeRepository.findByName(attributeDto.getName());
        if (existingAttribute.isPresent()){
            throw new ApplicationException(new CustomError(400, "Bad Request", "Attribute already exist"));
        } else {
            Attribute attribute = new Attribute();
            attribute.setName(attributeDto.getName());
            attributeRepository.save(attribute);
        }
    }

    public List<AttributeDto> getAllAttributes(){
        List<Attribute> attributes = attributeRepository.findAll();
        return attributes.stream().map(attribute -> new AttributeDto(attribute.getName())).collect(Collectors.toList());
    }

    public Attribute getAttribute(String attributeName){
        Optional<Attribute> attribute = attributeRepository.findByName(attributeName);
        if (attribute.isPresent()){
            return attribute.get();
        } else {
            throw new ApplicationException(new CustomError(404, "Not Found", "Attribute does not exist"));
        }
    }

    @Transactional
    public void deleteAttribute(String attributeName){
        Optional<Attribute> attribute = attributeRepository.findByName(attributeName);
        if (attribute.isPresent()){
            attributeRepository.deleteById(attribute.get().getId());
        } else {
            throw new ApplicationException(new CustomError(404, "Not Found", "Attribute does not exist"));
        }
    }

    @Transactional
    public Attribute updateAttribute(String oldAttributeName, String newAttributeName){
        Optional<Attribute> optRetrievedAttribute = attributeRepository.findByName(oldAttributeName);
        if (optRetrievedAttribute.isPresent()){
            Attribute retrievedAttribute = optRetrievedAttribute.get();
            retrievedAttribute.setName(newAttributeName);
            retrievedAttribute = attributeRepository.save(retrievedAttribute);
            return retrievedAttribute;
        } else {
            throw new ApplicationException(new CustomError(404, "Not Found", "Attribute does not exist"));
        }
    }
}
