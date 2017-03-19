package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Bill;
import com.mycompany.myapp.domain.Billconect;

import com.mycompany.myapp.domain.MonAn;
import com.mycompany.myapp.repository.BillRepository;
import com.mycompany.myapp.repository.BillconectRepository;
import com.mycompany.myapp.repository.search.BillconectSearchRepository;
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
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Billconect.
 */
@RestController
@RequestMapping("/api")
public class BillconectResource {

    private final Logger log = LoggerFactory.getLogger(BillconectResource.class);

    @Inject
    private BillconectRepository billconectRepository;

    @Inject
    private BillconectSearchRepository billconectSearchRepository;

    /**
     * POST  /billconects : Create a new billconect.
     *
     * @param billconect the billconect to create
     * @return the ResponseEntity with status 201 (Created) and with body the new billconect, or with status 400 (Bad Request) if the billconect has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/billconects",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Billconect> createBillconect(@Valid @RequestBody Billconect billconect) throws URISyntaxException {
        log.debug("REST request to save Billconect : {}", billconect);
        if (billconect.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("billconect", "idexists", "A new billconect cannot already have an ID")).body(null);
        }
        Billconect result = billconectRepository.save(billconect);
        billconectSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/billconects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("billconect", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /billconects : Updates an existing billconect.
     *
     * @param billconect the billconect to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated billconect,
     * or with status 400 (Bad Request) if the billconect is not valid,
     * or with status 500 (Internal Server Error) if the billconect couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/billconects",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Billconect> updateBillconect(@Valid @RequestBody Billconect billconect) throws URISyntaxException {
        log.debug("REST request to update Billconect : {}", billconect);
        if (billconect.getId() == null) {
            return createBillconect(billconect);
        }
        Billconect result = billconectRepository.save(billconect);
        billconectSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("billconect", billconect.getId().toString()))
            .body(result);
    }

    /**
     * GET  /billconects : get all the billconects.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of billconects in body
     */
    @RequestMapping(value = "/billconects",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Billconect> getAllBillconects() {
        log.debug("REST request to get all Billconects");
        List<Billconect> billconects = billconectRepository.findAll();
        return billconects;
    }

    /**
     * GET  /billconects/:id : get the "id" billconect.
     *
     * @param id the id of the billconect to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the billconect, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/billconects/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Billconect> getBillconect(@PathVariable Long id) {
        log.debug("REST request to get Billconect : {}", id);
        Billconect billconect = billconectRepository.findOne(id);
        return Optional.ofNullable(billconect)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /billconects/:id : delete the "id" billconect.
     *
     * @param id the id of the billconect to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/billconects/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBillconect(@PathVariable Long id) {
        log.debug("REST request to delete Billconect : {}", id);
        billconectRepository.delete(id);
        billconectSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("billconect", id.toString())).build();
    }

    /**
     * SEARCH  /_search/billconects?query=:query : search for the billconect corresponding
     * to the query.
     *
     * @param query the query of the billconect search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/billconects",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Billconect> searchBillconects(@RequestParam String query) {
        log.debug("REST request to search Billconects for query {}", query);
        return StreamSupport
            .stream(billconectSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    /** get thanh_toan bill by id_bill
     *
     */
    @RequestMapping(value = "/thanhtoan/{bill_id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE )
    @Timed

     public Bill_thanhtoan get_thanhtoan(@RequestParam Long id_bill){
        double result_Ship = Ship_bill(id_bill);
        double result_thanhtoan = 0;
        double result_sale = 0;

        Bill_thanhtoan bills = new Bill_thanhtoan();
        List<Billconect> billconects = billconectRepository.findAll();

        if(billconects.size() != 0){

            for(int i=0; i< billconects.size() ; i++){

                if(billconects.get(i).getBill().getId() == id_bill){
                    result_thanhtoan = result_thanhtoan + billconects.get(i).get_thanhtoan();
                    result_sale = result_sale + billconects.get(i).get_thanhtoansale();


                }

            }
            bills.setBill_thanhtoan(result_thanhtoan,result_sale,result_Ship);

            return bills;


        }
        else  return null;
    };

    public double Ship_bill(long id_bill){
        double result = 0 ;
        List<Billconect> billconects = billconectRepository.findAll();
        if(billconects.size() != 0){
            for(int i = 0; i<billconects.size() ; i++){
                if(billconects.get(i).getBill().getId() == id_bill){

                    result = billconects.get(i).getBill().getShip();
                    break;
                }
            }
            return result;
        }
        else {
            return 0;
        }

    }

    @RequestMapping(value = "/danhsachMA/{bill_id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public List<Billconect> getDanhsachMA(@RequestParam long bill_id){
        List<Billconect> list_result = new ArrayList<Billconect>();
        List<Billconect> billconects = billconectRepository.findAll();
        if(billconects.size() != 0){
            for(int i = 0;i <billconects.size();i++){
                if(billconects.get(i).getBill().getId() == bill_id){
                    System.out.print("\n========");
                    list_result.add(billconects.get(i));
                }
            }
            return list_result;
        }
        else {
            return null;
        }

    }
    @RequestMapping(value = "/danhsachMAdh",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed List<MonAn> getDsMonAn_dathang(){
        List<MonAn> list_result = billconectRepository.findMonAndathang();
        return  list_result;
    }





}


