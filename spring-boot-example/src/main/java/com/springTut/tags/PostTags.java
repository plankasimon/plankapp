package com.springTut.tags;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "post_tags")
@Table(name = "post_tags")
public class PostTags {

    @Id
    @SequenceGenerator(name = "post_tags_id_sequence", sequenceName = "post_tags_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_tags_id_sequence")
    @Column(name = "id")
    private Integer id;

    @Column(name = "post_id")
    private Integer postId;

    @Column(name = "tag_id")
    private Integer tagId;
}
