package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Comment;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class RequestService {
    public final String url = "https://jsonplaceholder.typicode.com/comments";

    public List<Comment> getComments() {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .uri(URI.create(this.url))
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.err.println("Error na req: " + e.getMessage());
            System.exit(1);
        }
        var mapper = new ObjectMapper();
        var body = response.body();
        List<Comment> comments = null;
        try {
            comments = mapper.readValue(body, new TypeReference<List<Comment>>(){});
        } catch (JsonProcessingException e) {
            System.err.println("Error na conversão: " + e.getMessage());
            System.exit(1);
        }
        return comments;
    }
}
