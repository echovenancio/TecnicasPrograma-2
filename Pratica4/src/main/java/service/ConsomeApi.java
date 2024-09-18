package service;

import io.github.cdimascio.dotenv.Dotenv;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ConsomeApi {
    private static final String apikey = Dotenv.load().get("API_KEY");
    private static final Pattern RESP_PATTERN = Pattern.compile("\"text\"\\s*:\\s*\"([^\"]+)\"");
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=%s";

    public static String fazerPergunta(String pergunta) {
        String jsonRequest = fazerJsonRequest(pergunta);
        return extrairJson(jsonRequest);
    }

    static String extrairJson(String jsonRequest) {
        var matcher = RESP_PATTERN.matcher(jsonRequest);
        var reslist = matcher.results().map(MatchResult::group).toList();
        var sb = new StringBuilder();
        Arrays.stream(reslist.get(0)
                .split(":")).skip(1)
                .forEach(s -> sb.append(s.trim()));
        sb.deleteCharAt(0).deleteCharAt(sb.length() - 1);
        return sb.toString().translateEscapes();
    }

    static String fazerJsonRequest(String pergunta) {
        var sanatized = pergunta.replaceAll("\"'", "'");
        var url = String.format(API_URL, apikey);
        var client = HttpClient.newHttpClient();
        var template_prompt = "{\"contents\":[{\"parts\":[{\"text\":\"%s\"}]}]}";
        var prompt = String.format(template_prompt, sanatized);
        var req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(prompt))
                .build();
        String body = null;
        try {
            var res = client.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() != 200) {
                return body;
            }
            body = res.body();
        } catch (Exception e) {
            System.err.println("Erro enviado req: " + e);
        }
        return body;
    }
}