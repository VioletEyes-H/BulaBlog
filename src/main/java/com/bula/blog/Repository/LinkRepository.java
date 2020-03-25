package com.bula.blog.Repository;

import com.bula.blog.Entity.Link;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface LinkRepository extends PagingAndSortingRepository<Link, Integer> {

    Link findByLinkName(String linkName);

    Link findByLinkId(int id);
}
