package com.example.springdummyapp.service;




import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@RequiredArgsConstructor
public class EventStreamService {
    public String bearerToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0b2stZjdjZGU3NDEtYjhlYS00MDUxLTk2MzEtZjNiMGU5NWY2MjU2IiwiaWF0IjoxNzg5NDYwNDE1LCJhY2NvdW50SWQiOiJ0cmFpbmluZ21pY2hhZWxydW1sZXktUFJXVklNIiwiZW52aXJvbm1lbnRJZCI6IjI0OGY2YmM2LTMwZjgtNDNhZS04NDYxLTdmZjdlMjkzZGE0NCIsInNlcnZpY2VVcmwiOiJwdWxzYXIrc3NsOi8vZ2JyLmV2ZW50c3RyZWFtcy5ib29taS5jb206NjY1MSJ9.Nn8MqxMW7ICCCBtMPe03LoNANiWQG4pP0WlhmapKJac";
    HttpClient client = HttpClient.newHttpClient();
    public void SendMessageToTopic(String toSave){
        String topicURL = "https://gbr-web.eventstreams.boomi.com/rest/singlemsg/trainingmichaelrumley-PRWVIM/248f6bc6-30f8-43ae-8461-7ff7e293da44/ThreadCreated";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(topicURL))
                .header("Content-Type","application/json")
                .header("Authorization","Bearer "+bearerToken)
                .POST(HttpRequest.BodyPublishers.ofString(toSave))
                .build();
        try{
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode()!=200){
                //Handle error in response
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Post Request failed");
        }

    }
    public String GetMessageFromSubscription(){
        String subscriptionURL = "https://gbr-web.eventstreams.boomi.com/rest/consume/trainingmichaelrumley-PRWVIM/248f6bc6-30f8-43ae-8461-7ff7e293da44/ThreadCreated/ThreadCreated_Subscription";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(subscriptionURL))
                .header("Content-Type","application/json")
                .header("Authorization","Bearer "+bearerToken)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();


        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Get Request Failed");
        }
    }
}
