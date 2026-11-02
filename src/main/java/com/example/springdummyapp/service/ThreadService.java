package com.example.springdummyapp.service;

import com.example.springdummyapp.model.dao.Thread;
import com.example.springdummyapp.model.dao.ThreadReference;
import com.example.springdummyapp.model.dao.ThreadWorker;
import com.example.springdummyapp.repository.ThreadReferenceRepository;
import com.example.springdummyapp.repository.ThreadRepository;
import com.example.springdummyapp.repository.ThreadWorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ThreadService {

    private final ThreadRepository threadRepository;
    private final ThreadWorkerRepository threadWorkerRepository;
    private final ThreadReferenceRepository threadReferenceRepository;

    public Thread getThreadById(Long id) {
        return threadRepository.getThreadById(id);
    }

    public Iterable<Thread> getThreads() {
        return threadRepository.findAll();
    }

    public List<ThreadReference> getThreadReferenceByCreatedDate(LocalDateTime date) {
        return threadReferenceRepository.getThreadReferencesCreatedBefore(date);
    }

    public List<ThreadReference> getThreadReferencesCreatedBetweenTimeStamps(LocalDateTime before, LocalDateTime after) {
        return threadReferenceRepository.getThreadReferencesCreatedBetweenTimeStamps(before, after);
    }

    public String getThreadWorkerNameByThreadName(String threadName) {
        return threadWorkerRepository.customQuery(threadName)
                .getFirst();
    }

    public String saveThreadSuccessResponse (ThreadReference toAdd){
        threadReferenceRepository.save(toAdd);
        return toAdd.getThreadReference();
    }

    public String deleteThreadReference(ThreadReference toDelete) {
        String reference = toDelete.getThreadReference();
        threadReferenceRepository.delete(toDelete);
        return reference;
    }

    public ThreadReference getThreadReferenceByThreadReference(String threadReference){
        return threadReferenceRepository.getThreadReferenceByThreadReference(threadReference);
    }

    public String getThreadWorkerNameByThreadName2(String threadName) {
        return threadWorkerRepository.findByThreadThreadName(threadName)
                .stream()
                .map(ThreadWorker::getCaseWorkerName)
                .collect(Collectors.joining());
    }
}
