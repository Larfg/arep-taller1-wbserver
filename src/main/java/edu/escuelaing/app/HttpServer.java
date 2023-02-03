package edu.escuelaing.app;

import java.net.*;
import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Servidor ws que nos permite consultar una pelicula por su titulo
 * @author Luis Felipe Giraldo Rodriguez
 * @version 1.0
 */
public class HttpServer {
    /**
     * Metodo principal que nos inicia un servidor socket http, que cuando recibe una peticion consulta el titulo de una pelicula.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        System.out.println("Servidor funcionando ...");
        //Estructura concurrente que nos permite almacenar las peliculas que ya fueron consultadas
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
                if (!in.ready()) {
                    break;
                }
            }
            //Encabezado de la peticion con cors habilitado y content type json
            outputLine = "HTTP/1.1 200 OK\r\n"
                    + "Access-Control-Allow-Origin: *\r\n"
                    + "Content-Type:application/json\r\n"
                    + "\r\n"
                    //Consulta del api del elemento
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