package db;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SupabaseClient {

    private static final HttpClient client = HttpClient.newHttpClient();

    public static void post(String endpoint, String json) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SupabaseConfig.SUPABASE_URL + endpoint))
                    .header("apikey", SupabaseConfig.SUPABASE_API_KEY)
                    .header("Authorization", "Bearer " + SupabaseConfig.SUPABASE_API_KEY)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

