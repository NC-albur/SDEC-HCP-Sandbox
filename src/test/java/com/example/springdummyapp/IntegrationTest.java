package com.example.springdummyapp;

import com.example.springdummyapp.model.dao.ThreadReference;
import com.example.springdummyapp.model.request.CreateThreadRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class IntegrationTest {

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    // url strings
    private static final String BASE_URL = "http://localhost:8080/v1/dummy/";
    public static final String UPDATE_THREAD = "UpdateThread/";
    public static final String ID = "/id/";
    public static final String DELETE_THREAD = "deleteThread/";
    public static final String CREATE_THREAD = "create-thread";
    public static final String BEFORE = "before/";
    public static final String AFTER = "/after/";
    public static final String OLDER_THAN = "olderThan/";

    @Test
    void getById() {
        String thread = restTemplate.getForObject(BASE_URL + ID + "1", String.class);
        assertNotNull(thread);
        assertEquals("Thread1", thread);
    }

    @Test
    void getByIdFail() {
        ResponseEntity<String> resp = restTemplate.getForEntity(BASE_URL + ID + "10", String.class);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode());
    }

    @Test
    void getByIdNotFound() {
        ResponseEntity<String> resp = restTemplate.getForEntity(BASE_URL + ID, String.class);
        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
    }

    @Test
    void getAllThreads() {
        List<String> threads = restTemplate.getForObject(BASE_URL, List.class);
        assertNotNull(threads);
        assertEquals(2, threads.size());
    }

    @Test
    void getThreadReference() {
        String reference = "THREAD-005";
        ResponseEntity<ThreadReference> resp = restTemplate.getForEntity(BASE_URL + reference, ThreadReference.class);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        ThreadReference ref = resp.getBody();
        assertEquals(reference, ref.getThreadReference());
    }

    @Test
    void getThreadReferenceNoContent() {
        String reference = "THREAD-009";
        ResponseEntity<ThreadReference> resp = restTemplate.getForEntity(BASE_URL + reference, ThreadReference.class);
        assertEquals(HttpStatus.NO_CONTENT, resp.getStatusCode());
        assertFalse(resp.hasBody());
    }

    @Nested
    class CreateThreadSuccessfully {
        @BeforeEach
        void deleteNewThread() {
            restTemplate.delete(BASE_URL + DELETE_THREAD + "REF-001");
        }

        @Test
        void createThreadSuccess() {
            CreateThreadRequest request = new CreateThreadRequest(
                    "name",
                    "123456",
                    "REF-001",
                    "Active",
                    "CASE-001"
            );

            ResponseEntity<String> resp = restTemplate.postForEntity(BASE_URL + CREATE_THREAD, request, String.class);
            assertEquals(HttpStatus.CREATED, resp.getStatusCode());
            var ref = resp.getBody();
            assertEquals(request.threadReference(), ref);
        }
    }

    @Test
    void createThread_NoBodyGiven() {
        ResponseEntity<String> resp = restTemplate.postForEntity(BASE_URL + CREATE_THREAD, null, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
    }

    @Test
    void updateThreadSuccess() {
        CreateThreadRequest request = new CreateThreadRequest(
                "name",
                "123456",
                "THREAD-001",
                "Active",
                "CASE-007"
        );

        // act
        restTemplate.put(BASE_URL + UPDATE_THREAD + request.threadReference(), request);

        // assert
        ResponseEntity<ThreadReference> resp = restTemplate.getForEntity(BASE_URL + request.threadReference(), ThreadReference.class);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        ThreadReference ref = resp.getBody();
        assertEquals(request.associatedCaseReference(), ref.getAssociatedCaseReference());
    }

    @Test
    void updateThreadFail() {
        CreateThreadRequest request = new CreateThreadRequest(
                "name",
                "123456",
                "THREAD-009",
                "Active",
                "CASE-007"
        );

        HttpEntity<CreateThreadRequest> entity = new HttpEntity<>(request);

        // act
        ResponseEntity<String> resp = restTemplate.exchange(
                BASE_URL + UPDATE_THREAD + request.threadReference(),
                HttpMethod.PUT,
                entity,
                String.class
        );

        // assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode());
    }

    @Test
    void updateThreadFail_NoRefGiven() {
        CreateThreadRequest request = new CreateThreadRequest(
                "name",
                "123456",
                "THREAD-009",
                "Active",
                "CASE-007"
        );

        HttpEntity<CreateThreadRequest> entity = new HttpEntity<>(request);

        // act
        ResponseEntity<String> resp = restTemplate.exchange(
                BASE_URL + UPDATE_THREAD,
                HttpMethod.PUT,
                entity,
                String.class
        );

        // assert
        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
    }

    @Test
    void updateThreadFail_NoBodyGiven() {
        CreateThreadRequest request = new CreateThreadRequest(
                "name",
                "123456",
                "THREAD-009",
                "Active",
                "CASE-007"
        );

        // act
        ResponseEntity<String> resp = restTemplate.exchange(
                BASE_URL + UPDATE_THREAD + request.threadReference(),
                HttpMethod.PUT,
                null,
                String.class
        );

        // assert
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
    }

    @Nested
    class SuccessfulDeleteAndCreateThreadAlreadyExists {
        private final String threadRef = "DELETE-ME";
        @BeforeEach
        void createNewThread() {
            CreateThreadRequest request = new CreateThreadRequest(
                    "name",
                    "123456",
                    threadRef,
                    "Active",
                    "CASE-001"
            );

            restTemplate.postForEntity(BASE_URL + CREATE_THREAD, request, String.class);
        }

        @Test
        void deleteThread() {
            // act
            ResponseEntity<String> resp = restTemplate.exchange(
                    BASE_URL + DELETE_THREAD + threadRef,
                    HttpMethod.DELETE,
                    null,
                    String.class
            );

            // assert
            assertEquals(HttpStatus.OK, resp.getStatusCode());
            String body = resp.getBody();
            assertEquals(threadRef, body);
        }

        @Test
        void createThreadAlreadyExists() {
            CreateThreadRequest request = new CreateThreadRequest(
                    "name",
                    "123456",
                    threadRef,
                    "Active",
                    "CASE-001"
            );

            var resp = restTemplate.postForEntity(BASE_URL + CREATE_THREAD, request, String.class);
            assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        }
    }

    @Test
    void deleteThreadFail() {
        // act
        ResponseEntity<String> resp = restTemplate.exchange(
                BASE_URL + DELETE_THREAD + "FAIL",
                HttpMethod.DELETE,
                null,
                String.class
        );

        // assert
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        String body = resp.getBody();
        assertEquals("File Not Found", body);
    }

    @Test
    void deleteThreadFail_NoRefGiven() {
        // act
        ResponseEntity<String> resp = restTemplate.exchange(
                BASE_URL + DELETE_THREAD,
                HttpMethod.DELETE,
                null,
                String.class
        );

        // assert
        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
    }

    @Test
    void getOlderThreadsSuccess() {
        LocalDateTime ldt = LocalDateTime.now();
        List<ThreadReference> response = restTemplate.getForObject(
                BASE_URL + OLDER_THAN + ldt,
                List.class
        );
        // assert
        assertNotNull(response);
    }

    @Test
    void getOlderThreadsFail_NoTimeGiven() {
        ResponseEntity<List<ThreadReference>> response = restTemplate.getForEntity(
                BASE_URL + OLDER_THAN,
                null,
                new ParameterizedTypeReference<List<ThreadReference>>(){}
        );
        // assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getThreadsBetweenDatesSuccess() {
        var now = LocalDateTime.now();
        var then = LocalDateTime.of(2000, 1, 1, 0, 0);
        List<ThreadReference> response = restTemplate.getForObject(
                BASE_URL + BEFORE + now + AFTER + then,
                List.class
        );
        // assert
        assertNotNull(response);
    }

    @Test
    void getThreadsBetweenDatesFail_FirstDateMissing() {
        var then = LocalDateTime.of(2000, 1, 1, 0, 0);
        ResponseEntity<List<ThreadReference>> response = restTemplate.getForEntity(
                BASE_URL + BEFORE + AFTER + then,
                null,
                new ParameterizedTypeReference<List<ThreadReference>>(){}
        );
        // assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getThreadsBetweenDatesFail_SecondDateMissing() {
        var now = LocalDateTime.now();
        ResponseEntity<List<ThreadReference>> response = restTemplate.getForEntity(
                BASE_URL + BEFORE + now + AFTER,
                null,
                new ParameterizedTypeReference<List<ThreadReference>>(){}
        );
        // assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getThreadsBetweenDatesFail_BothDatesMissing() {
        ResponseEntity<List<ThreadReference>> response = restTemplate.getForEntity(
                BASE_URL + BEFORE + AFTER,
                null,
                new ParameterizedTypeReference<List<ThreadReference>>(){}
        );
        // assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
