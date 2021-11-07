package net.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import engine.game.Player;
import main.Main;
import net.ServerHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class RootHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, Object> parameters = new HashMap<>();
        URI requestedUri = exchange.getRequestURI();
        String query = requestedUri.getRawQuery();
        ServerHandler.parseHttpQuery(query, parameters);

        String name = (String) ServerHandler.getServerInfo().get("name");
        String response = "<!DOCTYPE html> \n<head> \n<title>" + name + "</title> \n</head> \n<body>";

        response += "<h2>" + "Server: " + name + "</h2>" + '\n';
        response += "<h4>" + "Host: " + ServerHandler.getServerInfo().get("host") + "</h4>" + '\n';
        response += "<h4>" + "Description: " + ServerHandler.getServerInfo().get("description") + "</h4>" + '\n';
        response += "<h4>" + "Players Online: " + ServerHandler.getPlayerList().size() + "</h4>" + '\n';

        for (Player player : ServerHandler.getPlayerList()) {
            response += "<p>" + player.getUsername() + "<p> \n";
        }

        response += "</body>";

        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());

        os.close();
    }

}
