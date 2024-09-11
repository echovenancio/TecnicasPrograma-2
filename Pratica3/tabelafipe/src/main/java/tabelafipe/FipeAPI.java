package tabelafipe;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class FipeAPI {
    public static String doreq(String query) {
        var base = "https://parallelum.com.br/fipe/api/v2/%s";
        var url = String.format(base, query);
        var client = HttpClient.newHttpClient();
        var req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(5)).build();
        HttpResponse<String> res = null;
        try {
            res = client.send(req, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.err.println("Erro ao realizar req: " + e);
            System.exit(1);
        }
        return res.body();
    }
}
