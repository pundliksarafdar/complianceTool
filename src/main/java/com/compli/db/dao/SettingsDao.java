package com.compli.db.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.compli.db.bean.Pair;

public class SettingsDao {
	
	private JdbcTemplate jdbcTemplate;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public  void saveSettings(List<Pair>pairs){
		String sql = "INSERT INTO settings(keyfordata,val) VALUES (?,?) ON DUPLICATE KEY UPDATE   val = ?";
		this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Pair pair = pairs.get(i);
				ps.setString(1, pair.getKeyfordata());
				ps.setString(2, pair.getVal());
				ps.setString(3, pair.getVal());
			}
			 
			@Override
			public int getBatchSize() {
				return pairs.size();
			}
		});
	}
	
	public List<Pair> getSettings(){
		String sql = "select * from settings";
		return this.jdbcTemplate.query(sql, new BeanPropertyRowMapper(Pair.class));
	}
}
