package com.notifier.builder;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;

public class EventBuilder {
	public static Event buildEvent(String summary,String activity,Date date,List<String> attendees,String eventId){
		Event event = new Event();
		event.setSummary(summary)
		.setDescription(activity);
		
		DateTime startDateTime = new DateTime(date, TimeZone.getTimeZone("IST"));
		EventDateTime start = new EventDateTime();
		start.setDateTime(startDateTime).setTimeZone("Asia/Kolkata");
		
		DateTime endDateTime = new DateTime(date, TimeZone.getTimeZone("IST"));
		EventDateTime end = new EventDateTime().setTimeZone("Asia/Kolkata");
		end.setDateTime(endDateTime);
		
		event.setStart(start);
		event.setEnd(end);
		String[] recurrence = new String[] {"RRULE:FREQ=DAILY;COUNT=1"};
    	event.setRecurrence(Arrays.asList(recurrence));
    	
    	/*EventReminder[] reminderOverrides = new EventReminder[] {
        	    new EventReminder().setMethod("popup").setMinutes(100),
        	};
        	Event.Reminders reminders = new Event.Reminders()
        	    .setUseDefault(false)
        	    .setOverrides(Arrays.asList(reminderOverrides));
        	event.setReminders(reminders);*/
        	
        	eventId = getGoogleCalendarId(eventId);
        	
        	List<EventAttendee> eventAttendees = new ArrayList<EventAttendee>();
        	for(String attendee:attendees){
        		eventAttendees.add(new EventAttendee().setEmail(attendee));
        	}
        	event.setAttendees(eventAttendees);
        	event.setId(eventId);
        	return event;
	}
	
	public static String getGoogleCalendarId(String activityId){
		return String.format("%8s", activityId).replace(" ", "a");
	}
}
