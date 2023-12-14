package com.springTut.post.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {

    private String title;

    private String body;

    private Integer userId;
    public void checkIsEmpty(){
        if (getTitle().isEmpty()){
            throw new IllegalArgumentException("No title in request");
        }
        if (getBody().isEmpty()){
            throw new IllegalArgumentException("No body in request");
        }
    }

}
