package com.compli.rest;

import com.compli.annotation.Authorised;
import com.compli.bean.notification.*;
import com.compli.db.bean.Limit;
import com.compli.db.bean.UserBean;
import com.compli.managers.AuthorisationManager;
import com.compli.managers.NotificationManager;

import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Path("/notification")
@Produces(MediaType.APPLICATION_JSON)
public class NotificationRestApi extends Application {
    @GET
    @Authorised(role= Authorised.ROLE.ALL)
    public Response getNotification(){
        NotificationManager manager = new NotificationManager();
        List<NotificationData> notificaionts = manager.getNotificationorForAdmin();
        return Response.ok(notificaionts).build();
    }

    @POST
    @Authorised(role= Authorised.ROLE.ALL)
    public Response saveNotification(Notification notification) {
        NotificationManager manager = new NotificationManager();
        manager.saveNotification(notification);
        return Response.ok().build();
    }

    @POST
    @Authorised(role= Authorised.ROLE.ALL)
    @Path("/saveByLaw")
    public Response saveNotificationByLawId(NotificationByLaw notificationByLaw) {
        NotificationManager manager = new NotificationManager();
        manager.saveNotification(notificationByLaw);
        return Response.ok().build();
    }

    @DELETE
    @Authorised(role= Authorised.ROLE.ALL)
    @Path("/{notificationId}")
    public Response deleteNotification(@PathParam("notificationId") int notificationId) {
        NotificationManager manager = new NotificationManager();
        manager.deleteNotification(notificationId);
        return Response.ok().build();
    }

    @POST
    @Authorised(role= Authorised.ROLE.ALL)
    @Path("/all")
    public Response getAllNotification(@HeaderParam("auth")String auth, Limit limit) throws ExecutionException {
        UserBean userBean = AuthorisationManager.getUserCatche(auth);
        NotificationManager notificationManager = new NotificationManager();
        List<Notification> notifications = notificationManager.getNotifications(userBean.getUserId(), limit.getFrom(), limit.getSize());
        return Response.ok(notifications).build();
    }

    @POST
    @Authorised(role= Authorised.ROLE.ALL)
    @Path("/markread")
    public Response clearAllRaed(@HeaderParam("auth")String auth) throws ExecutionException {
        UserBean userBean = AuthorisationManager.getUserCatche(auth);
        NotificationManager notificationManager = new NotificationManager();
        notificationManager.markRead(userBean.getUserId());
        userBean.setUnreadMessage(0);
        AuthorisationManager.setUserBean(auth,userBean);
        return Response.ok().build();
    }

    @POST
    @Authorised(role= Authorised.ROLE.ALL)
    @Path("/email_log")
    public Response getEmailLogs(@HeaderParam("auth")String auth, Limit limit) throws ExecutionException {
        UserBean userBean = AuthorisationManager.getUserCatche(auth);
        NotificationManager notificationManager = new NotificationManager();
        EmailLog notifications = notificationManager.getEmailLogs(limit.getFrom(), limit.getSize());
        return Response.ok(notifications).build();
    }
}
