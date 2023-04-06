package com.DistributedSystems.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.DistributedSystems.model.Skiers;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@WebServlet(name = "PostServlet", urlPatterns = {"/skiers"})
public class ResourceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private ConcurrentHashMap<String, Skiers> skiersMap = new ConcurrentHashMap<>();
    private int skierIdCounter = 1;
    private Gson gson = new Gson();
    
    
    
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            String skierId = request.getParameter("skierId");
            if (skierId == null) {
                // if no skierId parameter provided, return all skiers
                response.setContentType("application/json");
                PrintWriter writer = response.getWriter();
                writer.write(gson.toJson(skiersMap));
                writer.close();
            } else {
                // if skierId parameter provided, return the skier with that id
                Skiers skier = skiersMap.get(skierId);
                if (skier == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    PrintWriter writer = response.getWriter();
                    writer.write("Skier with ID " + skierId + " not found");
                    writer.close();
                } else {
                    response.setContentType("application/json");
                    PrintWriter writer = response.getWriter();
                    writer.write(gson.toJson(skier));
                    writer.close();
                }
            }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }
        String json = jsonBuilder.toString();
        Skiers skiers = gson.fromJson(json, Skiers.class);
        String skierId = Integer.toString(skierIdCounter++);
        skiers.setskierID(skierId);
        skiersMap.put(skierId, skiers);
        response.setStatus(HttpServletResponse.SC_CREATED);
        PrintWriter writer = response.getWriter();
        writer.write("Skier with ID " + skierId + " created successfully");
        writer.close();
    }
}


