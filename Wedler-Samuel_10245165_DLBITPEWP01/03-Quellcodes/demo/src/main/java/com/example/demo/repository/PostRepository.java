package com.example.demo.repository;

import com.example.demo.model.post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<post, Integer> {
    List<post> findByPostID(int PostId);
    Page<post> findByUserID(Long userId, Pageable pageable);
    List<post> findAllByUserID(Long userId);

}
