package edu.escuelaing.app;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
/**
 * Clase que nos permite comunicarnos con un api y realizar una petición
 * @author Luis Giraldo
 * @version 1.0
 */
public class NetClient {
    // title de la forma palabra1+palabra2+palabra3...
    static String title = null;
    static int year = 0;
    static String plot = null;
    static ArrayList<String> plots = new ArrayList<String>(Arrays.asList("Full", "Short"));
    static String pelicula = "";

    /**
     * Metodo que a partir de un titulo realiza una consulta de un api determinada
     * @param titulo
     * @return String con la información de la pelicula con el titulo
     */
    public String consultApi(String titulo) {
        title = titulo;
        String urlString = "http://www.omdbapi.com/";
        if (title != null) {
            urlString += "?t=" + title;
            if (year != 0) {
                urlString += "&y=" + year;
            }
            if (!Objects.equals(plot, plots.get(0))) {
                urlString += "&plot=" + plot;
            }

        }
        urlString += "&apikey=fd52b2b3";
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            String output;
            String pelicula = "";
            while ((output = br.readLine()) != null) {
                pelicula+=output;
            }
            conn.disconnect();
            return pelicula;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "error";
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }

    }

    /**
     * Metodo memorizador que nos permite mantener un cache de los elementos consultados y agilizar el proceso
     * de consulta por medio de un mapa concurrente
     * @param titulo
     * @param cache
     * @return String con la información de la pelicula con el titulo
     */
    public String consultaApiMemo(String titulo,Map cache){
        if (cache.keySet().contains(titulo)){
            return (String) cache.get(titulo);
        }
        else{
            String consulta = consultApi(titulo);
            cache.put(titulo, consulta);
            return consulta;
        }
    }


    public static void setTitulo(String titulo) {
        title = titulo;
    }

}
