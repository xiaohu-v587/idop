package com.goodcol.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goodcol.core.plugin.activerecord.Record;

public class AffairsUtil {
	/**
	 * 找到下一个审批角色
	 */
	public static String getNextRoleId(Record jobRecord, String currRoleId) {
		String nextRoleId = ""; // 下一级审批角色
		if (jobRecord == null) {
			return nextRoleId;
		}
		String start = jobRecord.get("START_ROLE_ID");
		String one = jobRecord.get("ROLE_ONE_ID");
		String two = jobRecord.get("ROLE_TWO_ID");
		String three = jobRecord.get("ROLE_THREE_ID");
		String four = jobRecord.get("ROLE_FOUR_ID");
		String five = jobRecord.get("ROLE_FOVE_ID");
		String six = jobRecord.get("ROLE_SIX_ID");
		String seven = jobRecord.get("ROLE_SEVEN_ID");
		String end = jobRecord.get("END_ROLE_ID");
		// 存储，比如 1， 角色id1
		Map<Integer, String> nextRoleMap = new HashMap<Integer, String>();
		// 存储，比如 1
		List<Integer> nexeRoleList = new ArrayList<Integer>();

		nextRoleMap.put(0, start);
		nexeRoleList.add(0);
		nextRoleMap.put(1, one);
		nexeRoleList.add(1);
		nextRoleMap.put(2, two);
		nexeRoleList.add(2);
		nextRoleMap.put(3, three);
		nexeRoleList.add(3);
		nextRoleMap.put(4, four);
		nexeRoleList.add(4);
		nextRoleMap.put(5, five);
		nexeRoleList.add(5);
		nextRoleMap.put(6, six);
		nexeRoleList.add(6);
		nextRoleMap.put(7, seven);
		nexeRoleList.add(7);
		nextRoleMap.put(8, end);
		nexeRoleList.add(8);

		// 寻找下一个审批角色
		// 存储当前用户的角色和这条事务类别角色相同时的标记
		int nextRoleFlag = 0;
		String nextRoleFlag1 = "";
		if (nexeRoleList != null && nexeRoleList.size() > 0) {
			for (int i = 0; i < nexeRoleList.size(); i++) {
				if (nextRoleMap.get(i) != null
						&& currRoleId.equals(nextRoleMap.get(i))) {
					nextRoleFlag = i;
					nextRoleFlag1 = "ok";
				}
				if ("ok".equals(nextRoleFlag1) && i > nextRoleFlag
						&& nextRoleMap.get(i) != null) {
					// 代表当前的审批角色大于用户角色的级别，并且当前的审批角色不为空
					nextRoleId = nextRoleMap.get(i);
					break;
				}
			}
		}
		return nextRoleId;
	}
}
