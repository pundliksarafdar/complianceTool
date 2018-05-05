package com.notifier;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Mail {

  public static void main(String[] args) throws IOException {
	  getMailContent(new Object());
}

  public static String getMailContent(Object tempObj) throws IOException {
		HashMap<String, String> dataObj = new HashMap<String, String>();
	    MustacheFactory mf = new DefaultMustacheFactory();
	    Mustache mustache = mf.compile("registrationComplete.mustache");
	    Writer writerStr = new StringWriter();
	    Writer writer = mustache.execute(writerStr, tempObj);
	    String str = writerStr.toString();
	    writer.flush();
	    return str;
  }
  
  public static String getResendMailContent(Object tempObj) throws IOException {
		HashMap<String, String> dataObj = new HashMap<String, String>();
	    MustacheFactory mf = new DefaultMustacheFactory();
	    Mustache mustache = mf.compile("resendRegistrationComplete.mustache");
	    Writer writerStr = new StringWriter();
	    Writer writer = mustache.execute(writerStr, tempObj);
	    String str = writerStr.toString();
	    writer.flush();
	    return str;
}
}
