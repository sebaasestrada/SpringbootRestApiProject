package com.strada.movies.controllers;

import com.strada.movies.models.MovieProject;
import com.strada.movies.repositories.MovieProjectRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/movies")
public class MovieProjectController {

    @Autowired
    private MovieProjectRepository movieProjectRepository;
    
    @CrossOrigin
    @GetMapping
    public List<MovieProject> getAllMovies() {
        return movieProjectRepository.findAll();
    }
    
    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<MovieProject> getMovieById(@PathVariable Long id) {
        Optional<MovieProject> movie = movieProjectRepository.findById(id);
        return movie.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @CrossOrigin
    @PostMapping
    public ResponseEntity<MovieProject> createMovie(@RequestBody MovieProject movie) {
        MovieProject savedMovie = movieProjectRepository.save(movie);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);
    }
    
    @CrossOrigin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        if (!movieProjectRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        movieProjectRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @CrossOrigin
    @PutMapping("/{id}")
    public ResponseEntity<MovieProject> updateMovie(@PathVariable Long id, @RequestBody MovieProject updatedMovie) {
        if (!movieProjectRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        updatedMovie.setId(id);
        MovieProject savedMovie = movieProjectRepository.save(updatedMovie);
        return ResponseEntity.ok(savedMovie);
    }
    
    @CrossOrigin
    @GetMapping("/vote/{id}/{rating}")
    public ResponseEntity<MovieProject> voteMovie(@PathVariable Long id, @PathVariable double rating) {
        if (!movieProjectRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Optional<MovieProject> optional = movieProjectRepository.findById(id);
        MovieProject votedMovie = optional.get();
        
        double newRating = ( (votedMovie.getVotes() * votedMovie.getRating()) + rating) / (votedMovie.getVotes() + 1) ;
        
        votedMovie.setVotes(votedMovie.getVotes() + 1);
        votedMovie.setRating(newRating);
        
        MovieProject savedMovie = movieProjectRepository.save(votedMovie);
        
        return ResponseEntity.ok(savedMovie);
    }
}
