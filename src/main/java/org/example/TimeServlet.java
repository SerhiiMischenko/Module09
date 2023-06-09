package org.example;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {
    private TemplateEngine engine;

    @Override
    public void init() {
        engine = new TemplateEngine();
        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix("/Users/serhiimischenko/IdeaProjects/Module08/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        engine.addTemplateResolver(resolver);
    }

    @Override
        protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            resp.setContentType("text/html; charset=utf-8");
            ZonedDateTime now;
            String timeZone = req.getParameter ("timezone");
            Cookie cookie;
            if(timeZone == null) {
                Cookie[] cookies = req.getCookies();
                try {
                    now = ZonedDateTime.now(ZoneId.of(cookies[0].getValue()));
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
                    String formattedDate = now.format(formatter);
                    Context simpleContext = new Context (
                            req.getLocale (),
                            Map. of ("UTC", formattedDate)
);
                    engine.process ("test", simpleContext, resp.getWriter());
                    resp.getWriter().close();

                }catch (NullPointerException e) {
                    now = ZonedDateTime.now(ZoneId.of("UTC"));
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
                    String formattedDate = now.format(formatter);
                    Context simpleContext = new Context (
                            req.getLocale (),
                            Map. of ("UTC", formattedDate)
                    );
                    engine.process ("test", simpleContext, resp.getWriter());
                    resp.getWriter().close();
                }

            }else {
                now = ZonedDateTime.now(ZoneId.of(timeZone.replace(" ", "+")));
                cookie = new Cookie("lastTimezone", timeZone.replace(" ", "+"));
                cookie.setMaxAge(24 * 60 * 60);
                resp.addCookie(cookie);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
                String formattedDate = now.format(formatter);
                Context simpleContext = new Context (
                        req.getLocale (),
                        Map. of ("UTC", formattedDate)
                );
                engine.process ("test", simpleContext, resp.getWriter());
                resp.getWriter().close();
            }
        }
    }

