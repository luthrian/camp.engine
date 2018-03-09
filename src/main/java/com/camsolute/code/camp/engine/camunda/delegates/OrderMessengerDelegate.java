/*******************************************************************************
 * Copyright (C) 2017 Christopher Campbell (campbellccc@gmail.com)
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

public class OrderMessengerDelegate implements JavaDelegate {
	public static final String _F = "["+OrderMessengerDelegate.class.getSimpleName()+"]";
	public static final boolean _DEBUG = true;
	public static final boolean _IN_PRODUCTION = false;
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

	public void execute(DelegateExecution execution) throws Exception {_execute(execution,true);}
	
	public void _execute(DelegateExecution execution,boolean log){
		long startTime = System.currentTimeMillis();
		String messageName = (String) this.messageName.getValue(execution);
		String processName = (String) this.processName.getValue(execution);
		String _f = null;
		String msg = null;
		if(log && _DEBUG) {
			_f = "[_execute]";
			msg = "====[ handling order message '"+messageName+"' from process '"+processName+"']====";LOG.traceEntry(String.format(fmt,(_f+">>>>>>>>>").toUpperCase(),msg));
		}

		String objectBusinessId = (String) execution.getVariable("objectBusinessId");
		String objectId = (String) execution.getVariable("objectId");
		String objectStatus = (String) execution.getVariable("objectStatus");
		String businessKey = ((ExecutionEntity) execution).getBusinessKey();
		String processInstanceId = ((ExecutionEntity) execution).getProcessInstanceId();
		
		if(log && _DEBUG){msg = "----[objectBusinessId '"+objectBusinessId+"']----";LOG.info(String.format(fmt, _f,msg));}
		if(log && _DEBUG){msg = "----[objectId '"+objectId+"']----";LOG.info(String.format(fmt, _f,msg));}
		if(log && _DEBUG){msg = "----[objectStatus '"+objectStatus+"']----";LOG.info(String.format(fmt, _f,msg));}
		if(log && _DEBUG){msg = "----[messageName '"+messageName+"']----";LOG.info(String.format(fmt, _f,msg));}
		if(log && _DEBUG){msg = "----[businessKey '"+businessKey+"']----";LOG.info(String.format(fmt, _f,msg));}
		if(log && _DEBUG){msg = "----[processName '"+processName+"']----";LOG.info(String.format(fmt, _f,msg));}
		if(log && _DEBUG){msg = "----[processInstanceId '"+processInstanceId+"']----";LOG.info(String.format(fmt, _f,msg));}
		
		
		execution.getProcessEngineServices().getRuntimeService().createMessageCorrelation(messageName)
		    .processInstanceVariableEquals("objectBusinessId", objectBusinessId)
		    .processInstanceVariableEquals("objectId", objectId)
			.setVariable("objectBusinessId", objectBusinessId)
			.setVariable("objectId",objectId)
			.setVariable("objectStatus", objectStatus)
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
		if(log && _DEBUG) {
			String time = "[ExecutionTime:"+(System.currentTimeMillis()-startTime)+")]====";
			msg = "====[_execute completed.]====";LOG.info(String.format(fmt,("<<<<<<<<<"+_f).toUpperCase(),msg+time));
		}
		
	}

}
