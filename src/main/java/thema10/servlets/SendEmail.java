package thema10.servlets;


import thema10.config.WebConfig;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * SendEmail
 * Run /send page with email.html
 * Returns when contactform is filled in.
 */
@WebServlet(name="SendEmail", urlPatterns = "/send")
public class SendEmail extends HttpServlet {

    public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String from = request.getParameter("email");
        String to= "marcelsetz@hotmail.com";
        String subject=request.getParameter("subject");
        String msg=request.getParameter("message");

        System.out.println(from);
        System.out.println(subject);
        System.out.println(msg);

        thema10.servlets.MailApp.send(from, to, subject, msg);
        System.out.println("Mail sent!");
        out.print("message has been sent successfully");
        out.close();
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
                process("email", ctx, response.getWriter());
    }
}