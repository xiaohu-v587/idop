package com.goodcol.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;

public class IndustryImport {
//	public static void main(String[] args) throws Exception {
//		// getCompanyDetail(null);
//
//		FileInputStream input = new FileInputStream(new File(
//				"E:\\行业分类.csv"));
//		BufferedReader bufferedReader = new BufferedReader(
//				new InputStreamReader(input, "gbk"));
//		String line = null;
//		int i = 0;
//		int id = 10001;
//		String pcode = "";// 原始A
//		String pcode1 = "";
//		String pcode2 = "";
//		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(
//				new File("E:\\hy2018.txt")));
//
//		while ((line = bufferedReader.readLine()) != null) {
//			if (i < 3) {
//				i++;
//				continue;
//			}
//			id++;
//			String insertSql = "insert into t_cust_application_area (id ,parent_id,code,name,description,sort_order,status) values ('"
//					+ id + "','";
//			String[] sts = line.split(",");
//			if (StringUtils.isNotEmpty(sts[0])) {// A
//				if (sts.length > 5) {
//					insertSql += ("','" + sts[0] + "','" + sts[4] + "','"
//							+ sts[5].trim() + "','0','0');");
//				} else {
//					insertSql += ("','" + sts[0] + "','" + sts[4] + "','','0','0');");
//				}
//
//				pcode = sts[0];// A
//				System.out.println(insertSql);
//				bufferedWriter.write(insertSql + "\n");
//				continue;
//			}
//			if (StringUtils.isNotEmpty(sts[1])) {// A01
//				if (sts.length > 5) {
//					insertSql += (rightAddZero(pcode) + "','"
//							+ rightAddZero(pcode + sts[1]) + "','"
//							+ sts[4].trim() + "','" + sts[5].trim() + "','0','0');");
//				} else {
//					insertSql += (rightAddZero(pcode) + "','"
//							+ rightAddZero(pcode + sts[1]) + "','"
//							+ sts[4].trim() + "','','0','0');");
//				}
//				System.out.println(insertSql);
//				bufferedWriter.write(insertSql + "\n");
//				pcode1 = pcode + sts[1];// A01
//				continue;
//			}
//			if (StringUtils.isNotEmpty(sts[2])) {// A011
//				if (sts.length > 5) {
//					insertSql += (rightAddZero(pcode1) + "','"
//							+ rightAddZero(pcode + sts[2]) + "','"
//							+ sts[4].trim() + "','" + sts[5].trim() + "','0','0');");
//				} else {
//					insertSql += (rightAddZero(pcode1) + "','"
//							+ rightAddZero(pcode + sts[2]) + "','"
//							+ sts[4].trim() + "','','0','0');");
//				}
//
//				pcode2 = pcode + sts[2];// A011
//				System.out.println(insertSql);
//				bufferedWriter.write(insertSql + "\n");
//				continue;
//			}
//			if (StringUtils.isNotEmpty(sts[3])) {// A0111
//				if (sts.length > 5) {
//					insertSql += (rightAddZero(pcode2) + "','"
//							+ rightAddZero(pcode + sts[3]) + "','"
//							+ sts[4].trim() + "','" + sts[5].trim() + "','0','0');");
//				} else {
//					insertSql += (rightAddZero(pcode2) + "','"
//							+ rightAddZero(pcode + sts[3]) + "','"
//							+ sts[4].trim() + "','','0','0');");
//				}
//				System.out.println(insertSql);
//				bufferedWriter.write(insertSql + "\n");
//				continue;
//			}
//		}
//		bufferedWriter.close();
//		input.close();
//		bufferedReader.close();
//	}

	
	public static void main(String[] args) {
		System.out.println(test());
	}
	
	
	public static String test() {
		for (int i = 0; i < 10; i++) {
			if (i == 6) {
				return "6";
			} else if (i == 7) {
				return "7";
			} else {
				continue;
			}
		}
		return "0";
	}
	
	private static String rightAddZero(String str) {
		String zero = "00000";
		if (!StringUtils.isEmpty(str)) {
			if (str.length() < 5) {
				str += zero.substring(0, 5 - str.length());
			}
		}
		return str;
	}
}
