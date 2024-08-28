package api;

import com.google.gson.Gson;
import model.Cep;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CepApi {
    private static Cep getCEP(String cep) {
        var url = String.format("https://viacep.com.br/ws/%s/json/", cep);
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.err.println("Erro buscando cep: " + e);
            System.exit(1);
        }
        return new Gson().fromJson(response.body(), Cep.class);
    }

    public static Cep buscarCep(String cep) {
        var resCep = CepApi.getCEP(cep);
        CepLog.salvarLog(resCep);
        return resCep;
    }

}
