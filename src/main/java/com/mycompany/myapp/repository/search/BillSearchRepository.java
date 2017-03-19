package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Bill;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Bill entity.
 */
public interface BillSearchRepository extends ElasticsearchRepository<Bill, Long> {
}
