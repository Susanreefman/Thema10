package thema10.servlets;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import thema10.model.Station;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * This servlet provides a file-download of benchmark results depending on session ID
 */
@WebServlet(urlPatterns = "/stationdownload")
public class WeatherDataServlet extends HttpServlet {

    private static <E> List<E> pickRandom(List<E> list, int n) {
        return new Random().ints(n, 0, list.size()).mapToObj(list::get).collect(Collectors.toList());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int nstations = Integer.parseInt(request.getParameter("nstations"));

        String filename = "/WEB-INF/stations.json";
        List<Station> stations;

        try {
            // create Gson instance
            Gson gson = new Gson();

            ServletContext context = getServletContext();

            // First get the file InputStream using ServletContext.getResourceAsStream()
            // method.
            InputStream is = context.getResourceAsStream(filename);
            InputStreamReader isr = new InputStreamReader(is);
            // create a reader
            BufferedReader reader = new BufferedReader(isr);

            // convert JSON array to list of users
            stations = new Gson().fromJson(reader, new TypeToken<List<Station>>() {}.getType());

            // close reader
            reader.close();

            List<Station> randomStations = pickRandom(stations, nstations);
            String json = new Gson().toJson(randomStations);

            // Return results as JSON
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
