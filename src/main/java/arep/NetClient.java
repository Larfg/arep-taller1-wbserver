package arep;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class NetClient {
    //title de la forma palabra1+palabra2+palabra3...
    static String title = null;
    static int year = 0;
    static String plot = null;
    static ArrayList<String> plots = new ArrayList<String>(Arrays.asList("Full", "Short"));

    public static void main(String[] args) {
        testValues();
        String urlString = "http://www.omdbapi.com/";
        if (title != null){
            urlString += "?t=" + title;
            if (year!= 0){
                urlString += "&y=" + year;
                if (!Objects.equals(plot, plots.get(0))){
                    urlString += "&plot=" + plot;
                }
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
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void testValues() {
        title = "Die+Hard";
        year = 0;
        plot = plots.get(0);
    }
}
