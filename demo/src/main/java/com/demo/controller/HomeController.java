package com.demo.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.demo.model.User;

@WebServlet(urlPatterns = { "/home", "/login", "/logout" })
public class HomeController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String requestURI = req.getRequestURI();

        if (requestURI.endsWith("/home")) {
            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute("userInfo") == null) {
                resp.sendRedirect(req.getContextPath() + "/login");
            } else {
            	resp.sendRedirect(req.getContextPath() + "/home");
            }
        } else if (requestURI.endsWith("/login")) {
        	req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
        } else if (requestURI.endsWith("/logout")) {
            HttpSession session = req.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            resp.sendRedirect(req.getContextPath() + "/login");
        } else {
        	resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String userID, roleID,password,remmerberMe;
		String requestURI = req.getRequestURI();
		if (requestURI.endsWith("/login")) {
			userID = req.getParameter("userID");
			roleID = req.getParameter("roleID");
		//	password = req.getParameter("psw");
		//	remmerberMe = req.getParameter("remmerberMe");
			User userInfo = new User(userID, roleID);
		//	req.getSession().setAttribute("password", "1");
		//	req.getSession().setAttribute("remmerberMe", remmerberMe);
			req.getSession().setAttribute("userInfo", userInfo);
		//	req.getRequestDispatcher("/views/home.jsp").forward(req, resp);
		//	resp.sendRedirect(req.getContextPath() + "/home");
			req.getRequestDispatcher("/views/home.jsp").forward(req, resp);
		
		}
	}
}
