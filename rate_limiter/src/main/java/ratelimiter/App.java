package ratelimiter;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class App {

    private static final RateLimiter limiter = new RedisRateLimiter();

    public static void main(String[] args) throws IOException {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/limited", (HttpExchange exchange) -> {
            String ip = exchange.getRemoteAddress().getAddress().getHostAddress();

            if (!limiter.addRequest(ip)) {
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
        System.out.println("Server running on port " + port);
    }
}
