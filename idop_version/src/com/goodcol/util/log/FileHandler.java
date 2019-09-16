package com.goodcol.util.log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import com.goodcol.util.PropertiesContent;
import com.goodcol.util.date.BolusDate;


public class FileHandler extends StreamHandler {

	private String app = "";

	private String pattern = "./log/";

	private String today;

	public FileHandler(String app) {
		this.app = app;
		configure();
		openWriteFiles();
	}

	private void configure() {
		pattern = PropertiesContent.get("log.path");
		if (pattern == null) {
			pattern = "./log/";
		}

		setFormatter(new SimpleFormatter());

		try {
			setEncoding("GBK");
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.getMessage());
		}

		String levelVal = PropertiesContent.get("log.level");
		if (levelVal == null || levelVal.equals("")) {
			setLevel(Level.ALL);
		} else {
			setLevel(Level.parse(levelVal.trim()));
		}
	}


	private void openFile() {
		super.close();
		today = BolusDate.getDate();
		String fileName = "";
		String fileDir = pattern + File.separator + today + File.separator;
		File dir = new File(fileDir);
		if (!dir.exists())
			dir.mkdir();
		fileName = fileDir + app + "_" + today + ".log";
		try {
			FileOutputStream fout = new FileOutputStream(fileName, true);
			BufferedOutputStream bout = new BufferedOutputStream(fout);
			setOutputStream(bout);
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}

	}


	private synchronized void openWriteFiles() {
		if (!getLevel().equals(Level.OFF)) {
			openFile();
		}
	}


	public synchronized void publish(LogRecord record) {
		if (!today.equals(BolusDate.getDate())) {
			super.close();
			openFile();
		}
		super.publish(record);
		super.flush();
		return;
	}
}
