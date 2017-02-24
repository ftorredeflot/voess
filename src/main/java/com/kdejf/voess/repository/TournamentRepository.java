package com.kdejf.voess.repository;

import com.kdejf.voess.domain.Tournament;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Tournament entity.
 */
@SuppressWarnings("unused")
public interface TournamentRepository extends JpaRepository<Tournament,Long> {

    @Query("select distinct tournament from Tournament tournament left join fetch tournament.teamNames")
    List<Tournament> findAllWithEagerRelationships();

    @Query("select tournament from Tournament tournament left join fetch tournament.teamNames where tournament.id =:id")
    Tournament findOneWithEagerRelationships(@Param("id") Long id);

}
