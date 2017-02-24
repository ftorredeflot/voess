package com.kdejf.voess.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.kdejf.voess.domain.UserLikedPlayer;

import com.kdejf.voess.repository.UserLikedPlayerRepository;
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
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing UserLikedPlayer.
 */
@RestController
@RequestMapping("/api")
public class UserLikedPlayerResource {

    private final Logger log = LoggerFactory.getLogger(UserLikedPlayerResource.class);
        
    @Inject
    private UserLikedPlayerRepository userLikedPlayerRepository;

    /**
     * POST  /user-liked-players : Create a new userLikedPlayer.
     *
     * @param userLikedPlayer the userLikedPlayer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userLikedPlayer, or with status 400 (Bad Request) if the userLikedPlayer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-liked-players")
    @Timed
    public ResponseEntity<UserLikedPlayer> createUserLikedPlayer(@Valid @RequestBody UserLikedPlayer userLikedPlayer) throws URISyntaxException {
        log.debug("REST request to save UserLikedPlayer : {}", userLikedPlayer);
        if (userLikedPlayer.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userLikedPlayer", "idexists", "A new userLikedPlayer cannot already have an ID")).body(null);
        }
        UserLikedPlayer result = userLikedPlayerRepository.save(userLikedPlayer);
        return ResponseEntity.created(new URI("/api/user-liked-players/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("userLikedPlayer", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-liked-players : Updates an existing userLikedPlayer.
     *
     * @param userLikedPlayer the userLikedPlayer to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userLikedPlayer,
     * or with status 400 (Bad Request) if the userLikedPlayer is not valid,
     * or with status 500 (Internal Server Error) if the userLikedPlayer couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-liked-players")
    @Timed
    public ResponseEntity<UserLikedPlayer> updateUserLikedPlayer(@Valid @RequestBody UserLikedPlayer userLikedPlayer) throws URISyntaxException {
        log.debug("REST request to update UserLikedPlayer : {}", userLikedPlayer);
        if (userLikedPlayer.getId() == null) {
            return createUserLikedPlayer(userLikedPlayer);
        }
        UserLikedPlayer result = userLikedPlayerRepository.save(userLikedPlayer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("userLikedPlayer", userLikedPlayer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-liked-players : get all the userLikedPlayers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of userLikedPlayers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/user-liked-players")
    @Timed
    public ResponseEntity<List<UserLikedPlayer>> getAllUserLikedPlayers(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of UserLikedPlayers");
        Page<UserLikedPlayer> page = userLikedPlayerRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/user-liked-players");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /user-liked-players/:id : get the "id" userLikedPlayer.
     *
     * @param id the id of the userLikedPlayer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userLikedPlayer, or with status 404 (Not Found)
     */
    @GetMapping("/user-liked-players/{id}")
    @Timed
    public ResponseEntity<UserLikedPlayer> getUserLikedPlayer(@PathVariable Long id) {
        log.debug("REST request to get UserLikedPlayer : {}", id);
        UserLikedPlayer userLikedPlayer = userLikedPlayerRepository.findOne(id);
        return Optional.ofNullable(userLikedPlayer)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /user-liked-players/:id : delete the "id" userLikedPlayer.
     *
     * @param id the id of the userLikedPlayer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-liked-players/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserLikedPlayer(@PathVariable Long id) {
        log.debug("REST request to delete UserLikedPlayer : {}", id);
        userLikedPlayerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("userLikedPlayer", id.toString())).build();
    }

}
