package com.springTut.friendSystem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springTut.user.User;
import com.springTut.user.UserProjection;

public interface FriendSystemRepository extends JpaRepository<FriendSystem, Integer> {
    @Query("SELECT new com.springTut.user.UserProjection(u.firstname, u.lastname, u.email) FROM User u JOIN FriendSystem f ON u.id = CASE WHEN f.userId1 = :userId THEN f.userId2 ELSE f.userId1 END WHERE (:userId IN (f.userId1, f.userId2)) AND f.status = 'resolved'")
    List<UserProjection> findAllFriends(Integer userId);

    @Query("SELECT f FROM FriendSystem f WHERE (f.userId1 = :userId OR f.userId2 = :userId) AND f.status = 'unresolved'")
    List<FriendSystem> findFriendRequests(Integer userId);

    @Query("SELECT f FROM FriendSystem f WHERE (f.userId1 = :userId1 AND f.userId2 = :userId2 AND f.status = 'unresolved')")
    FriendSystem findFriendRequestByUserAndId(Integer userId1, Integer userId2);

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM FriendSystem f WHERE ((f.userId1 = :userId1 AND f.userId2 = :userId2) OR (f.userId1 = :userId2 AND f.userId2 = :userId1)) AND f.status = 'resolved'")
    boolean existsFriend(Integer userId1, Integer userId2);

}



