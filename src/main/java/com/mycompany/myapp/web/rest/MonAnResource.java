package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.MonAn;

import com.mycompany.myapp.repository.MonAnRepository;
import com.mycompany.myapp.repository.search.MonAnSearchRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing MonAn.
 */
@RestController
@RequestMapping("/api")
public class MonAnResource {

    private final Logger log = LoggerFactory.getLogger(MonAnResource.class);

    @Inject
    private MonAnRepository monAnRepository;

    @Inject
    private MonAnSearchRepository monAnSearchRepository;

    /**
     * POST  /mon-ans : Create a new monAn.
     *
     * @param monAn the monAn to create
     * @return the ResponseEntity with status 201 (Created) and with body the new monAn, or with status 400 (Bad Request) if the monAn has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/mon-ans",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MonAn> createMonAn(@Valid @RequestBody MonAn monAn) throws URISyntaxException {
        log.debug("REST request to save MonAn : {}", monAn);
        if (monAn.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("monAn", "idexists", "A new monAn cannot already have an ID")).body(null);
        }
        MonAn result = monAnRepository.save(monAn);
        monAnSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/mon-ans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("monAn", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mon-ans : Updates an existing monAn.
     *
     * @param monAn the monAn to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated monAn,
     * or with status 400 (Bad Request) if the monAn is not valid,
     * or with status 500 (Internal Server Error) if the monAn couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/mon-ans",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MonAn> updateMonAn(@Valid @RequestBody MonAn monAn) throws URISyntaxException {
        log.debug("REST request to update MonAn : {}", monAn);
        if (monAn.getId() == null) {
            return createMonAn(monAn);
        }
        MonAn result = monAnRepository.save(monAn);
        monAnSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("monAn", monAn.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mon-ans : get all the monAns.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of monAns in body
     */
    @RequestMapping(value = "/mon-ans",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<MonAn> getAllMonAns() {
        log.debug("REST request to get all MonAns");
        List<MonAn> monAns = monAnRepository.findAll();
        return monAns;
    }

    /**
     * GET  /mon-ans/:id : get the "id" monAn.
     *
     * @param id the id of the monAn to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the monAn, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/mon-ans/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MonAn> getMonAn(@PathVariable Long id) {
        log.debug("REST request to get MonAn : {}", id);
        MonAn monAn = monAnRepository.findOne(id);
        return Optional.ofNullable(monAn)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /mon-ans/:id : delete the "id" monAn.
     *
     * @param id the id of the monAn to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/mon-ans/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMonAn(@PathVariable Long id) {
        log.debug("REST request to delete MonAn : {}", id);
        monAnRepository.delete(id);
        monAnSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("monAn", id.toString())).build();
    }

    /**
     * SEARCH  /_search/mon-ans?query=:query : search for the monAn corresponding
     * to the query.
     *
     * @param query the query of the monAn search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/mon-ans",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<MonAn> searchMonAns(@RequestParam String query) {
        log.debug("REST request to search MonAns for query {}", query);
        return StreamSupport
            .stream(monAnSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
    @RequestMapping(value = "/MonAn/theloai",
     method = RequestMethod.GET,
     produces = MediaType.APPLICATION_JSON_VALUE )
    @Timed public List<MonAn> getMonAn_byTheloai(@RequestParam String theloai){
        List<MonAn> result = monAnRepository.findMonAntheloai(theloai);
        return  result ;
    }



}
