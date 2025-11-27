package net.teujaem.nrDonation.common.handler.donation.soop;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class SoopCreateCode {

    private static final String BASE_URL = "https://openapi.sooplive.co.kr/auth/code";

    private static String clientId;

    public SoopCreateCode(String clientId) {
        SoopCreateCode.clientId = clientId;
    }

    public URI getLoginUrl() throws IOException, InterruptedException {

        String query = "client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8);

        String url = BASE_URL + "?" + query;

        // create url
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();

        // GET url
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "*/*")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.uri();

    }
}
