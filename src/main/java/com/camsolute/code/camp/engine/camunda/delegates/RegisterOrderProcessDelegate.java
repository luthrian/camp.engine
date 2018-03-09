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

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
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
import com.camsolute.code.camp.lib.models.product.Product;
import com.camsolute.code.camp.lib.models.product.ProductRest;
import com.camsolute.code.camp.lib.utilities.Util;


public class RegisterOrderProcessDelegate implements JavaDelegate {
	public static final String _F = "["+RegisterOrderProcessDelegate.class.getSimpleName()+"]";
	public static final boolean _DEBUG = true;
	private static String fmt = "[%15s] [%s]";
	public static final boolean _IN_PRODUCTION = false;
    private static final Logger LOG = LogManager.getLogger("RegisterOrderProcessDelegate");
	
    private Expression processName;

    private Expression targetStatus;

    
	public void execute(DelegateExecution execution) throws Exception {
//		LOG.trace("trace");LOG.debug("debug");LOG.info("info");LOG.warn("warn");LOG.error("error");LOG.fatal("fatal");
		long startTime = System.currentTimeMillis();
		String _f = null;
		String msg = null;
		if(_DEBUG) {
			_f = "[RegisterOrderProcessDelegate]";
			msg = "====[ register order process ]====";LOG.traceEntry(String.format(fmt,(_f+">>>>>>>>>").toUpperCase(),msg));
		}

		String objectBusinessId = (String) execution.getVariable("objectBusinessId");
		if(_DEBUG){msg = "----[objectBusinessId('"+objectBusinessId+"') ]----";LOG.info(String.format(fmt, _f,msg));}
		String objectId = (String) execution.getVariable("objectId");
		if(_DEBUG){msg = "----[objectId('"+objectId+"') ]----";LOG.info(String.format(fmt, _f,msg));}
		String objectStatus = (String) execution.getVariable("objectStatus");
		if(_DEBUG){msg = "----[objectStatus('"+objectStatus+"')]----";LOG.info(String.format(fmt, _f,msg));}
		String processName = (String) getProcessName().getValue(execution);
		if(_DEBUG){msg = "----[processName('"+processName+"') ]----";LOG.info(String.format(fmt, _f,msg));}
		String processInstanceId = ((ExecutionEntity) execution).getProcessInstanceId();
		if(_DEBUG){msg = "----[processInstanceId('"+processInstanceId+"')]----";LOG.info(String.format(fmt, _f,msg));}
		String businessKey = ((ExecutionEntity) execution).getBusinessKey();
		if(_DEBUG){msg = "----[businessKey('"+businessKey+"')]----";LOG.info(String.format(fmt, _f,msg));}
		String tenantId = ((ExecutionEntity) execution).getTenantId();
		if(_DEBUG){msg = "----[tenantId('"+tenantId+"')]----";LOG.info(String.format(fmt, _f,msg));}
		String caseInstanceId = ((ExecutionEntity) execution).getCaseInstanceId();
		if(_DEBUG){msg = "----[caseInstanceId('"+caseInstanceId+"')]----";LOG.info(String.format(fmt, _f,msg));}
		String definitionId = ((ExecutionEntity) execution).getProcessDefinitionId();
		if(_DEBUG){msg = "----[definitionId('"+definitionId+"')]----";LOG.info(String.format(fmt, _f,msg));}
		boolean suspended = ((ExecutionEntity) execution).isSuspended();
		if(_DEBUG){msg = "----[suspended('"+suspended+"')]----";LOG.info(String.format(fmt, _f,msg));}
		boolean ended = ((ExecutionEntity) execution).isEnded();
		if(_DEBUG){msg = "----[ended('"+ended+"')]----";LOG.info(String.format(fmt, _f,msg));}
		String executionId = ((ExecutionEntity) execution).getId();
		
		Process<Order,OrderProcess> process = new Process<Order,OrderProcess>(executionId, processInstanceId, businessKey, processName, definitionId,tenantId, caseInstanceId,ended,suspended,Process.ProcessType.product_process);
		
		ProcessRest.instance().save(process, !Util._IN_PRODUCTION);
		
		OrderRest.instance().addProcessReference(objectBusinessId, processInstanceId, businessKey, !Util._IN_PRODUCTION);
				
		if(_DEBUG) {
			String time = "[ExecutionTime:"+(System.currentTimeMillis()-startTime)+")]====";
			msg = "====[execute completed.]====";LOG.info(String.format(fmt,("<<<<<<<<<"+_f).toUpperCase(),msg+time));
		}
		
	}

	public Expression getProcessName() {
		return processName;
	}

	public void setProcessName(Expression processName) {
		this.processName = processName;
	}

	public Expression getTargetStatus() {
		return targetStatus;
	}

	public void setTargetStatus(Expression targetStatus) {
		this.targetStatus = targetStatus;
	}

}
