package com.roy.repository;

import com.roy.model.BlogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2016/8/19.
 */
@Repository
public interface BlogRepository extends JpaRepository<BlogEntity ,Integer> {
}
