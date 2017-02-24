package com.kdejf.voess.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.kdejf.voess.domain.TeamFormer;

import com.kdejf.voess.repository.TeamFormerRepository;
import com.kdejf.voess.web.rest.util.HeaderUtil;
import com.kdejf.voess.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing TeamFormer.
 */
@RestController
@RequestMapping("/api")
public class TeamFormerResource {

    private final Logger log = LoggerFactory.getLogger(TeamFormerResource.class);
        
    @Inject
    private TeamFormerRepository teamFormerRepository;

    /**
     * POST  /team-formers : Create a new teamFormer.
     *
     * @param teamFormer the teamFormer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new teamFormer, or with status 400 (Bad Request) if the teamFormer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/team-formers")
    @Timed
    public ResponseEntity<TeamFormer> createTeamFormer(@RequestBody TeamFormer teamFormer) throws URISyntaxException {
        log.debug("REST request to save TeamFormer : {}", teamFormer);
        if (teamFormer.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("teamFormer", "idexists", "A new teamFormer cannot already have an ID")).body(null);
        }
        TeamFormer result = teamFormerRepository.save(teamFormer);
        return ResponseEntity.created(new URI("/api/team-formers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("teamFormer", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /team-formers : Updates an existing teamFormer.
     *
     * @param teamFormer the teamFormer to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated teamFormer,
     * or with status 400 (Bad Request) if the teamFormer is not valid,
     * or with status 500 (Internal Server Error) if the teamFormer couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/team-formers")
    @Timed
    public ResponseEntity<TeamFormer> updateTeamFormer(@RequestBody TeamFormer teamFormer) throws URISyntaxException {
        log.debug("REST request to update TeamFormer : {}", teamFormer);
        if (teamFormer.getId() == null) {
            return createTeamFormer(teamFormer);
        }
        TeamFormer result = teamFormerRepository.save(teamFormer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("teamFormer", teamFormer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /team-formers : get all the teamFormers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of teamFormers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/team-formers")
    @Timed
    public ResponseEntity<List<TeamFormer>> getAllTeamFormers(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of TeamFormers");
        Page<TeamFormer> page = teamFormerRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/team-formers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /team-formers/:id : get the "id" teamFormer.
     *
     * @param id the id of the teamFormer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the teamFormer, or with status 404 (Not Found)
     */
    @GetMapping("/team-formers/{id}")
    @Timed
    public ResponseEntity<TeamFormer> getTeamFormer(@PathVariable Long id) {
        log.debug("REST request to get TeamFormer : {}", id);
        TeamFormer teamFormer = teamFormerRepository.findOne(id);
        return Optional.ofNullable(teamFormer)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /team-formers/:id : delete the "id" teamFormer.
     *
     * @param id the id of the teamFormer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/team-formers/{id}")
    @Timed
    public ResponseEntity<Void> deleteTeamFormer(@PathVariable Long id) {
        log.debug("REST request to delete TeamFormer : {}", id);
        teamFormerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("teamFormer", id.toString())).build();
    }

}
