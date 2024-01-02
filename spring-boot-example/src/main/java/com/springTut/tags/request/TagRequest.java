package com.springTut.tags.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagRequest {

    private String tagName;
    private Integer postId;

    public void checkIsEmpty(){
        if (getTagName().isEmpty()){
            throw new IllegalArgumentException("No tag name in request");
        }
    }
}
