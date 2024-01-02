package com.demo.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.demo.model.RequestLoginParams;
import com.demo.model.User;
import com.demo.service.IUserService;
import com.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet(urlPatterns = { "/home", "/login", "/logout" })
public class HomeController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Inject
	private IUserService _userService;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String requestURI = req.getRequestURI();
		HttpSession session = req.getSession(false);
		if (requestURI.endsWith("/home")) {
			if (session == null || session.getAttribute("userInfo") == null) {
				resp.sendRedirect(req.getContextPath() + "/login");
			} else {
				User user = (User) session.getAttribute("userInfo");
				String remmerberMe = (String) session.getAttribute("remmerberMe");
				req.setAttribute("userInfo", user);
				req.setAttribute("remmemberMe", remmerberMe);
				req.getRequestDispatcher("/views/home.jsp").forward(req, resp);
			}
		} else if (requestURI.endsWith("/login")) {
			RequestLoginParams _requestLoginParams = new RequestLoginParams("", "PCMV1", "http://localhost:8080/demo/home");
			ObjectMapper objectMapper = new ObjectMapper();
			String stringParams = objectMapper.writeValueAsString(_requestLoginParams);
			try {
				UserService.decrypt_n();
			} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException
					| NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException
					| BadPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//String text = "{\"Cookie\":null,\"AfterLoginPage\":\"http://google.com\",\"SystemName\":\"PCMV1\"}";
			//String key = "4c454b8a-31d6-11ee-be56-0242ac120002";
		//	String key = "1";
	//String request;
//	try {
//		UserService.encrypt_n(stringParams);
//	} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
//			| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
			//String request2 = _userService.encrypt3(stringParams, key);
			//String url = "https://sso.pungkookvn.com/Account/Index?request=" + request2;resp.sendRedirect(url);
			//System.out.println(request);
			//System.out.println(request2);
			//System.out.println(_userService.decrypt(request, key));
			
			

			// req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
		} else if (requestURI.endsWith("/logout")) {
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
		String userID, roleID, password, remmerberMe;
		String requestURI = req.getRequestURI();
		if (requestURI.endsWith("/login")) {
			userID = req.getParameter("userID");
			roleID = req.getParameter("roleID");
			password = req.getParameter("password");
			remmerberMe = req.getParameter("remmerberMe");
			Optional<User> user;
			try {
				user = _userService.getUserById_Role(userID, roleID, password);
				if (user.isPresent()) {
					User userInfo = new User(user.get().getUserID(), user.get().getRoleID(), user.get().getName());
					req.getSession().setAttribute("userInfo", userInfo);
					req.getSession().setAttribute("remmerberMe", remmerberMe);
					resp.sendRedirect(req.getContextPath() + "/home");
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
