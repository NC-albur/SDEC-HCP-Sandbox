package com.example.springdummyapp.service;

import com.example.springdummyapp.model.dao.Thread;
import com.example.springdummyapp.repository.ThreadRepository;
import com.example.springdummyapp.repository.ThreadWorkerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ThreadServiceTest {
    @Mock
    private ThreadRepository threadRepository;
    @Mock
    private ThreadWorkerRepository threadWorkerRepository;

    @InjectMocks
    private ThreadService threadService;

    @Test
    void getThreadByIdSuccess() {
        // assign
        Long l = Long.valueOf("1000");
        when(threadRepository.getThreadById(l)).thenReturn(new Thread("name", new Timestamp(1000000)));

        //act
        Thread t = threadService.getThreadById(l);

        //assert
        assertNotNull(t);
        assertEquals("name", t.getThreadName());
    }

    @Test
    void getThreadByIdFailure() {
        // assign
        Long l = Long.valueOf("1000");
        when(threadRepository.getThreadById(l)).thenReturn(null);

        //act
        Thread t = threadService.getThreadById(l);

        //assert
        assertNull(t);
    }

    @Test
    void getThreads() {
        // assign
        List<Thread> threads = new ArrayList<>();
        threads.add(new Thread());

        when(threadRepository.findAll()).thenReturn(threads);

        // act
        Iterable<Thread> ls = threadService.getThreads();

        // assert
        assertNotNull(ls);
        ArrayList al = (ArrayList) ls;
        assertEquals(1, al.size());
    }

    @Test
    void getThreadWorkerNameByThreadNameSuccess() {
        // assign
        String threadName = "name";
        List<String> workerNames = new ArrayList<>(){
            {
                add("name");
            }
        };
        when(threadWorkerRepository.customQuery(threadName)).thenReturn(workerNames);

        // act
        String first = threadService.getThreadWorkerNameByThreadName(threadName);

        // assert
        assertEquals("name", first);
    }

    @Test
    void getThreadWorkerNameByThreadNameFailure() {
        // assign
        String threadName = "name";
        List<String> workerNames = new ArrayList<>(){};
        when(threadWorkerRepository.customQuery(threadName)).thenReturn(workerNames);

        // act & assert
        assertThrows(NoSuchElementException.class, () -> threadService.getThreadWorkerNameByThreadName(threadName));
    }
}