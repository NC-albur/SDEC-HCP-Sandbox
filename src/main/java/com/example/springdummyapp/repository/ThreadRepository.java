package com.example.springdummyapp.repository;


import com.example.springdummyapp.model.dao.Thread;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThreadRepository extends CrudRepository<Thread, Integer> {

    Thread getThreadById(Long id);
}
