package org.example;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(value = "/time")
public class TimezoneValidateFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String timeZone = req.getParameter ("timezone");
        if(timeZone == null) {
            chain.doFilter(req, res);
        }
        Integer timeZoneCode = Integer.parseInt(timeZone.replaceAll("[^0-9]", ""));
        if(timeZoneCode < -12 || timeZoneCode > 14) {
            res.setContentType("text/html; charset=utf-8");
            res.getWriter().write("Invalid timezone HTTP status 400.");
            res.getWriter().close();
        }else {
            chain.doFilter(req, res);
        }
    }
}
