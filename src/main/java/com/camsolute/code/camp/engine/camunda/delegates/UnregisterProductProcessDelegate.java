/*******************************************************************************
 * Copyright (C) 2018 Christopher Campbell (campbellccc@gmail.com)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 * 	Christopher Campbell (campbellccc@gmail.com) - all code prior and post initial release
 ******************************************************************************/
package com.camsolute.code.camp.engine.camunda.delegates;

import java.text.SimpleDateFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;

import com.camsolute.code.camp.lib.models.process.Process;
import com.camsolute.code.camp.lib.models.process.ProductProcess;
import com.camsolute.code.camp.lib.models.product.Product;
import com.camsolute.code.camp.lib.models.product.ProductRest;
import com.camsolute.code.camp.lib.utilities.Util;


public class UnregisterProductProcessDelegate implements JavaDelegate {
	private static final Logger LOG = LogManager.getLogger(UnregisterProductProcessDelegate.class);
	private static String fmt = "[%15s] [%s]";
	
  private Expression processName;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
//		PostRestClient client = new PostRestClient();
		
		String objectBusinessId = (String) execution.getVariable("objectBusinessId");
		String objectId = (String) execution.getVariable("objectId");
		String objectStatus = (String) execution.getVariable("objectStatus");
		String objectType = (String) execution.getVariable("objectType");
		String objectPrincipal = (String) execution.getVariable("objectPrincipal");
		String processName = (String) getProcessName().getValue(execution);
		String processInstanceId = ((ExecutionEntity) execution).getProcessInstanceId();
		String businessKey = ((ExecutionEntity) execution).getBusinessKey();
		String tenantId = ((ExecutionEntity) execution).getTenantId();
		String caseInstanceId = ((ExecutionEntity) execution).getCaseInstanceId();
		String definitionId = ((ExecutionEntity) execution).getProcessDefinitionId();
		boolean suspended = ((ExecutionEntity) execution).isSuspended();
		boolean ended = ((ExecutionEntity) execution).isEnded();
		String executionId = ((ExecutionEntity) execution).getId();
//		Process<Product,ProductProcess> process = new Process<Product,ProductProcess>(executionId, processInstanceId, businessKey, processName, definitionId,tenantId, caseInstanceId,ended,suspended,Process.ProcessType.product_process);
		if(!Util._IN_PRODUCTION){String msg = "----[objectBusinessId '"+objectBusinessId+"']----";LOG.info(String.format(fmt, "execute",msg));}
		if(!Util._IN_PRODUCTION){String msg = "----[objectId '"+objectId+"']----";LOG.info(String.format(fmt, "execute",msg));}
		if(!Util._IN_PRODUCTION){String msg = "----[objectStatus '"+objectStatus+"']----";LOG.info(String.format(fmt, "execute",msg));}
		if(!Util._IN_PRODUCTION){String msg = "----[objectType('"+objectType+"') ]----";LOG.info(String.format(fmt, "execute",msg));}
		if(!Util._IN_PRODUCTION){String msg = "----[objectPrincipal('"+objectPrincipal+"') ]----";LOG.info(String.format(fmt, "execute",msg));}
		if(!Util._IN_PRODUCTION){String msg = "----[businessKey '"+businessKey+"']----";LOG.info(String.format(fmt, "execute",msg));}
		if(!Util._IN_PRODUCTION){String msg = "----[processName '"+processName+"']----";LOG.info(String.format(fmt, "execute",msg));}
		if(!Util._IN_PRODUCTION){String msg = "----[processInstanceId '"+processInstanceId+"']----";LOG.info(String.format(fmt, "execute",msg));}
		if(!Util._IN_PRODUCTION){String msg = "----[executionId '"+executionId+"']----";LOG.info(String.format(fmt, "execute",msg));}
		if(!Util._IN_PRODUCTION){String msg = "----[tenantId '"+tenantId+"']----";LOG.info(String.format(fmt, "execute",msg));}
		if(!Util._IN_PRODUCTION){String msg = "----[caseInstanceId '"+caseInstanceId+"']----";LOG.info(String.format(fmt, "execute",msg));}
		if(!Util._IN_PRODUCTION){String msg = "----[definitionId '"+definitionId+"']----";LOG.info(String.format(fmt, "execute",msg));}
		if(!Util._IN_PRODUCTION){String msg = "----[ended '"+ended+"']----";LOG.info(String.format(fmt, "execute",msg));}
		if(!Util._IN_PRODUCTION){String msg = "----[suspended '"+suspended+"']----";LOG.info(String.format(fmt, "execute",msg));}

		
//		ProductProcessRestDao.instance().registerProcess(orderNumber, process);
		ProductRest.instance().delProcessReference(objectBusinessId, processInstanceId, businessKey, !Util._IN_PRODUCTION);
	}

	public Expression getProcessName() {
		return processName;
	}

	public void setProcessName(Expression processName) {
		this.processName = processName;
	}

}
