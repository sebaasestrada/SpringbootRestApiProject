package com.strada.movies.repositories;

import com.strada.movies.models.MovieProject;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MovieProjectRepository extends JpaRepository<MovieProject, Long> {
    
}
