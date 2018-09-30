package com.chemguan.dbmapper;

import com.chemguan.model.BackGroundLogin;

import java.util.List;
import java.util.Map;

public interface BackGroundLoginDao {

	//查询分页
	List<BackGroundLogin> selectAllBgUser();

	int deleteBgUserById(Integer id);

	int insertBgUser(BackGroundLogin bg);

	BackGroundLogin checkbgusername(String username);

	BackGroundLogin selectBgUserById(Integer id);

	int updateBgUser(BackGroundLogin bg);

	BackGroundLogin loginback(Map map);
}