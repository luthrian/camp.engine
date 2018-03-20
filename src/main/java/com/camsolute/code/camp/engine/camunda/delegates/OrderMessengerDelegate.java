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

import com.camsolute.code.camp.lib.utilities.Util;

public class OrderMessengerDelegate implements JavaDelegate {
	public static final String _F = "["+OrderMessengerDelegate.class.getSimpleName()+"]";
	private static final Logger LOG = LogManager.getLogger(OrderMessengerDelegate.class);
	private static String fmt = "[%15s] [%s]";
	
	
	private Expression messageName;
	private Expression businessKey;
	private Expression processName;

	public Expression getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(Expression businessKey) {
		this.businessKey = businessKey;
	}

	public Expression getMessage() {
		return messageName;
	}

	public void setMessage(Expression message) {
		this.messageName = message;
	}

	public Expression getProcessName() {
		return processName;
	}

	public void setProcessName(Expression processName) {
		this.processName = processName;
	}

	public void execute(DelegateExecution execution) throws Exception {
		long startTime = System.currentTimeMillis();
		String messageName = (String) this.messageName.getValue(execution);
		String processName = (String) this.processName.getValue(execution);
		String _f = null;
		String msg = null;
		if(!Util._IN_PRODUCTION) {
			_f = "[_execute]";
			msg = "====[ handling order message '"+messageName+"' from process '"+processName+"']====";LOG.traceEntry(String.format(fmt,(_f+">>>>>>>>>").toUpperCase(),msg));
		}

		String objectBusinessId = (String) execution.getVariable("objectBusinessId");
		String objectId = (String) execution.getVariable("objectId");
		String objectStatus = (String) execution.getVariable("objectStatus");
		String objectType = (String) execution.getVariable("objectType");
		String objectPrincipal = (String) execution.getVariable("objectPrincipal");
		String businessKey = ((ExecutionEntity) execution).getBusinessKey();
		String processInstanceId = ((ExecutionEntity) execution).getProcessInstanceId();
		
		if(!Util._IN_PRODUCTION){msg = "----[objectBusinessId '"+objectBusinessId+"']----";LOG.info(String.format(fmt, _f,msg));}
		if(!Util._IN_PRODUCTION){msg = "----[objectId '"+objectId+"']----";LOG.info(String.format(fmt, _f,msg));}
		if(!Util._IN_PRODUCTION){msg = "----[objectStatus '"+objectStatus+"']----";LOG.info(String.format(fmt, _f,msg));}
		if(!Util._IN_PRODUCTION){msg = "----[objectType '"+objectType+"']----";LOG.info(String.format(fmt, _f,msg));}
		if(!Util._IN_PRODUCTION){msg = "----[objectPrincipal '"+objectPrincipal+"']----";LOG.info(String.format(fmt, _f,msg));}
		if(!Util._IN_PRODUCTION){msg = "----[messageName '"+messageName+"']----";LOG.info(String.format(fmt, _f,msg));}
		if(!Util._IN_PRODUCTION){msg = "----[businessKey '"+businessKey+"']----";LOG.info(String.format(fmt, _f,msg));}
		if(!Util._IN_PRODUCTION){msg = "----[processName '"+processName+"']----";LOG.info(String.format(fmt, _f,msg));}
		if(!Util._IN_PRODUCTION){msg = "----[processInstanceId '"+processInstanceId+"']----";LOG.info(String.format(fmt, _f,msg));}
		
		
		execution.getProcessEngineServices().getRuntimeService().createMessageCorrelation(messageName)
		    .processInstanceVariableEquals("objectBusinessId", objectBusinessId)
		    .processInstanceVariableEquals("objectId", objectId)
			.setVariable("objectBusinessId", objectBusinessId)
			.setVariable("objectId",objectId)
			.setVariable("objectStatus", objectStatus)
			.setVariable("objectType",objectType)
			.setVariable("objectPrincipal", objectPrincipal)
			.processInstanceBusinessKey(businessKey)
			.correlateWithResult();
		
		/*
		 * This would also work....
		 */
/*		
		HashMap<String,VariableValue> correlationKeys = new HashMap<String,VariableValue>();
		correlationKeys.put("objectBusinessId", new VariableValue(objectBusinessId, "String"));
		correlationKeys.put("objectId", new VariableValue(objectId, "String"));
		

		HashMap<String,VariableValue> processVariables = new HashMap<String,VariableValue>();
		processVariables.put("objectBusinessId", new VariableValue(objectBusinessId, "String"));
		processVariables.put("objectId", new VariableValue(objectId, "String"));
		processVariables.put("objectStatus", new VariableValue(objectStatus, "String"));

		HashMap<String,VariableValue> correlationKeys = new HashMap<String,VariableValue>();
		correlationKeys.put("objectBusinessId", new VariableValue(objectBusinessId, "String"));
		correlationKeys.put("objectId", new VariableValue(objectId, "String"));
		

		HashMap<String,VariableValue> processVariables = new HashMap<String,VariableValue>();
		processVariables.put("objectBusinessId", new VariableValue(objectBusinessId, "String"));
		processVariables.put("objectId", new VariableValue(objectId, "String"));
		processVariables.put("objectStatus", new VariableValue(objectStatus, "String"));

		.processInstanceId(processInstanceId)
		execution.getProcessEngineServices().getRuntimeService().correlateMessage(messageName, businessKey, processVariables);
*/	
		if(!Util._IN_PRODUCTION) {
			String time = "[ExecutionTime:"+(System.currentTimeMillis()-startTime)+")]====";
			msg = "====[_execute completed.]====";LOG.info(String.format(fmt,("<<<<<<<<<"+_f).toUpperCase(),msg+time));
		}
		
	}

}
