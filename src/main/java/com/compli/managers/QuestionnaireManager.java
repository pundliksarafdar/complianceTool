package com.compli.managers;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import com.compli.bean.QuestionnairBean;

public class QuestionnaireManager {
	
	//As not required frequently no need of loading in static object and object can be static
	public List<QuestionnairBean>  getQuestionnair(){
		List<QuestionnairBean> questionnair = new ArrayList<QuestionnairBean>();
		Properties prop = null;
		try {
			InputStream input = this.getClass().getResourceAsStream("/questionnaire.properties");
			prop = new Properties();  
		    prop.load(input);  
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Enumeration<Object> keys = prop.keys();
		while (keys.hasMoreElements()) {
			String obj = (String) keys.nextElement();
			QuestionnairBean questionnairBean = new QuestionnairBean();
			questionnairBean.setQuestionnairId(obj);
			questionnairBean.setQuestion(prop.getProperty(obj));
			questionnair.add(questionnairBean);
		}
		questionnair.sort(new Comparator<QuestionnairBean>() {
			public int compare(QuestionnairBean o1, QuestionnairBean o2) {
				return Integer.parseInt(o1.getQuestionnairId())-Integer.parseInt(o2.getQuestionnairId());
			}
		});
		return questionnair;
	}
}
