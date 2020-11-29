package com.compli.db.dao;

import com.compli.bean.notification.Notification;
import com.mysql.cj.api.jdbc.Statement;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

public class NotificationDao {
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public int saveNotification(Notification notification, Date expiryDate){
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query = "INSERT INTO `compli`.`notification`\n" +
                "(" +
                "`title`,\n" +
                "`notification`,\n" +
                "`userType`,\n" +
                "`locations`,\n" +
                "`severity`,\n" +
                "`is_all_location`,\n" +
                "`creationDate`,\n" +
                "`expiryDate`)\n" +
                "VALUES(?,?,?,?,?,?,?,?)";

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, notification.getTitle());
            ps.setString(2, notification.getNotification());
            ps.setString(3, notification.getType());
            ps.setString(4, String.join(",", notification.getLocations()));
            ps.setString(5, notification.getSeverity());
            ps.setBoolean(6, notification.isAllLocation());
            Date creationDate = new Date();
            ps.setTimestamp(7, new Timestamp(creationDate.getTime()));
            ps.setTimestamp(8, new Timestamp(expiryDate.getTime()));
            return ps;
        },keyHolder);
        return keyHolder.getKey().intValue();
    }

    public boolean assigneNotificationForUserOfLocation(List<String>locations, int year, int notificationId){
        String assignNotification = "insert into notification_user (userId,notification_id)" +
                " select distinct(userId),:notificationId from activity_assignment join" +
                " (select activityassociation.activityId from activitymaster join activityassociation on activitymaster.activityId = activityassociation.activityId where locationId in (:locations) and periodicityDateId > concat(:yearFrom,'-03-31')) act" +
                " on act.activityId = activity_assignment.activityId";
        HashMap namedMap = new HashMap();
        namedMap.put("locations",locations);
        namedMap.put("yearFrom",year);
        namedMap.put("notificationId",notificationId);
        int updateUser = this.namedParameterJdbcTemplate.update(assignNotification,namedMap);
        return true;
    }

    public int getCountOfUnreadMessageForUser(String userId){
        String getCountOfUNreadMessageForUser = "select count(*) as count from notification_user where userId=:userId and isRead=false";
        int count = 0;
        Map<String,String> param = new HashMap<>();
        param.put("userId",userId);
        Map<String, Object> countData = this.namedParameterJdbcTemplate.queryForMap(getCountOfUNreadMessageForUser, param);
        Long countL = (long) countData.get("count");
        count = countL.intValue();
        return count;
    }

    public List<Notification> getNotification(String userId, int from , int size){
        String sql = "select * from notification join notification_user on notification.id=notification_user.notification_id " +
                "where userId = :userId order by creationDate desc limit :from,:size";
        Map<String,Object> namedMap = new HashMap<>();
        namedMap.put("userId",userId);
        namedMap.put("from",from);
        namedMap.put("size",size);
        return this.namedParameterJdbcTemplate.query(sql, namedMap,
                (rs, rowNum) -> {
                    Notification notification = new Notification();
                    notification.setTitle(rs.getString(2));
                    notification.setNotification(rs.getString(3));
                    notification.setCreationDate(rs.getDate(7));
                    notification.setSeverity(rs.getString(9));
                    notification.setRead(rs.getBoolean(12));
                    return notification;
                });
    }

    public void markAllReadForUser(String userId){
        String markRead = "update notification_user set isRead = true where userId=:userId and isRead = false";
        Map<String,Object> namedMap = new HashMap<>();
        namedMap.put("userId",userId);
        this.namedParameterJdbcTemplate.update(markRead,namedMap);
    }
}
