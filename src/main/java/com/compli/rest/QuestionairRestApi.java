package com.compli.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.compli.bean.QuestionnairBean;
import com.compli.managers.QuestionnaireManager;

@Path("questionnaire")
@Produces(MediaType.APPLICATION_JSON)
public class QuestionairRestApi extends Application{
	@GET
	public Response getQuestionnaire(){
		return Response.ok(new QuestionnaireManager().getQuestionnair()).build(); 
	}

	@POST
	public Response setQuestionnaire(List<QuestionnairBean> questionnairBeans){
		return Response.ok().build(); 
	}

}

