package com.chemguan.service;

import com.chemguan.model.BackGroundLogin;
import com.github.pagehelper.PageInfo;

import java.util.Map;

public interface BackGroundLoginService {
	//查询用户-分页
	PageInfo listAllBgUserByPage(Integer pn, Integer pageSize);

	int deleteBgUserById(Integer id);

	int insertBgUser(BackGroundLogin bg);

	BackGroundLogin checkbgusername(String username);

	BackGroundLogin selectBgUserById(Integer id);

	int updateBgUser(BackGroundLogin bg);

	BackGroundLogin loginback(Map map);
}


