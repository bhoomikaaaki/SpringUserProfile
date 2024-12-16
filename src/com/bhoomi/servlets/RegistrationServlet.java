package com.bhoomi.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegistrationServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;

	private Connection   connection = null;	
	
	private PreparedStatement preparedStatementSelect = null;
	
	private PreparedStatement preparedStatementInsert = null;
	
	private final static String SQL_INSERT_QUERY = "INSERT INTO WEBUSERS VALUES(?,?,?,?,?,?)";
	
	private final static String  SQL_SELECT_QUERY = "SELECT COUNT(*) FROM WEBUSERS WHERE user_id = ? ";
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		try {
			ServletContext context=config.getServletContext();
			
			connection=(Connection)context.getAttribute("dbconnection");
			
			preparedStatementSelect=connection.prepareStatement(SQL_SELECT_QUERY);
			
			preparedStatementInsert=connection.prepareStatement(SQL_INSERT_QUERY);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
		
		
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try
		{
			
			response.setContentType("text/html");
			
			PrintWriter out = response.getWriter();
			
			String firstName = request.getParameter("fname");
			
			String lastName = request.getParameter("lname");
			
			String userId = request.getParameter("preferredid");
			
			String userPrePassword = request.getParameter("ppasswd");
			
			String userRetypedPassword = request.getParameter("rtpasswd");
			
			int userAge = Integer.parseInt( request.getParameter("age") );
			
			String userGender = request.getParameter("gender");
			
			if( ! userPrePassword.equals(userRetypedPassword) )
			{
				out.println("<h1> passwords not matching </h1>");
				
				return;
				
			}
			preparedStatementSelect.setString(1,userId);
			
			ResultSet rs=preparedStatementSelect.executeQuery();
			
			rs.next();
			
			int count = rs.getInt(1);
			
			if( count >= 1 )
			{
				out.println("<h1> User Already Exist </h1>");
				
				return;
			}
			
			preparedStatementInsert.setString(1, userId);
			
			preparedStatementInsert.setString(2, userPrePassword);
			
			preparedStatementInsert.setString(3, firstName);
			
			preparedStatementInsert.setString(4, lastName);
			
			preparedStatementInsert.setString(5, userGender);
			
			preparedStatementInsert.setInt(6, userAge);
			
			int cnt = preparedStatementInsert.executeUpdate();
			
			if( cnt == 1)
			{
				out.println("<h1> User Registered Successfully </h1>");
				
				RequestDispatcher requestDispatcher=request.getRequestDispatcher("index.html");
				
				requestDispatcher.include(request, response);
			}

			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}


	}
	@Override
	public void destroy() {

		try
		{
			
			if(preparedStatementSelect!=null)
			{
				preparedStatementSelect.close();
			}
			

			if( preparedStatementInsert!=null){
				
				preparedStatementInsert.close();
			}
			
			
			if( connection!=null)
			{
				connection.close();
			}
			
			System.out.println("Application Stopped");
		
		}
		catch(Exception e)
		{			
			e.printStackTrace();
		}

	}



}
