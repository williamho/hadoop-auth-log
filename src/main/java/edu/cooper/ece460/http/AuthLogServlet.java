package edu.cooper.ece460.http;

import java.io.FileNotFoundException;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

// Should probably be in a jsp
public class AuthLogServlet extends HttpServlet {
    PrintWriter out;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        resp.setContentType("text/html");
        resp.setStatus(HttpServletResponse.SC_OK);
        String user = req.getParameter("user");
        String node = req.getParameter("node");
        String dataFile = req.getParameter("datafile");

        out = resp.getWriter();
        out.println("<link rel='stylesheet' href='style.css' type='text/css'>");
        out.println("<h1>Analyse logs</h1>");

        req.getRequestDispatcher("/authlog_analyse.html").include(req, resp); 

        if ((user == null && node == null) || dataFile == null)
            return;

        // Open file and try to find patent with that number
        out.println("<div style='float:left; padding-left:10px;'>");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(dataFile));
        }
        catch (FileNotFoundException e) {
            out.println("File <tt>" + dataFile + "</tt> not found");
            return;
        }

        ArrayList<String> outLines = new ArrayList<String>();
        String line;
        while ((line = br.readLine()) != null){
            if(line.matches(user + "," + node + "(.*)"))
                outLines.add(line);
        }
        br.close();

        // Patent not found
        out.println("User:" + user);
        out.println("Node:" + node);
        
        if (outLines.size() == 0) {
            out.println("no results found <tt>" + dataFile + "</tt>");
            return;
        }

        out.println("<table><tr><th>User</th><th>Node</th><th>Local logins</th><th>SSH logins</th><th>Failed logins</th></tr>");
        for(String foundLine : outLines){
            String[] parts = foundLine.split("\\s+",2); // Split by whitespace
            String[] parts2 = parts[0].split(",");
            String[] parts3 = parts[1].split(",");
            out.println("<tr>");
            out.print("<td><a href='?datafile=" + dataFile + "&user=" + parts2[0] + "&node=(.*)" + "'>" + parts2[0] + "</a></td>");
            out.print("<td><a href='?datafile=" + dataFile + "&user=(.*)" + "&node=" + parts2[1] + "'>" + parts2[1] + "</a></td>");
            for (String s : parts3)
                out.print("<td class='center'>" + s + "</td>");
            out.println("</tr>");
        }
        out.println("</div>");
    }
}

