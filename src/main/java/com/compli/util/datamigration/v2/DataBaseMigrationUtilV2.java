package com.compli.util.datamigration.v2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.compli.db.bean.migration.v2.UserBean;
import com.compli.db.dao.UserDao;

public class DataBaseMigrationUtilV2 {
	private static String FILE_NAME = "C:\\report\\db_tracker.xlsx";
	private static int ROW_COUNT = 27;
	private static int DELAY = 2*1000;
	public static void main(String[] args) throws IOException {
		startUploading();
		startUploadingFiles();
		//updateUserDetails();
	}
	
	public static void updateUserDetails() throws IOException{
		FILE_NAME = "C:\\report\\db_auth_user.xlsx";
		Sheet sheet = readSheetFile();
		DBMigrationUtilV2UserDetails.init();
		DBMigrationUtilV2UserDetails.updateUserDetails(sheet);
		try {	Thread.sleep(DELAY);} catch (InterruptedException e) {e.printStackTrace();}

	}
	
	public static void startUploading() throws IOException{
		FILE_NAME = "C:\\report\\db_tracker_1.xlsx";
		Sheet sheet = readSheetFile();
		boolean isValidSheet = validateSheet(sheet);
		System.out.println("Is valid "+isValidSheet);
		if(isValidSheet){
			DBMigrationUtilV2User.init();
			DBMigrationUtilV2User.createUsers(sheet);
			try {	Thread.sleep(DELAY);} catch (InterruptedException e) {e.printStackTrace();}
			
			DBMigrationUtilV2Company.init();
			DBMigrationUtilV2Company.createCompanies(sheet);
			try {	Thread.sleep(DELAY);} catch (InterruptedException e) {e.printStackTrace();}
			
			
			DBMigrationUtilV2UserCompany.init();
			DBMigrationUtilV2UserCompany.createUserCompany(sheet);
			try {	Thread.sleep(DELAY);} catch (InterruptedException e) {e.printStackTrace();}
			
			DBMigrationUtilV2Location.init();
			DBMigrationUtilV2Location.createLocations(sheet);
			try {	Thread.sleep(DELAY);} catch (InterruptedException e) {e.printStackTrace();}
			
			DBMigrationUtilV2CompanyLocations.init();
			DBMigrationUtilV2CompanyLocations.createCompaniesLocation(sheet);
			try {	Thread.sleep(DELAY);} catch (InterruptedException e) {e.printStackTrace();}
			
			DBMigrationUtilV2PeriodicityMaster.init();
			DBMigrationUtilV2PeriodicityMaster.createPeriodicityMaster(sheet);
			try {	Thread.sleep(DELAY);} catch (InterruptedException e) {e.printStackTrace();}
			
			DBMigrationUtilV2PeriodicityDateMaster.init();
			DBMigrationUtilV2PeriodicityDateMaster.createPeriodicityDateMaster(sheet);
			try {	Thread.sleep(DELAY);} catch (InterruptedException e) {e.printStackTrace();}
			
			DBMigrationUtilV2LawMaster.init();
			DBMigrationUtilV2LawMaster.createLawMaster(sheet);
			try {	Thread.sleep(DELAY);} catch (InterruptedException e) {e.printStackTrace();}
			
			DBMigrationUtilV2ActivityMaster.init();
			DBMigrationUtilV2ActivityMaster.createActivityMaster(sheet);
			try {	Thread.sleep(DELAY);} catch (InterruptedException e) {e.printStackTrace();}
			
			DBMigrationUtilV2ActivityAssociation.init();
			DBMigrationUtilV2ActivityAssociation.createActivityAssociation(sheet);
			try {	Thread.sleep(DELAY);} catch (InterruptedException e) {e.printStackTrace();}
			
			
		   DBMigrationUtilV2Activity.init();
			DBMigrationUtilV2Activity.createActivity(sheet);
			try {	Thread.sleep(DELAY);} catch (InterruptedException e) {e.printStackTrace();}
			
			DBMigrationUtilV2ActivityAssignment.init();
			DBMigrationUtilV2ActivityAssignment.updateUserDetails(sheet);	
		}
	}
	
	public static void startUploadingFiles() throws IOException{
		FILE_NAME = "C:\\report\\db_submit_detail.xlsx";
		Sheet sheet = readSheetFile();
		/*DBMigrationUtilV2FIles.init();
		DBMigrationUtilV2FIles.createFIles(sheet);*/
		
		DBMigrationUtilV2FIlesDriveRefresh.init();
		DBMigrationUtilV2FIlesDriveRefresh.refreshFilesId();
	}
	
	public static Sheet readSheetFile() throws IOException{
		FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));
        Workbook workbook = new XSSFWorkbook(excelFile);
        Sheet datatypeSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = datatypeSheet.iterator();
        int rowCount = 0;
        Set<Integer>count = new HashSet<Integer>();
        while (iterator.hasNext()) {
        	rowCount++;
        	Row currentRow = iterator.next();
        	int cn;
            for(cn=0; cn<currentRow.getLastCellNum(); cn++) {
                // If the cell is missing from the file, generate a blank one
                // (Works by specifying a MissingCellPolicy)
                Cell cell = currentRow.getCell(cn, Row.CREATE_NULL_AS_BLANK);
            }
            count.add(cn);
        }      
        return datatypeSheet;
       }

	public static boolean validateSheet(Sheet datatypeSheet){
		Set<Integer>count = new HashSet<Integer>();
		Iterator<Row> iterator = datatypeSheet.iterator();
		while (iterator.hasNext()) {
        	Row currentRow = iterator.next();
            count.add((int) currentRow.getLastCellNum());
        }
		count.remove(ROW_COUNT);
		return count.size()==0;
	}
	
	
}
