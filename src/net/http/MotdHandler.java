package net.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.Main;
import net.ServerHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class MotdHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, Object> parameters = new HashMap<>();
        URI requestedUri = exchange.getRequestURI();
        String query = requestedUri.getRawQuery();
        ServerHandler.parseHttpQuery(query, parameters);

        String response = "motd";

        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());

        os.close();
    }

}
