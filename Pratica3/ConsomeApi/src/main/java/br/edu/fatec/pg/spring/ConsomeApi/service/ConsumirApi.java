package br.edu.fatec.pg.spring.ConsomeApi.service;

import io.github.cdimascio.dotenv.Dotenv;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class ConsumirApi {

    private static String apikey = Dotenv.configure().load().get("API_KEY");

    public static String obterDado(String query) {
        var normalized = URLEncoder.encode(query.toLowerCase(), StandardCharsets.UTF_8);
        var base = "https://www.omdbapi.com/?t=%s&apikey=%s";
        var url = String.format(base, normalized, apikey);
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(5))
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.err.println("Erro buscando filme: " + e);
            System.exit(1);
        }
        if (response.body().contains("Error")) {
            return null;
        }
        return response.body();

    }
}
