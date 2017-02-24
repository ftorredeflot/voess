package com.kdejf.voess.repository;

import com.kdejf.voess.domain.Video;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Video entity.
 */
@SuppressWarnings("unused")
public interface VideoRepository extends JpaRepository<Video,Long> {

    @Query("select distinct video from Video video left join fetch video.players")
    List<Video> findAllWithEagerRelationships();

    @Query("select video from Video video left join fetch video.players where video.id =:id")
    Video findOneWithEagerRelationships(@Param("id") Long id);

}
