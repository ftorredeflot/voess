package com.kdejf.voess.repository;

import com.kdejf.voess.domain.UserLikedTeams;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserLikedTeams entity.
 */
@SuppressWarnings("unused")
public interface UserLikedTeamsRepository extends JpaRepository<UserLikedTeams,Long> {

    @Query("select userLikedTeams from UserLikedTeams userLikedTeams where userLikedTeams.user.login = ?#{principal.username}")
    List<UserLikedTeams> findByUserIsCurrentUser();

}
