package com.example.springdummyapp.repository;

import com.example.springdummyapp.model.dao.ThreadReference;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ThreadReferenceRepository extends CrudRepository<ThreadReference,Integer> {
    ThreadReference getThreadReferenceByThreadReference(String threadReference);

    @Query("""
    SELECT tr
    FROM ThreadReference tr
    WHERE tr.createdTimeStamp <= :createdTimeStampBefore
    """)
    List<ThreadReference> getThreadReferencesCreatedBefore(
            @Param("createdTimeStampBefore") LocalDateTime createdTimeStampBefore
    );

    @Query("""
    SELECT tr
    FROM ThreadReference tr
    WHERE tr.createdTimeStamp <= :beforeThis
        AND tr.createdTimeStamp >= :afterThis
    """)
    List<ThreadReference> getThreadReferencesCreatedBetweenTimeStamps(
            @Param("beforeThis") LocalDateTime beforeThis,
            @Param("afterThis") LocalDateTime afterThis
    );
}
