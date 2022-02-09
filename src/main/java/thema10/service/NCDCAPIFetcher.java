package thema10.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import thema10.model.Station;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NCDCAPIFetcher {

    /**
     * Given a NCDC API URL pointing to occurrences, this method stores several fields
     * from the 'results' array.
     * @param RESTAPI API URL for performing the request
     * @return List containing Station instances with the mapped JSON data
     * @throws IOException
     */
    public List<Station> requestJSONData(String RESTAPI) throws IOException {
        URL APIUrl = new URL(RESTAPI);
        // Open a connection given a URL
        HttpURLConnection con = (HttpURLConnection) APIUrl.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        // FIXME: add your own NCDC token
        con.setRequestProperty("token", "");

        // List storing all stations
        List<Station> stations = new ArrayList<>();

        // Check if request was successful (status 200)
        int status = con.getResponseCode();
        if (status == 200) {
            // Read and parse the response to JSON
            String response = readJSONData(con.getInputStream());
            JsonObject results = JsonParser.parseString(response).getAsJsonObject();

            // Deserialize the JSON string to Java object(s)
            Gson gson = new Gson();
            // Define the type of object to convert to, an ArrayList<Station> in this case
            Type listType = new TypeToken<ArrayList<Station>>(){}.getType();
            stations = gson.fromJson(results.get("results"), listType);

            con.disconnect();
        } else {
            System.out.println("Received status code " + status + "!!");
            System.exit(1);
        }

        return stations;
    }

    /**
     * Store the response of an API request as a single String
     * @param response The InputStream from a HttpURLConnection
     * @return String containing the complete response
     * @throws IOException
     */
    private String readJSONData(InputStream response) throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(response));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        return content.toString();
    }

    /**
     * Converts a List of Stations back into JSON as a String
     * @param stations a List of one or more Stations
     * @return String containing a JSON representation of the input
     */
    public String summarizedJSON(List<Station> stations) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                // This will include any null-values in the JSON output
                .serializeNulls()
                .create();
        String jsonString = gson.toJson(stations);
        return jsonString;
    }

    /**
     * Writes a JSON String to a given file
     * @param JSON Input JSON String
     * @param filePath Input file path
     */
    public void jsonWriter(String JSON, String filePath) {
        try(FileWriter writer = new FileWriter(filePath)) {
            writer.write(JSON);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        NCDCAPIFetcher fetcher = new NCDCAPIFetcher();

        // Total stations available: 143927;
        int offset = 0;     // Start at
        int limit = 1000;   // Download size per request
        int total = 2000;  // Stop at
        int nrequests = (int)Math.ceil(total / limit);
        System.out.println("Performing " + nrequests + " requests..");
        List<Station> stations = new ArrayList<>();
        for (int i = 0; i < nrequests; i++) {
            String url = "https://www.ncdc.noaa.gov/cdo-web/api/v2/stations?limit=" +
                    limit + "&offset=" + (offset + (i * limit));

            List<Station> results = fetcher.requestJSONData(url);

            // Pause for 1 second
            Thread.sleep(5000);
            // Progress
            System.out.println("\t" + ((i+1) * limit) * 100.0 / (limit * nrequests) + "% ...");
        }
        // Print result
        System.out.println("A total of " + stations.size() + " stations have been stored:");
        //System.out.println(stations.toString());
        System.out.println("-----------------");

        // Convert the observations to JSON
        String summarizedStations = fetcher.summarizedJSON(stations);

        // Write the data to a JSON file
        String strTmp = System.getProperty("java.io.tmpdir");
        String filePath = strTmp + File.pathSeparator + "stations.json";
        System.out.println("Written " + stations.size() + " records to " + filePath);
        fetcher.jsonWriter(summarizedStations,
                filePath);
    }
}