package api;

import com.google.gson.Gson;
import db.Connect;
import model.Cep;
import model.LogEntry;
import model.ResDate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CepLog {
    private static ResDate getApiDate() {
        var url = String.format("https://worldtimeapi.org/api/timezone/America/Sao_Paulo");
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.err.println("Erro buscando data: " + e);
            System.exit(1);
        }
        return new Gson().fromJson(response.body(), ResDate.class);
    }

    public static void salvarLog(Cep cep) {
        var resDate = CepLog.getApiDate();
        var logLine = String.format(
                "Cep: %s;Logradouro: %s; Localidade: %s;Data: %s;IP: %s",
                cep.cep(),
                cep.logradouro(),
                cep.localidade(),
                resDate.datetime(),
                resDate.client_ip());
        // Salva no Arquivo 'log'.
        var logFile = new File("log");
        try {
            if (!logFile.isFile()) {
                logFile.createNewFile();
                logFile.setWritable(true);
            }
            var writer = new BufferedWriter(new FileWriter(logFile));
            writer.write(logLine);
            writer.close();
        } catch (Exception e) {
            System.err.println("Erro salvando no arquivo log: " + e);
            System.exit(1);
        }
        // Salva no banco sqlite.
        var conn = Connect.connect();
        try {
            String sql = "INSERT INTO LOG_ENTRY (cep, logradouro, localidade, datetime, client_ip) VALUES(?, ?, ?, ?, ?)";
            var pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cep.cep());
            pstmt.setString(2, cep.logradouro());
            pstmt.setString(3, cep.localidade());
            pstmt.setString(4, resDate.datetime());
            pstmt.setString(5, resDate.client_ip());
            pstmt.execute();
        } catch (Exception e) {
            System.err.println("Erro salvando no banco: " + e);
            System.exit(1);
        }
    }
}
