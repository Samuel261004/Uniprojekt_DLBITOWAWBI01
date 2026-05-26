package com.example.demo.repository;

import com.example.demo.model.answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.*;

@Repository
public interface answerRepository extends JpaRepository<answer,Integer> {
	Page<answer> findByPostid(int postid, Pageable pageable);
	List<answer> findAllByPostid(int postid);
    void deleteByuserid(Long PostId);
    void deleteByPostid(Integer PostId);
    boolean existsByuserid(Long PostId);
    long countByPostid(int postid);
}
