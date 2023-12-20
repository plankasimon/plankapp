package com.springTut.post.request;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostRequest {

    private String title;

    private String body;

    private String tag;

    private Integer userId;

    public void checkIsEmpty() {
        if (getTitle().isEmpty()) {
            throw new IllegalArgumentException("No title in request");
        }
        if (getBody().isEmpty()) {
            throw new IllegalArgumentException("No body in request");
        }
    }

    public void checkTagEmpty(){
        if (getTag().isEmpty()){
            throw new IllegalArgumentException("No tag provided in body");
        }
    }

}
