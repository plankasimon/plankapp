package com.springTut.likes;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Like")
@Table(name = "likes")
public class Likes {

    @Id
    @SequenceGenerator(name = "post_id_sequence", sequenceName = "post_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_id_sequence")
    @Column(name = "id")
    private Integer id;

    @Column(name = "postid")
    private Integer postId;

    @Column(name = "userid")
    private Integer userId;

    @Column(name = "liked")
    private boolean liked;
}
