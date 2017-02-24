package com.kdejf.voess.repository;

import com.kdejf.voess.domain.TeamFormer;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TeamFormer entity.
 */
@SuppressWarnings("unused")
public interface TeamFormerRepository extends JpaRepository<TeamFormer,Long> {

}
