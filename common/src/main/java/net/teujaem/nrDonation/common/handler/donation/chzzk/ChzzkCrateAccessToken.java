package net.teujaem.nrDonation.common.handler.donation.chzzk;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.teujaem.nrDonation.common.util.UrlEncoding;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class ChzzkCrateAccessToken {

    private static final String BASE_URL = "https://openapi.chzzk.naver.com/auth/v1/token";

    private final String clientId;
    private final String clientSecret;

    public ChzzkCrateAccessToken(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public String getAccessToken(String code, String state) throws Exception {

        // body setting
        Map<String, String> map = Map.of(
                "grantType", "authorization_code",
                "clientId", clientId,
                "clientSecret", clientSecret,
                "code", code,
                "state", state
        );

        String body = UrlEncoding.toJson(map);

        // GET tokens
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response.body());
            JsonNode content = json.path("content");

            return content.path("accessToken").asText(null);

        }

        return null;

    }
}