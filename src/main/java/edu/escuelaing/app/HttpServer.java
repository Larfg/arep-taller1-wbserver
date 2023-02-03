package edu.escuelaing.app;

import java.net.*;
import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * @author Luis Felipe Giraldo Rodriguez
 * @version 1.0
 */
public class HttpServer {

    public static void main(String[] args) throws IOException {
        System.out.println("Servidor funcionando ...");
        Map<String, String> cache = new ConcurrentHashMap<String, String>();
        String lastLine = "";
        ServerSocket serverSocket = null;
        NetClient netClient=new NetClient();
        try {
            serverSocket = new ServerSocket(35000);

        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        boolean running = true;
        while (running) {
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            String inputLine, outputLine;
            while ((inputLine = in.readLine()) != null) {
                if(inputLine.contains("titulo")){
                    lastLine = inputLine.split(":")[1].replace(" ","+");
                }
                // Aqui se recibe el json de la consulta y se hace la consulta
                if (!in.ready()) {
                    break;
                }
            }
            // El output line debería ser el JSON de la respuesta de la API
            outputLine = "HTTP/1.1 200 OK\r\n"
                    + "Access-Control-Allow-Origin: *\r\n"
                    + "Content-Type:application/json\r\n"
                    + "\r\n"
                    // Content type para json
                    + netClient.consultaApiMemo(lastLine,cache);
            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
        System.out.println("Servidor apagado.");
    }
}