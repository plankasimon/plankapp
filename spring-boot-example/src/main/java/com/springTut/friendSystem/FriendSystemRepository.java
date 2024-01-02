package com.springTut.friendSystem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springTut.user.User;

public interface FriendSystemRepository extends JpaRepository<FriendSystem, Integer> {
    @Query("SELECT u.firstname, u.lastname, u.email FROM User u JOIN FriendSystem f ON u.id = CASE WHEN f.userId1 = :userId THEN f.userId2 ELSE f.userId1 END WHERE (:userId IN (f.userId1, f.userId2)) AND f.status = 'resolved'")
    List<User> findAllFriends(Integer userId);
    @Query("SELECT f FROM FriendSystem f WHERE (f.userId1 = :userId OR f.userId2 = :userId) AND f.status = 'unresolved'")
    List<FriendSystem> findFriendRequests(Integer userId);
    @Query("SELECT f FROM FriendSystem f WHERE (f.userId1 = :userId1 AND f.userId2 = :userId2 AND f.status = 'unresolved')")
    FriendSystem findFriendRequestByUserAndId(Integer userId1, Integer userId2);
    /*@Query("SELECT f FROM FriendSystem f WHERE (f.userId1 = :userId1 AND f.userId2 = :userId2 AND f.status = 'resolved')")
    FriendSystem findFriend(Integer userId1, Integer userId2);*/ // ASI NA OVĚŘENÍ JESTLI UŽ JSOU FRIENDS PŘI CREATE FRIEND REQUEST

}



