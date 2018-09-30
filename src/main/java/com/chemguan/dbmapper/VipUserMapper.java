package com.chemguan.dbmapper;

import com.chemguan.model.VipUser;

import java.util.List;

public interface VipUserMapper {
    int deleteByPrimaryKey(Integer userid);

    int insert(VipUser record);

    int insertSelective(VipUser record);

    VipUser selectByPrimaryKey(Integer userid);

    int updateByPrimaryKeySelective(VipUser record);

    int updateByPrimaryKey(VipUser record);

    List<VipUser> selectAllUser();
}