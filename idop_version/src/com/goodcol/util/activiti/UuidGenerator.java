package com.goodcol.util.activiti;

import java.util.UUID;
import org.activiti.engine.impl.cfg.IdGenerator;
public class UuidGenerator implements IdGenerator {
	@Override
	public String getNextId() {
		return UUID.randomUUID().toString().replace("-","");
	}
}