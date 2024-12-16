package com.bhoomi.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UserProfileServlet extends HttpServlet{
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try
		{
		resp.setContentType("text/html");
		
		PrintWriter out=resp.getWriter();
		
		HttpSession session=req.getSession(false);
		
		if( session == null )
		{
			out.println("<h1> User Not Login, first you login and check for profile </h1>");
			
			RequestDispatcher requestDispatcher = req.getRequestDispatcher("index.html");
			
			requestDispatcher.include(req, resp);
		}
		
		    String userId = (String)session.getAttribute("uid");
			
			ServletContext servletContext = getServletContext();
			
			Connection  connection = (Connection)servletContext.getAttribute("dbconnection");
			
			PreparedStatement prepareStatement = connection.prepareStatement("select  USER_FIRSTNAME,  USER_LASTNAME,  USER_GENDER, USER_AGE  from   webusers where user_id = ?");
			
			prepareStatement.setString(1, userId);
			
			
			ResultSet resultSet = prepareStatement.executeQuery();
			
			resultSet.next();
			
			out.println("<h1>");
			
			out.println( resultSet.getString(1) );
			
			out.println( resultSet.getString(2) );
			
			out.println( resultSet.getString(3) );
			
			out.println( resultSet.getInt(4) );
			
			out.println("</h1>");
			
			out.println("<a href=\"logout\"> Logout </a>");
	
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}

}
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doPost(request, response);
	}


}
