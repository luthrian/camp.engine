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


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;

import com.camsolute.code.camp.lib.models.order.Order;
import com.camsolute.code.camp.lib.models.order.OrderRest;
import com.camsolute.code.camp.lib.models.process.OrderProcess;
import com.camsolute.code.camp.lib.models.process.Process;
import com.camsolute.code.camp.lib.models.process.ProcessRest;
import com.camsolute.code.camp.lib.models.process.ProductProcess;
import com.camsolute.code.camp.lib.models.process.ProductionProcess;
import com.camsolute.code.camp.lib.models.product.Product;



public class UnregisterProductionProcessDelegate implements JavaDelegate {
	public static final boolean _DEBUG = true;
	public static final boolean _IN_PRODUCTION = false;
	private static final Logger LOG = LogManager.getLogger(UnregisterProductionProcessDelegate.class);
	private static String fmt = "[%15s] [%s]";
	
	private Expression targetStatus;
    private Expression processName;
    
	public Expression getTargetStatus() {
		return targetStatus;
	}

	public void setTargetStatus(Expression targetStatus) {
		this.targetStatus = targetStatus;
	}
	public void setProcessName(Expression processName) {
		this.processName = processName;
	}
	public Expression getProcessName() {
		return this.processName;
	}
	public void execute(DelegateExecution execution) throws Exception {_execute(execution,true);}
	public void _execute(DelegateExecution execution,boolean log){
		long startTime = System.currentTimeMillis();
		String _f = null;
		String msg = null;
		if(log && _DEBUG) {
			_f = "[_execute]";
			msg = "====[ registering production process: ]====";LOG.traceEntry(String.format(fmt,_f,msg));
		}
		
		String objectBusinessId = (String) execution.getVariable("objectBusinessId");
		String objectId = (String) execution.getVariable("objectId");
		String objectStatus = (String) execution.getVariable("objectStatus");
//		String activityId = ((ExecutionEntity) execution).getActivityId();
		String processName = (String) getProcessName().getValue(execution);
		String tenantId =((ExecutionEntity) execution).getTenantId();
		String caseInstanceId = ((ExecutionEntity) execution).getCaseInstanceId(); 
		String processInstanceId = ((ExecutionEntity) execution).getProcessInstanceId();
		String businessKey = ((ExecutionEntity) execution).getBusinessKey();
		String definitionId = ((ExecutionEntity) execution).getProcessDefinitionId();
		boolean ended = ((ExecutionEntity) execution).isEnded();
		boolean suspended = ((ExecutionEntity) execution).isSuspended();
		String executionId = ((ExecutionEntity) execution).getId();
//		Process<Order,ProductionProcess> process = new Process<Order,ProductionProcess>(executionId, processInstanceId, businessKey, processName, definitionId,tenantId, caseInstanceId,ended,suspended,Process.ProcessType.product_process);
		
		if(log && _DEBUG){msg = "----[objectBusinessId '"+objectBusinessId+"']----";LOG.info(String.format(fmt, _f,msg));}
		if(log && _DEBUG){msg = "----[objectId '"+objectId+"']----";LOG.info(String.format(fmt, _f,msg));}
		if(log && _DEBUG){msg = "----[objectStatus '"+objectStatus+"']----";LOG.info(String.format(fmt, _f,msg));}
		if(log && _DEBUG){msg = "----[businessKey '"+businessKey+"']----";LOG.info(String.format(fmt, _f,msg));}
		if(log && _DEBUG){msg = "----[processInstanceId '"+processInstanceId+"']----";LOG.info(String.format(fmt, _f,msg));}
		if(log && _DEBUG){msg = "----[processName '"+processName+"']----";LOG.info(String.format(fmt, _f,msg));}

		OrderRest.instance().delProcessReference(objectBusinessId, processInstanceId, businessKey, log);
	}

}
