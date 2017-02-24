package com.kdejf.voess.repository;

import com.kdejf.voess.domain.UserLikedVideo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserLikedVideo entity.
 */
@SuppressWarnings("unused")
public interface UserLikedVideoRepository extends JpaRepository<UserLikedVideo,Long> {

    @Query("select userLikedVideo from UserLikedVideo userLikedVideo where userLikedVideo.user.login = ?#{principal.username}")
    List<UserLikedVideo> findByUserIsCurrentUser();

}
