package com.compli.managers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.compli.bean.SettingsBean;
import com.compli.bean.SettingsScheduleBean;
import com.compli.db.bean.CompanyBean;
import com.compli.db.bean.Pair;
import com.compli.db.dao.CompanyDao;
import com.compli.db.dao.SettingsDao;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;


public class SettingsManager {
	SettingsDao settingsDao;
	CompanyDao companyDao;
	public static SettingsBean settingsBean;
	
	static{
		SettingsManager settingsManager = new SettingsManager();
		refreshSettings();
	}
	
	public SettingsManager() {
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
		this.settingsDao = (SettingsDao) ctx.getBean("settingsDao");
		this.companyDao = (CompanyDao) ctx.getBean("companyDao");
	}
	public boolean saveSettings(SettingsBean settingsBean){
		List<Pair> pairs = new ArrayList<Pair>();
		Field[] fields = settingsBean.getClass().getDeclaredFields();
		for(Field field:fields){
			System.out.println(field.getName());
			try {
				Object fieldValue = field.get(settingsBean);
				System.out.println(fieldValue);
				if(field.get(settingsBean)!=null){
					Pair pair = new Pair(field.getName(), field.get(settingsBean).toString());
					pairs.add(pair);
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		this.settingsDao.saveSettings(pairs);
		SettingsManager.refreshSettings();
		return true;
	}
	
	private SettingsBean getSettings(){
		SettingsBean settingsBean = new SettingsBean();
		List<Pair> settings = this.settingsDao.getSettings();
		try {
			settings.parallelStream().forEach(setting->{ 
				try {
					Field field = settingsBean.getClass().getDeclaredField(setting.getKeyfordata());
					if(field.getType().equals(List.class)){
						List<SettingsScheduleBean> scheduleBeans = new Gson().fromJson(setting.getVal(), new TypeToken<List<SettingsScheduleBean>>(){}.getType());
						field.set(settingsBean, scheduleBeans);
					}else{
						field.set(settingsBean, setting.getVal());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		
		List<SettingsScheduleBean> scheduleBean = getSettingsScheduleBean();
		List<SettingsScheduleBean> complimentedScheduleBean = mergeScheduleBeans(settingsBean.getSettingsScheduleBean(),scheduleBean);
		settingsBean.setSettingsScheduleBean(complimentedScheduleBean);
		return settingsBean;
	}
	
	private List<SettingsScheduleBean>mergeScheduleBeans(List<SettingsScheduleBean> fromDb,List<SettingsScheduleBean> forAllCompany){
		//if database is null then show default companies
		if(null==fromDb){
			return forAllCompany;
		}
		//remove all from allCompany which are present in db
		forAllCompany.removeAll(fromDb) ;
		fromDb.addAll(forAllCompany);
		return fromDb;
	}
	
	public static String getCManagerEmail(String email){
		if(settingsBean.testingmode.equals("true")){
			return settingsBean.getComplainceManagerEmail();
		}else{
			return email;
		}
	}
	
	public static String getCOwnerEmail(String email){
		if(settingsBean.testingmode.equals("true")){
			return settingsBean.getComplainceOwnerEmail();
		}else{
			return email;
		}
	}
	
	public static String getSManagerEmail(String email){
		if(settingsBean.testingmode.equals("true")){
			return settingsBean.getSuperManagerEmail();
		}else{
			return email;
		}
	}
	
	public static String getArtecEmail(String email){
		if(settingsBean.testingmode.equals("true")){
			return settingsBean.getArTechEmail();
		}else{
			return email;
		}
	}
	
	public static void refreshSettings(){
		SettingsManager manager = new SettingsManager();
		SettingsManager.settingsBean = manager.getSettings();
	}
	
	public static SettingsBean getStaticSettings(){
		if(null==SettingsManager.settingsBean){
			refreshSettings();
		}
		return SettingsManager.settingsBean;
	}
	
	public List<SettingsScheduleBean> getSettingsScheduleBean(){
		List<SettingsScheduleBean>scheduleBeans = new ArrayList<SettingsScheduleBean>();
		List<CompanyBean> allCompanies = this.companyDao.getAllCompany();
		for(CompanyBean companyBean:allCompanies){
			String comapanyName = companyBean.getName();
			String companyId = companyBean.getCompanyId();
			SettingsScheduleBean settingsScheduleBean = new SettingsScheduleBean();
			settingsScheduleBean.setCompanyId(companyId);
			settingsScheduleBean.setCompanyName(comapanyName);
			scheduleBeans.add(settingsScheduleBean);			
		}
		return scheduleBeans;
	}
}
