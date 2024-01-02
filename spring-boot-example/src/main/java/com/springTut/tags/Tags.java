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
@Entity(name = "Tags")
@Table(name = "tags")
public class Tags {
    @Id
    @SequenceGenerator(name = "tag_id_sequence", sequenceName = "tag_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tag_id_sequence")
    @Column(name = "id")
    private Integer id;
    @Column(name = "tag_name")
    private String tagName;
}
