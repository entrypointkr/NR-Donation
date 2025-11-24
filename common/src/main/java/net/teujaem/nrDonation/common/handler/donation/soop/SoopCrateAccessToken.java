package net.teujaem.nrDonation.common.handler.donation.soop;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.teujaem.nrDonation.common.util.UrlEncoding;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class SoopCrateAccessToken {

    private static final String BASE_URL = "https://openapi.sooplive.co.kr/auth/token";

    private static String clientId;
    private static String clientSecret;

    public SoopCrateAccessToken(String clientId, String clientSecret) {
        SoopCrateAccessToken.clientId = clientId;
        SoopCrateAccessToken.clientSecret = clientSecret;
    }

    public String getAccessToken(String code) throws Exception {

        // body setting
        Map<String, String> map = Map.of(
                "grant_type", "authorization_code",
                "client_id", clientId,
                "client_secret", clientSecret,
                "redirect_uri", "https://localhost:8080/callback",
                "code", code,
                "refresh_token", "null"
        );

        String body = UrlEncoding.toXWwwFormUrl(map);

        HttpClient client = HttpClient.newHttpClient();

        // GET token
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept", "*/*")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(response.body());

        return rootNode.get("access_token").asText(null);

    }

}
