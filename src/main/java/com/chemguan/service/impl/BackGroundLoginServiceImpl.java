package com.chemguan.service.impl;

import com.chemguan.dbmapper.BackGroundLoginDao;
import com.chemguan.model.BackGroundLogin;
import com.chemguan.service.BackGroundLoginService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class BackGroundLoginServiceImpl implements BackGroundLoginService {

	@Autowired
	private BackGroundLoginDao dao;


	public PageInfo listAllBgUserByPage(Integer pn, Integer pageSize) {
		PageHelper.startPage(pn==null?1:pn, pageSize,true);
		List<BackGroundLogin> list = dao.selectAllBgUser();
		PageInfo pageInfo = new PageInfo(list);
		return pageInfo;
	}

	public int deleteBgUserById(Integer id) {
		return dao.deleteBgUserById(id);
	}

	public int insertBgUser(BackGroundLogin bg) {
		int s=dao.insertBgUser(bg);
		//System.out.println(1/0);
		return s;
	}

	public BackGroundLogin checkbgusername(String username) {
		return dao.checkbgusername(username);
	}

	public BackGroundLogin selectBgUserById(Integer id) {
		return dao.selectBgUserById(id);
	}

	public int updateBgUser(BackGroundLogin bg) {
		return dao.updateBgUser(bg);
	}

	public BackGroundLogin loginback(Map map) {
		return dao.loginback(map);
	}
}
