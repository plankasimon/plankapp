package com.springTut.core.request;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;

@Setter
@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CrudRequest {

    private String firstname;
    private String lastname;
    private String email;

    public void checkIsEmpty(){
        if(getFirstname().isEmpty()){
            throw new IllegalArgumentException("No firstname in body");
        }
        if (getLastname().isEmpty()){
            throw new IllegalArgumentException("No lastname in request");
        }
        if (getEmail().isEmpty()){
            throw new IllegalArgumentException("No email in request");
        }
    }

}
