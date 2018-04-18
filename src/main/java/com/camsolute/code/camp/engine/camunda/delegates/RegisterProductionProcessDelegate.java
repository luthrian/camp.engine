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
import com.camsolute.code.camp.lib.models.process.Process;
import com.camsolute.code.camp.lib.models.process.ProcessRest;
import com.camsolute.code.camp.lib.models.process.ProductProcess;
import com.camsolute.code.camp.lib.models.process.ProductionProcess;
import com.camsolute.code.camp.lib.models.product.Product;
import com.camsolute.code.camp.lib.models.product.ProductRest;
import com.camsolute.code.camp.lib.utilities.Util;


public class RegisterProductionProcessDelegate implements JavaDelegate {
	private static final Logger LOG = LogManager.getLogger(RegisterProductionProcessDelegate.class);
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
		if(log && !Util._IN_PRODUCTION) {
			_f = "[_execute]";
			msg = "====[ registering production process: ]====";LOG.traceEntry(String.format(fmt,_f,msg));
		}
		
		String objectBusinessId = (String) execution.getVariable("objectBusinessId");
		if(!Util._IN_PRODUCTION){msg = "----[objectBusinessId('"+objectBusinessId+"') ]----";LOG.info(String.format(fmt, _f,msg));}
		String objectId = (String) execution.getVariable("objectId");
		if(!Util._IN_PRODUCTION){msg = "----[objectId('"+objectId+"') ]----";LOG.info(String.format(fmt, _f,msg));}
		String objectType = (String) execution.getVariable("objectType");
		if(!Util._IN_PRODUCTION){msg = "----[objectType('"+objectType+"') ]----";LOG.info(String.format(fmt, _f,msg));}
		
		String objectPrincipal = (String) execution.getVariable("objectPrincipal");
		if(!Util._IN_PRODUCTION){msg = "----[objectPrincipal('"+objectPrincipal+"') ]----";LOG.info(String.format(fmt, _f,msg));}
		
		String objectStatus = (String) execution.getVariable("objectStatus");
		if(!Util._IN_PRODUCTION){msg = "----[objectStatus('"+objectStatus+"')]----";LOG.info(String.format(fmt, _f,msg));}
		String processName = (String) getProcessName().getValue(execution);
		if(!Util._IN_PRODUCTION){msg = "----[processName('"+processName+"') ]----";LOG.info(String.format(fmt, _f,msg));}
		String processInstanceId = ((ExecutionEntity) execution).getProcessInstanceId();
		if(!Util._IN_PRODUCTION){msg = "----[processInstanceId('"+processInstanceId+"')]----";LOG.info(String.format(fmt, _f,msg));}
		String businessKey = ((ExecutionEntity) execution).getBusinessKey();
		if(!Util._IN_PRODUCTION){msg = "----[businessKey('"+businessKey+"')]----";LOG.info(String.format(fmt, _f,msg));}
		String tenantId = ((ExecutionEntity) execution).getTenantId();
		if(!Util._IN_PRODUCTION){msg = "----[tenantId('"+tenantId+"')]----";LOG.info(String.format(fmt, _f,msg));}
		String caseInstanceId = ((ExecutionEntity) execution).getCaseInstanceId();
		if(!Util._IN_PRODUCTION){msg = "----[caseInstanceId('"+caseInstanceId+"')]----";LOG.info(String.format(fmt, _f,msg));}
		String definitionId = ((ExecutionEntity) execution).getProcessDefinitionId();
		if(!Util._IN_PRODUCTION){msg = "----[definitionId('"+definitionId+"')]----";LOG.info(String.format(fmt, _f,msg));}
		boolean suspended = ((ExecutionEntity) execution).isSuspended();
		if(!Util._IN_PRODUCTION){msg = "----[suspended('"+suspended+"')]----";LOG.info(String.format(fmt, _f,msg));}
		boolean ended = ((ExecutionEntity) execution).isEnded();
		if(!Util._IN_PRODUCTION){msg = "----[ended('"+ended+"')]----";LOG.info(String.format(fmt, _f,msg));}
		String executionId = ((ExecutionEntity) execution).getId();
		//TODO: handle all process types defined int ProcessType
		Process<Order> process = new Process<Order>(executionId, processInstanceId, businessKey, processName, definitionId,tenantId, caseInstanceId,ended,suspended,Process.ProcessType.production_process);
		process.setBusinessId(objectBusinessId);
		
		ProcessRest.instance().save(process, !Util._IN_PRODUCTION);
		
		OrderRest.instance().addProcessReference(objectBusinessId, processInstanceId, businessKey, !Util._IN_PRODUCTION);

		if(log && !Util._IN_PRODUCTION) {
			String time = "[ExecutionTime:"+(System.currentTimeMillis()-startTime)+")]====";
			msg = "====[_execute completed.]====";LOG.info(String.format(fmt,("<<<<<<<<<"+_f).toUpperCase(),msg+time));
		}
		
	}

}
