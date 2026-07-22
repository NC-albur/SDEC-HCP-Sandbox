package com.example.springdummyapp.repository;


import com.example.springdummyapp.model.dao.ThreadWorker;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThreadWorkerRepository extends CrudRepository<ThreadWorker, Integer> {

    List<ThreadWorker> findByThreadThreadName(String threadName);

    @Query("SELECT tw.caseWorkerName FROM ThreadWorker tw " +
            "LEFT JOIN thread t on tw.thread.id = t.id " +
            "WHERE t.threadName = :threadName")
    List<String> customQuery(@Param("threadName") String threadName);
}
