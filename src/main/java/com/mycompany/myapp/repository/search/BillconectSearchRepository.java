package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Billconect;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Billconect entity.
 */
public interface BillconectSearchRepository extends ElasticsearchRepository<Billconect, Long> {
}
