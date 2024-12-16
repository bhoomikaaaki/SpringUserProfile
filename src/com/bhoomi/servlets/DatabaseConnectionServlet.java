package com.bhoomi.servlets;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class DatabaseConnectionServlet extends HttpServlet{
	private Connection conn=null;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		
		try {
			ServletContext context=config.getServletContext();
			
			Class.forName(context.getInitParameter("driver"));
			
			conn=DriverManager.getConnection(context.getInitParameter("url"), context.getInitParameter("username"),
					context.getInitParameter("password"));
			
			context.setAttribute("dbconnection", conn);
			
			System.out.println("Application connected to database");
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	@Override
	public void destroy() {

		try
		{
			if( conn!=null)
			{
				conn.close();
			}
			
			System.out.println("Application Stopped");
		
		}
		catch(Exception e)
		{			
			e.printStackTrace();
		}

	}


}
