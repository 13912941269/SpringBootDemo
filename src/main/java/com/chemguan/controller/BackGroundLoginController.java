package com.chemguan.controller;

import com.chemguan.model.BackGroundLogin;
import com.chemguan.service.BackGroundLoginService;
import com.chemguan.util.YZM;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("manager")
public class BackGroundLoginController {

	@Value("${page.size:2}")
	private Integer pageSize;

	@Autowired
	private BackGroundLoginService service;

	@Autowired
	private YZM yzm;

	/**
	 * 查询
	 */
	@RequestMapping("allbguser")
	public String selectAllUser(ModelMap map, Integer pageNum) {
		PageInfo page = service.listAllBgUserByPage(pageNum, pageSize);
		map.put("pagebean",page);
		return "manager/selectallbguser";
	}


	/**
	 * 删除
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping("deletebguser")
	public void deleteUserById(Integer id,HttpServletResponse response) throws IOException {
		service.deleteBgUserById(id);
		response.sendRedirect("allbguser");
	}





	/**
	 * 点击添加按钮访问对应jsp页面
	 *
	 * @return
	 */
	@RequestMapping("insertbguserjsp")
	public String insertBgUserJsp() {
		return "manager/insertbguser";
	}


	/**
	 * 注册
	 *
	 * @param username
	 * @param password
	 * @param username
	 * @param password
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("insertbguser")
	public void insertBgUser(HttpServletResponse response, String username, String password) throws IOException {
		BackGroundLogin bg=new BackGroundLogin();
		bg.setUsername(username);
		bg.setPassword(password);
		service.insertBgUser(bg);
		response.sendRedirect("allbguser");
	}






	/**
	 * 检查用户名
	 *
	 * @param username
	 * @return
	 */
	@RequestMapping("checkbgusername")
	public ResponseEntity<?> checkBgUserName(String username) {
		BackGroundLogin user = service.checkbgusername(username);
		String json = null;
		if (user != null) {
			json = "nook";
		} else {
			json = "ok";
		}
		return ResponseEntity.ok(json);
	}





	/**
	 * 查询出需要修改的数据
	 *
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping("selectbgbyid")
	public String selectBgById(int id, HttpServletRequest request, ModelMap map) {
		BackGroundLogin backGroundLogin = service.selectBgUserById(id);
		map.put("backGroundLogin", backGroundLogin);
		return "manager/updatebguser";
	}







	/**
	 * 根据id检查用户名
	 *
	 * @param username
	 * @param id
	 * @return
	 */
	@RequestMapping("checkusernameandid")
	public ResponseEntity<?> checkUserNameAndId(String username, int id) {
		BackGroundLogin userone = service.selectBgUserById(id);
		String json = null;
		if (userone.getUsername().equals(username)) {
			json = "ok";
		} else {
			BackGroundLogin user = service.checkbgusername(username);
			if (user != null) {
				json = "nook";
			} else {
				json = "ok";
			}
		}
		return ResponseEntity.ok(json);
	}


	/**
	 * 修改
	 *
	 * @return
	 */
	@RequestMapping("updatebguser")
	public void updateBgUser(HttpServletResponse response, BackGroundLogin bg) throws IOException {
		service.updateBgUser(bg);
		response.sendRedirect("allbguser");
	}





	/**
	 * 返回登录页面
	 *
	 * @return
	 */
	@RequestMapping("login")
	public String returnloginjsp() {
		return "manager/login";
	}




	/**
	 * 验证码
	 *
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping("codeqr")
	public void code(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		yzm.service(request, response);
	}





	/**
	 * 登陆页面
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("backmangerlogin")
	public ResponseEntity<?> backMangerLogin(HttpServletRequest request,
                                             HttpServletResponse response, HttpSession session) {
		String username = request.getParameter("usename");
		String password = request.getParameter("password");
		String codejsp = request.getParameter("codejsp");
		int flag=0;
		// 根据登陆名和密码查询用户
		Map map = new HashMap();
		map.put("username", username);
		map.put("password", password);
		BackGroundLogin user = service.loginback(map);
		String code=(String) session.getAttribute("validateCode");
		if (code.equalsIgnoreCase(codejsp)) {
			if (null != user) {
				HttpSession ss = request.getSession();
				ss.setAttribute("bgid", user.getId());
				ss.setAttribute("username", user.getUsername());
				flag=1;
			} else {
				flag=2;
			}
		}else{
			flag=3;
		}
		Map valueMap = new HashMap();
		valueMap.put("flag",flag);
		return ResponseEntity.ok(valueMap);
	}






	/**
	 * 退出登陆
	 * @param request
	 * @return
	 */
	@RequestMapping("backmangerloginout")
	public void backMangerLoginOut(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.getSession().removeAttribute("bgid");
		request.getSession().removeAttribute("username");
		response.sendRedirect("login");
	}
}
