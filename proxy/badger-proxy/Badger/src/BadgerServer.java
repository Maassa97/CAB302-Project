
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class BadgerServer {
    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
        String appId     = System.getenv("BADGER_APP_ID");
        String appSecret = System.getenv("BADGER_APP_SECRET");
        if (appId == null || appSecret == null) {
            System.err.println("ERROR: BADGER_APP_ID and BADGER_APP_SECRET must be set");
            return;
        }

        HttpClient http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // Simple health
        server.createContext("/health", ex -> respond(ex, 200, "ok", "text/plain"));

        // POST /api/gamification/events  -> forward event to Badger
        server.createContext("/api/gamification/events", ex -> {
            if (!"POST".equalsIgnoreCase(ex.getRequestMethod())) { respond(ex, 405, "", null); return; }
            byte[] body = ex.getRequestBody().readAllBytes();

            String upstream = "https://api.usebadger.dev/v1/" + appId + "/events";
            HttpRequest req = HttpRequest.newBuilder(URI.create(upstream))
                    .timeout(Duration.ofSeconds(10))
                    .header("Authorization", "Bearer " + appSecret)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofByteArray(body))
                    .build();
            try {
                HttpResponse<byte[]> r = http.send(req, HttpResponse.BodyHandlers.ofByteArray());
                copyResponse(ex, r);
            } catch (Exception e) {
                e.printStackTrace();
                respond(ex, 502, "", null);
            }
        });

        // GET /api/gamification/users/{userId}/badges/{badgeId}
        // Tries 3 upstreams and returns the first one that includes "conditions".
        // Falls back to returning /users/{id}/badges?expand=conditions (array) so the client can filter.
        server.createContext("/api/gamification/users", ex -> {
            if (!"GET".equalsIgnoreCase(ex.getRequestMethod())) { respond(ex, 405, "", null); return; }

            String[] p = ex.getRequestURI().getPath().split("/");
            // expected: /api/gamification/users/<userId>/badges/<badgeId>
            if (p.length != 7 || !"badges".equals(p[5])) { respond(ex, 400, "", null); return; }

            String userId  = p[4];
            String badgeId = p[6];

            String base = "https://api.usebadger.dev/v1/" + appId;

            String u1 = base + "/users/" + enc(userId) + "/badges/" + enc(badgeId) + "?expand=conditions";
            String u2 = base + "/badges/" + enc(badgeId) + "/users/" + enc(userId) + "?expand=conditions";
            String u3 = base + "/users/" + enc(userId) + "/badges?expand=conditions";

            try {
                HttpResponse<byte[]> r1 = http.send(
                        HttpRequest.newBuilder(URI.create(u1))
                                .header("Authorization", "Bearer " + appSecret).GET().build(),
                        HttpResponse.BodyHandlers.ofByteArray());
                String b1 = new String(r1.body(), StandardCharsets.UTF_8);
                if (r1.statusCode() / 100 == 2 && b1.contains("\"conditions\"")) { copyResponse(ex, r1); return; }

                HttpResponse<byte[]> r2 = http.send(
                        HttpRequest.newBuilder(URI.create(u2))
                                .header("Authorization", "Bearer " + appSecret).GET().build(),
                        HttpResponse.BodyHandlers.ofByteArray());
                String b2 = new String(r2.body(), StandardCharsets.UTF_8);
                if (r2.statusCode() / 100 == 2 && b2.contains("\"conditions\"")) { copyResponse(ex, r2); return; }

                HttpResponse<byte[]> r3 = http.send(
                        HttpRequest.newBuilder(URI.create(u3))
                                .header("Authorization", "Bearer " + appSecret).GET().build(),
                        HttpResponse.BodyHandlers.ofByteArray());
                // r3 may be an array; return as-is and let the client filter for badgeId
                copyResponse(ex, r3);
            } catch (Exception e) {
                e.printStackTrace();
                respond(ex, 502, "", null);
            }
        });

        server.start();
        System.out.println("Badger proxy listening on http://localhost:" + port);
    }

    private static String enc(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }

    private static void copyResponse(HttpExchange ex, HttpResponse<byte[]> r) throws IOException {
        ex.getResponseHeaders().add("Content-Type", "application/json");
        ex.sendResponseHeaders(r.statusCode(), r.body().length);
        try (OutputStream os = ex.getResponseBody()) { os.write(r.body()); }
        ex.close();
    }

    private static void respond(HttpExchange ex, int status, String body, String contentType) throws IOException {
        if (contentType != null) ex.getResponseHeaders().add("Content-Type", contentType);
        byte[] b = body.getBytes(StandardCharsets.UTF_8);
        ex.sendResponseHeaders(status, b.length);
        try (OutputStream os = ex.getResponseBody()) { os.write(b); }
        ex.close();
    }
}
