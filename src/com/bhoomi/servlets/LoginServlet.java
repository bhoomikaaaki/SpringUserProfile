package com.bhoomi.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;

	private Connection   connection = null;	
	
	private PreparedStatement preparedStatementSelect = null;
	
	private final static String  SQL_SELECT_QUERY = "SELECT COUNT(*) FROM WEBUSERS WHERE user_id = ? and user_pwd = ? ";
	
	
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		
		try
		{
			ServletContext context=config.getServletContext();
			
			connection=(Connection)context.getAttribute("dbconnection");
			
			preparedStatementSelect = connection.prepareStatement(SQL_SELECT_QUERY);
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		try
		{	
			response.setContentType("text/html");
			
			PrintWriter out = response.getWriter();
			
			String userId = request.getParameter("uid");
			
			String usePassword = request.getParameter("pwd");
			
			
			preparedStatementSelect.setString(1, userId );
			
			preparedStatementSelect.setString(2, usePassword );
			
			ResultSet resultSet = preparedStatementSelect.executeQuery();
			
			resultSet.next();
			
			int count = resultSet.getInt(1);
			
			if( count >= 1 )
			{
				out.println("<h1> User Successfully Login </h1>");
				
				HttpSession session = request.getSession();
				
				session.setAttribute("uid", userId );
				
				RequestDispatcher requestDispatcher=request.getRequestDispatcher("Home.html");
				
				requestDispatcher.include(request, response);
				
			}
			else
			{
				out.println("<h1> Invalid Login </h1>");
				
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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


		doPost(request, response);
	}
	
	@Override
	public void destroy() {

		try
		{
			
			if(preparedStatementSelect!=null)
			{
				preparedStatementSelect.close();
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
