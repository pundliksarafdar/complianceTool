package com.compli.db.dao;

import com.compli.bean.backup.User;
import com.compli.db.bean.MasterDataBean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterDataDao {
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<MasterDataBean> getMasterDataLocations(List<String>locationId){
        List<MasterDataBean> masterData = new ArrayList<>();
        String sql = "select * from masterdata where locationId in (:locationId)";
        Map datamap = new HashMap(){{put("locationId",locationId);}};
        masterData = this.namedParameterJdbcTemplate.query(sql,datamap, new BeanPropertyRowMapper<MasterDataBean>(MasterDataBean.class));
        return masterData;
    }
}
