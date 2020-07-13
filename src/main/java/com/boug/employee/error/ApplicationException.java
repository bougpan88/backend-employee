package com.boug.employee.error;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationException extends RuntimeException  {

    private CustomError customError;

    public ApplicationException(CustomError customError){
        super();
        this.customError = customError;
    }
}
