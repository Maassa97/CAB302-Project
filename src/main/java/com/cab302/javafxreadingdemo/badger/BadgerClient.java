package com.cab302.javafxreadingdemo.badger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Properties;

public final class BadgerClient {
    private final HttpClient http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(8)).build();
    private final ObjectMapper json = new ObjectMapper();
    private final String base;

    public BadgerClient(String baseUrl) {
        this.base = baseUrl;
    }

    public static BadgerClient fromProperties() {
        try (InputStream in = BadgerClient.class.getResourceAsStream("/app.properties")) {
            Properties p = new Properties();
            p.load(in);
            return new BadgerClient(p.getProperty("badger.base", "http://localhost:8080"));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /** Fire-and-forget login event */
    public void sendLoginEvent(String userId) {
        String url  = base + "/api/gamification/events";
        String body = "{\"userId\":\"" + esc(userId) + "\",\"event\":\"login\"}";
        HttpRequest req = HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofSeconds(8))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        http.sendAsync(req, HttpResponse.BodyHandlers.discarding())
                .thenAccept(r -> {
                    if (r.statusCode() / 100 != 2) {
                        System.err.println("Badger event failed: HTTP " + r.statusCode());
                    }
                })
                .exceptionally(ex -> { ex.printStackTrace(); return null; });
    }

    /** Get current streak for a user's badge. Returns -1 if progress isn't available. */
    public int getStreak(String userId, String badgeId) throws Exception {
        // First try: the single-badge route
        String url1 = base + "/api/gamification/users/" + enc(userId) + "/badges/" + enc(badgeId);
        HttpResponse<String> r1 = http.send(
                HttpRequest.newBuilder(URI.create(url1)).timeout(Duration.ofSeconds(8)).GET().build(),
                HttpResponse.BodyHandlers.ofString());
        System.out.println("Streak GET   -> " + url1);
        System.out.println("Streak status-> " + r1.statusCode());
        System.out.println("Streak body  -> " + r1.body());

        int val1 = tryParseStreak(r1.body(), badgeId);
        if (r1.statusCode() / 100 == 2 && val1 >= 0) return val1;

        // Fallback: list of user badges with conditions, filter by badgeId
        String url2 = base + "/api/gamification/users/" + enc(userId) + "/badges?expand=conditions";
        HttpResponse<String> r2 = http.send(
                HttpRequest.newBuilder(URI.create(url2)).timeout(Duration.ofSeconds(8)).GET().build(),
                HttpResponse.BodyHandlers.ofString());
        System.out.println("Streak GET   -> " + url2);
        System.out.println("Streak status-> " + r2.statusCode());
        System.out.println("Streak body  -> " + r2.body());

        int val2 = tryParseStreak(r2.body(), badgeId);
        return (r2.statusCode() / 100 == 2 && val2 >= 0) ? val2 : -1;
    }

    /** Parse either a single-badge object or a badges array; return -1 if no progress present. */
    private int tryParseStreak(String body, String badgeId) {
        try {
            JsonNode root = json.readTree(body);
            JsonNode conds = null;

            if (root.isArray()) {
                for (JsonNode item : root) {
                    if (badgeId.equals(item.path("badgeId").asText())) {
                        conds = item.path("conditions");
                        break;
                    }
                }
            } else {
                conds = root.path("conditions");
            }

            if (conds == null || !conds.isArray()) return -1;

            int streak = 0;
            for (JsonNode c : conds) {
                if ("STREAK".equals(c.path("type").asText())) {
                    if (c.has("value")) streak = c.path("value").asInt(0);
                    else streak = c.path("progress").path("current").asInt(0);
                }
            }
            return streak;
        } catch (Exception e) {
            return -1;
        }
    }

    private static String enc(String s) { return URLEncoder.encode(s, StandardCharsets.UTF_8); }
    private static String esc(String s) { return s.replace("\\", "\\\\").replace("\"", "\\\""); }
}
