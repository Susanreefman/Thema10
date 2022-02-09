package thema10.service;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import thema10.model.Observation;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;

public class GbifAPIFetcher {

    /**
     * Given a GBIF API URL pointing to occurrences, this method stores several fields
     * from the 'results' array.
     * @param RESTAPI API URL for performing the request
     * @return List containing Observation instances with the mapped JSON data
     * @throws IOException
     */
    public List requestJSONData(String RESTAPI) throws IOException {
        URL APIUrl = new URL(RESTAPI);
        // Open a connection given a URL
        HttpURLConnection con = (HttpURLConnection) APIUrl.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");

        // List storing all observations
        List<Observation> observations = new ArrayList<Observation>();

        // Check if request was successful (status 200)
        int status = con.getResponseCode();
        if (status == 200) {
            // Read and parse the response to JSON
            String response = readJSONData(con.getInputStream());
            JsonObject results = JsonParser.parseString(response).getAsJsonObject();

            // Deserialize the JSON string to Java object(s)
            Gson gson = new Gson();
            // Define the type of object to convert to, an ArrayList<Observation> in this case
            Type listType = new TypeToken<ArrayList<Observation>>(){}.getType();
            observations = gson.fromJson(results.get("results"), listType);

            con.disconnect();
        }

        return observations;
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
     * Converts a List of Observations back into JSON as a String
     * @param observations a List of one or more Observations
     * @return String containing a JSON representation of the input
     */
    public String summarizedJSON(List<Observation> observations) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                // This will include any null-values in the JSON output
                .serializeNulls()
                .create();
        String jsonString = gson.toJson(observations);
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
        GbifAPIFetcher fetcher = new GbifAPIFetcher();

        int offset = 2;
        int limit = 20;
        int total = 100;
        int nrequests = (int)Math.ceil(total / limit);
        System.out.println("Fetching " + limit * offset + " occurrences.");
        List<Observation> observations = new ArrayList<>();
        for (int i = 0; i < nrequests; i++) {
            List<Observation> results = fetcher.requestJSONData(
                    "https://api.gbif.org/v1/occurrence/search?taxon_key=7982719&limit=" +
                            limit + "&offset=" +
                            i * offset);
            observations.addAll(results);

            // Pause for 2 seconds
            Thread.sleep(2000);
            // Progress
            System.out.println("\t" + ((i+1) * limit) * 100.0 / (limit * nrequests) + "% ...");
        }
        // Print result
        System.out.println("A total of " + observations.size() + " observations have been stored:");
        System.out.println(observations.toString());
        System.out.println("-----------------");

        // Convert the observations to JSON
        String summarizedObservations = fetcher.summarizedJSON(observations);
        System.out.println("Summarized JSON representation:");
        System.out.println(summarizedObservations);

        // Write the subset to a JSON file
        String strTmp = System.getProperty("java.io.tmpdir");
        String filePath = strTmp + "occurrences.json";
        System.out.println("Written " + observations.size() + " records to " + filePath);
        fetcher.jsonWriter(summarizedObservations,
                filePath);
    }
}
