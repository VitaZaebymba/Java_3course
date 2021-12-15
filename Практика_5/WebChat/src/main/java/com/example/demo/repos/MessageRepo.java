package com.example.demo.repos;

import com.example.demo.entiy.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepo extends JpaRepository<Message,Long> {
    Optional<Message> findById(Long id);
    List<Message> findAllByAuthor(String author);
}
