package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.MonAn;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the MonAn entity.
 */
public interface MonAnSearchRepository extends ElasticsearchRepository<MonAn, Long> {
}
