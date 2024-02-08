package com.springTut.friendSystem;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "FriendSystem")
@Table(name = "friends")
public class FriendSystem {

    @Id
    @SequenceGenerator(name = "friend_id_sequence", sequenceName = "friend_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "friend_id_sequence")
    @Column(name = "id")
    private Integer id;

    @Column(name = "userid1")
    private Integer userId1;

    @Column(name = "userid2")
    private Integer userId2;

    @Column(name = "status")
    private String status;


}
