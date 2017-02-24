package com.kdejf.voess.repository;

import com.kdejf.voess.domain.Friendship;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Friendship entity.
 */
@SuppressWarnings("unused")
public interface FriendshipRepository extends JpaRepository<Friendship,Long> {

    @Query("select friendship from Friendship friendship where friendship.frienshipFrom.login = ?#{principal.username}")
    List<Friendship> findByFrienshipFromIsCurrentUser();

    @Query("select friendship from Friendship friendship where friendship.frienshipTo.login = ?#{principal.username}")
    List<Friendship> findByFrienshipToIsCurrentUser();

}
