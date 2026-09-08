package com.example.springdummyapp.repository;

import com.example.springdummyapp.model.dao.ThreadReference;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThreadReferenceRepository extends CrudRepository<ThreadReference,Integer> {
    ThreadReference getThreadReferenceByThreadReference(String threadReference);
}
