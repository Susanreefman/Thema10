package thema10.servlets;

import thema10.config.WebConfig;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * ContactServlet
 * Run /contact page with contact.html
 */
@WebServlet(name = "ContactServlet", urlPatterns = "/contact", loadOnStartup = 1)
public class ContactServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        System.out.println("Initializing Thymeleaf template engine");
        final ServletContext servletContext = this.getServletContext();
        WebConfig.createTemplateEngine(servletContext);
    }
    private static final long serialVersionUID = 1L;
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        // read form fields

        process(request, response);
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String email = request.getParameter("email");
        String message = request.getParameter("message");

        process(request, response);
    }
    public void process(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        //this step is optional; standard settings also suffice
        WebConfig.configureResponse(response);
        WebContext ctx = new WebContext(
                request,
                response,
                request.getServletContext(),
                request.getLocale());
        ctx.setVariable("currentDate", new Date());
        WebConfig.createTemplateEngine(getServletContext()).
                process("contact", ctx, response.getWriter());
    }
}
