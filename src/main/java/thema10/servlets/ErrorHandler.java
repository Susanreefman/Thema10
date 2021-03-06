package thema10.servlets;

// Import required java libraries
import thema10.config.WebConfig;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * ErrorHandler
 * Run error pages: /403, /404 & /500 page with different html files as template
 */
@WebServlet(name = "ErrorHandler", urlPatterns = {"/404", "/403", "/500"}, loadOnStartup = 1)
public class ErrorHandler extends HttpServlet {
    @Override
    public void init() throws ServletException {
        System.out.println("Initializing Thymeleaf template engine");
        final ServletContext servletContext = this.getServletContext();
        WebConfig.createTemplateEngine(servletContext);
    }
    private static final long serialVersionUID = 1L;
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
        process(request, response);
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
        process(request, response);
    }
    public void process(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // Analyze the servlet exception
        Throwable throwable = (Throwable)
                request.getAttribute("javax.servlet.error.exception");
        Integer statusCode = (Integer)
                request.getAttribute("javax.servlet.error.status_code");
        String servletName = (String)
                request.getAttribute("javax.servlet.error.servlet_name");

        if (servletName == null) {
            servletName = "Unknown";
        }
        String requestUri = (String)
                request.getAttribute("javax.servlet.error.request_uri");

        if (requestUri == null) {
            requestUri = "Unknown";
        }

        // The status code refers to corresponding error page
        if (statusCode == 404) {
            WebConfig.configureResponse(response);
            WebContext ctx = new WebContext(
                    request,
                    response,
                    request.getServletContext(),
                    request.getLocale());
            ctx.setVariable("currentDate", new Date());
            WebConfig.createTemplateEngine(getServletContext()).
                    process("errors/404", ctx, response.getWriter());
        } else if (statusCode == 403) {
            WebConfig.configureResponse(response);
            WebContext ctx = new WebContext(
                    request,
                    response,
                    request.getServletContext(),
                    request.getLocale());
            ctx.setVariable("currentDate", new Date());
            WebConfig.createTemplateEngine(getServletContext()).
                    process("errors/403", ctx, response.getWriter());
        } else if (statusCode == 500) {
            WebConfig.configureResponse(response);
            WebContext ctx = new WebContext(
                    request,
                    response,
                    request.getServletContext(),
                    request.getLocale());
            ctx.setVariable("currentDate", new Date());
            WebConfig.createTemplateEngine(getServletContext()).
                    process("errors/500", ctx, response.getWriter());
        } else {
            // For other status codes references to corresponding information
            PrintWriter out = response.getWriter();
            String title = "Error/Exception Information";
            String docType =
                    "<!doctype html public \"-//w3c//dtd html 4.0 " +
                            "transitional//en\">\n";

            out.println(docType +
                    "<html>\n" +
                    "<head><title>" + title + "</title></head>\n" +
                    "<body bgcolor = \"#f0f0f0\">\n");

            if (throwable == null && statusCode == null) {
                out.println("<h2>Error information is missing</h2>");
                out.println("Please return to the <a href=\"" +
                        response.encodeURL("http://localhost:8080/") +
                        "\">Home Page</a>.");
            } else if (statusCode != null) {
                out.println("The status code : " + statusCode);
                out.println("Please contact us for any questions regarding this message!");
            } else {
                out.println("<h2>Error information</h2>");
                out.println("Servlet Name : " + servletName + "</br></br>");
                out.println("Exception Type : " + throwable.getClass( ).getName( ) + "</br></br>");
                out.println("The request URI: " + requestUri + "<br><br>");
                out.println("The exception message: " + throwable.getMessage( ));
            }
            out.println("</body>");
            out.println("</html>");
        }
        //this step is optional; standard settings also suffice

    }
}