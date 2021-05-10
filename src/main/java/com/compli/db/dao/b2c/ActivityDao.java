package com.compli.db.dao.b2c;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityDao {
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Integer getMaximumActivityId(){
        Map<String, String> map = new HashMap<>();
        List<Map<String, Object>> maxActivity = this.namedParameterJdbcTemplate.queryForList(
                "select CAST(activityId AS UNSIGNED)as maxactivitycount from activity_master_b2c order by maxactivitycount desc limit 1;", map);
        //This is first activity in table
        if (maxActivity.size() == 0) return 0;

        return Integer.parseInt((String) maxActivity.get(0).get("maxactivitycount").toString());
    }
}
