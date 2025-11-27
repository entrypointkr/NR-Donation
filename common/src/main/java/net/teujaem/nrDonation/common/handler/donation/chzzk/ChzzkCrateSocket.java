package net.teujaem.nrDonation.common.handler.donation.chzzk;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ChzzkCrateSocket {

    private static final String BASE_URL = "https://openapi.chzzk.naver.com/open/v1/sessions/auth";

    public String getUrl(String accessToken) throws Exception {

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            String body = response.body();

            // "content" 블록 찾기
            int contentStart = body.indexOf("\"content\"");
            if (contentStart == -1) return null;

            // "content" 이후 첫 '{' 위치 찾기
            int braceStart = body.indexOf("{", contentStart);
            int braceEnd = body.indexOf("}", braceStart);
            if (braceStart == -1 || braceEnd == -1) return null;

            String contentJson = body.substring(braceStart + 1, braceEnd);

            // content 안의 "url" 값 찾기
            int urlKey = contentJson.indexOf("\"url\"");
            if (urlKey == -1) return null;

            int colon = contentJson.indexOf(":", urlKey);
            int quoteStart = contentJson.indexOf("\"", colon + 1);
            int quoteEnd = contentJson.indexOf("\"", quoteStart + 1);
            if (colon == -1 || quoteStart == -1 || quoteEnd == -1) return null;

            return contentJson.substring(quoteStart + 1, quoteEnd);
        }
        return null;

    }

}
