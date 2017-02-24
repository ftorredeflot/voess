package com.kdejf.voess.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.kdejf.voess.domain.UserExt;

import com.kdejf.voess.repository.UserExtRepository;
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
 * REST controller for managing UserExt.
 */
@RestController
@RequestMapping("/api")
public class UserExtResource {

    private final Logger log = LoggerFactory.getLogger(UserExtResource.class);
        
    @Inject
    private UserExtRepository userExtRepository;

    /**
     * POST  /user-exts : Create a new userExt.
     *
     * @param userExt the userExt to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userExt, or with status 400 (Bad Request) if the userExt has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-exts")
    @Timed
    public ResponseEntity<UserExt> createUserExt(@Valid @RequestBody UserExt userExt) throws URISyntaxException {
        log.debug("REST request to save UserExt : {}", userExt);
        if (userExt.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userExt", "idexists", "A new userExt cannot already have an ID")).body(null);
        }
        UserExt result = userExtRepository.save(userExt);
        return ResponseEntity.created(new URI("/api/user-exts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("userExt", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-exts : Updates an existing userExt.
     *
     * @param userExt the userExt to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userExt,
     * or with status 400 (Bad Request) if the userExt is not valid,
     * or with status 500 (Internal Server Error) if the userExt couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-exts")
    @Timed
    public ResponseEntity<UserExt> updateUserExt(@Valid @RequestBody UserExt userExt) throws URISyntaxException {
        log.debug("REST request to update UserExt : {}", userExt);
        if (userExt.getId() == null) {
            return createUserExt(userExt);
        }
        UserExt result = userExtRepository.save(userExt);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("userExt", userExt.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-exts : get all the userExts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of userExts in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/user-exts")
    @Timed
    public ResponseEntity<List<UserExt>> getAllUserExts(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of UserExts");
        Page<UserExt> page = userExtRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/user-exts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /user-exts/:id : get the "id" userExt.
     *
     * @param id the id of the userExt to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userExt, or with status 404 (Not Found)
     */
    @GetMapping("/user-exts/{id}")
    @Timed
    public ResponseEntity<UserExt> getUserExt(@PathVariable Long id) {
        log.debug("REST request to get UserExt : {}", id);
        UserExt userExt = userExtRepository.findOne(id);
        return Optional.ofNullable(userExt)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /user-exts/:id : delete the "id" userExt.
     *
     * @param id the id of the userExt to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-exts/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserExt(@PathVariable Long id) {
        log.debug("REST request to delete UserExt : {}", id);
        userExtRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("userExt", id.toString())).build();
    }

}
