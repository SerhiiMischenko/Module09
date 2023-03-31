package org.example;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {
        @Override
        protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            ZonedDateTime now;
            String timeZone = req.getParameter ("timezone");
            if(timeZone == null) {
                now = ZonedDateTime.now(ZoneId.of("UTC"));
                resp.setContentType("text/html; charset=utf-8");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
                String formattedDate = now.format(formatter);
                resp.getWriter().write(formattedDate);
                resp.getWriter().close();
            }else {
                Cookie cookie = new Cookie("lastTimezone", timeZone.replace(" ", "+"));
                cookie.setMaxAge(24 * 60 * 60);
                resp.addCookie(cookie);
                Cookie[] cookies = req.getCookies();
                for (int i = 0; i < cookies.length; i++) {
                    now = ZonedDateTime.now(ZoneId.of(cookies[0].getValue()));
                    resp.setContentType("text/html; charset=utf-8");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
                    String formattedDate = now.format(formatter);
                    resp.getWriter().write(formattedDate);
                    resp.getWriter().close();
                }
//                for (Cookie coo : cookies) {
//                    now = ZonedDateTime.now(ZoneId.of(coo.getValue()));
//                    resp.setContentType("text/html; charset=utf-8");
//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
//                    String formattedDate = now.format(formatter);
//                    resp.getWriter().write(formattedDate);
//                    resp.getWriter().close();
//                }
            }
        }
    }

