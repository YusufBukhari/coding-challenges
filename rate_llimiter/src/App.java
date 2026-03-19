import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

public class App {

    private static final ConcurrentHashMap<String, RateLimiter> limiters = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/limited", (HttpExchange exchange) -> {
            String ip = exchange.getRemoteAddress().getAddress().getHostAddress();
            RateLimiter window = limiters.computeIfAbsent(ip, k -> new SlidingWindowCounter());

            if  (!window.addRequest()) {
                String response = "Too Many Requests";
                exchange.sendResponseHeaders(429, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
                return;
            }

            String response = "Limited! Don't overuse me!";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        });


        server.createContext("/unlimited", (HttpExchange exchange) -> {
           String response = "Unlimited! let's go!";
           exchange.sendResponseHeaders(200, response.length());
           OutputStream os = exchange.getResponseBody();
           os.write(response.getBytes());
           os.close();
        });

        server.start();
        System.out.println("Server running on port 8080");
    }
}
