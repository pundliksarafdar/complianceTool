package com.compli.db.dao;

import com.compli.bean.notification.EmailBean;
import com.compli.bean.notification.EmailLogBean;
import com.compli.bean.notification.Notification;
import com.compli.bean.notification.NotificationData;
import com.mysql.cj.api.jdbc.Statement;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
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

    public boolean assignNotificationForUserOfLocationAndLaw(String lawName, List<String>locations, int year, int notificationId,String userTypeId, boolean isAllLocation){
        //Dummy is added to remove error for empty list
        if(locations.size() == 0){
            locations.add("dummy");
        }
        String assignNotification = "insert into notification_user (userId,notification_id)" +
                "select distinct(user.userId),:notificationId from activitymaster join lawmaster  on " +
                "activitymaster.lawId = lawmaster.lawId and periodicityDateId >  concat(:yearFrom,'-03-31') and lawName=:lawName join activity_assignment on " +
                "activity_assignment.activityId=activitymaster.activityId join activityassociation on activityassociation.activityId=activity_assignment.activityId and (activityassociation.locationId in (:locations) or 'all'=:isAllLocation) join user on " +
                "user.userId=activity_assignment.userId and user.userTypeId=:userTypeId";

        HashMap namedMap = new HashMap();
        namedMap.put("locations",locations);
        namedMap.put("lawName",lawName);
        namedMap.put("yearFrom",year);
        namedMap.put("notificationId",notificationId);
        namedMap.put("userTypeId",userTypeId);
        namedMap.put("isAllLocation",isAllLocation?"all":"no");

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

    public List<NotificationData>getNotificationorForAdminList(){
        String sql = "select * from notification order by creationDate desc";
        Map<String,Object> namedMap = new HashMap<>();
        return this.namedParameterJdbcTemplate.query(sql, namedMap,
                (rs, rowNum) -> {
                    NotificationData notification = new NotificationData();
                    notification.setId(rs.getInt(1)+"");
                    notification.setTitle(rs.getString(2));
                    notification.setNotification(rs.getString(3));
                    notification.setCreationDate(rs.getDate(7));
                    notification.setExpiryDate(rs.getDate(7));
                    notification.setSeverity(rs.getString(9));
                    return notification;
                });
    }

    public void markAllReadForUser(String userId){
        String markRead = "update notification_user set isRead = true where userId=:userId and isRead = false";
        Map<String,Object> namedMap = new HashMap<>();
        namedMap.put("userId",userId);
        this.namedParameterJdbcTemplate.update(markRead,namedMap);
    }

    public void deleteNotification(int notificationId) {
        Map<String,Object> namedMap = new HashMap<>();
        namedMap.put("notification_id",notificationId);
        String deleteUsersForNotif = "delete from notification_user where notification_id=:notification_id";
        String deleteNotif = "delete from notification where id=:notification_id";
        this.namedParameterJdbcTemplate.update(deleteUsersForNotif, namedMap);
        this.namedParameterJdbcTemplate.update(deleteNotif, namedMap);
    }

    public List getUserForNotification(String lawName, List<String>locations, int year, int notificationId, String userTypeId, boolean isAllLocation){
        String sql = "select distinct(user.userId),user.email,user.userTypeId,user.firstname from activitymaster join lawmaster  on " +
                "activitymaster.lawId = lawmaster.lawId and periodicityDateId >  concat(:yearFrom,'-03-31') and lawName=:lawName join activity_assignment on " +
                "activity_assignment.activityId=activitymaster.activityId join activityassociation on activityassociation.activityId=activity_assignment.activityId and (activityassociation.locationId in (:locations) or 'all'=:isAllLocation) join user on " +
                "user.userId=activity_assignment.userId and user.userTypeId=:userTypeId";

        HashMap namedMap = new HashMap();
        namedMap.put("locations",locations);
        namedMap.put("lawName",lawName);
        namedMap.put("yearFrom",year);
        namedMap.put("userTypeId",userTypeId);
        namedMap.put("isAllLocation",isAllLocation?"all":"no");

        return this.namedParameterJdbcTemplate.query(sql, namedMap,
                (rs, rowNum) -> {
                    EmailBean emailBean = new EmailBean();
                    emailBean.setEmailId(rs.getString(2));
                    emailBean.setUserType(rs.getString(3));
                    emailBean.setName(rs.getString(4));
                    return emailBean;

                });
    }

    public void logEmail(List<EmailLogBean>emailLogBeans){
        String sql = "insert into email_logs(email, subject, content, cdate) values(?, ?, ?, ?)";
        this.jdbcTemplate.batchUpdate(sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, emailLogBeans.get(i).getEmail());
                        ps.setString(2, emailLogBeans.get(i).getSubject());
                        ps.setString(3, emailLogBeans.get(i).getContent());
                        ps.setDate(4 ,new java.sql.Date(new Date().getTime()));
                    }
                    @Override
                    public int getBatchSize() {
                        return emailLogBeans.size();
                    }
                });
    }

    public List<EmailLogBean> getEmailLogs(int from, int size) {
        String sql = "select * from email_logs order by cdate desc limit :from, :size";
        Map<String,Object> namedMap = new HashMap<>();
        namedMap.put("from", from);
        namedMap.put("size", size);
        return this.namedParameterJdbcTemplate.query(sql, namedMap,
                (rs, rowNum) -> {
                    EmailLogBean emailLog = new EmailLogBean();
                    emailLog.setEmail(rs.getString(1));
                    emailLog.setSubject(rs.getString(2));
                    emailLog.setContent(rs.getString(3));
                    emailLog.setCreationDate(rs.getDate(4));
                    return emailLog;
                });
    }

    public long getCountOfEmailLog(){
        String sql = "select count(*) as count from email_logs";
        Map<String,Object> namedMap = new HashMap<>();
        Map<String, Object> countMap = this.namedParameterJdbcTemplate.queryForMap(sql, namedMap);
        return (long)countMap.get("count");
    }

    public void cleanUpEmailsLog(int days){
        String sql = "delete from email_logs where cdate < now() - interval :days day";
        Map<String, Object> dateMap  = new HashMap<>();
        dateMap.put("days",days);
        this.namedParameterJdbcTemplate.update(sql, dateMap);
    }
}
