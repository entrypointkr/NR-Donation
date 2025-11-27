package net.teujaem.nrDonation.common.handler.donation.chzzk;

import net.teujaem.nrDonation.common.MainAPI;
import net.teujaem.nrDonation.common.util.UrlEncoding;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class ChzzkAddEvent {

    private static final String BASE_URL = "https://openapi.chzzk.naver.com/open/v1/sessions/events/subscribe/";

    private static final String accessToken = MainAPI.getInstance().getDataClassManager().getAccessToken().getChzzk();

    public ChzzkAddEvent(String sessionKey) throws IOException, InterruptedException {
        addChatEvent(sessionKey);
        addDonationEvent(sessionKey);
    }

    private void addChatEvent(String sessionKey) throws IOException, InterruptedException {
        // body setting
        Map<String, String> map = Map.of(
                "sessionKey", sessionKey
        );

        String body = UrlEncoding.toXWwwFormUrl(map);

        // POST chat
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "chat"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept", "*/*")
                .header("Authorization", "Bearer " + accessToken)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());

    }

    private void addDonationEvent(String sessionKey) throws IOException, InterruptedException {
        // body setting
        Map<String, String> map = Map.of(
                "sessionKey", sessionKey
        );

        String body = UrlEncoding.toXWwwFormUrl(map);

        // POST donation
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "donation"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept", "*/*")
                .header("Authorization", "Bearer " + accessToken)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());

    }

}
