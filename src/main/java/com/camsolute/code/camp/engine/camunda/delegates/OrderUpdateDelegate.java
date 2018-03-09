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

import com.camsolute.code.camp.core.U;
import com.camsolute.code.camp.core.rest.order.OrderRestDao;
import com.camsolute.code.camp.models.business.Order;


public class OrderUpdateDelegate implements JavaDelegate {
	public static final boolean _DEBUG = true;
	public static final boolean _IN_PRODUCTION = false;
	private static final Logger LOG = LogManager.getLogger(OrderUpdateDelegate.class);
	private static String fmt = "[%15s] [%s]";
	
    private Expression targetStatus;
    private Expression processName;
    

	public Expression getTargetStatus() {
		return targetStatus;
	}

	public void setTargetStatus(Expression targetStatus) {
		this.targetStatus = targetStatus;
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
		String targetStatus = (String) execution.getVariable("orderStatus");
//		String targetStatus = (String) this.targetStatus.getValue(execution);
		String _f = null;
		String msg = null;
		if(log && _DEBUG) {
			_f = "[_execute]";
			msg = "====[ handling order update event: updating status to '"+targetStatus+"']====";LOG.traceEntry(String.format(fmt,(_f+">>>>>>>>>").toUpperCase(),msg));
		}

		String orderNumber = (String) execution.getVariable("orderNumber");
		String orderId = (String) execution.getVariable("orderId");
		String orderStatus = (String) execution.getVariable("orderStatus");
		String activityId = ((ExecutionEntity) execution).getActivityId();
		String businessKey = ((ExecutionEntity) execution).getBusinessKey();
		String processInstanceId = ((ExecutionEntity) execution).getProcessInstanceId();
		
		
		if(log && _DEBUG){msg = "----[orderNumber '"+orderNumber+"']----";LOG.info(String.format(fmt, _f,msg));}
		if(log && _DEBUG){msg = "----[orderId '"+orderId+"']----";LOG.info(String.format(fmt, _f,msg));}
		if(log && _DEBUG){msg = "----[orderStatus '"+orderStatus+"']----";LOG.info(String.format(fmt, _f,msg));}
		if(log && _DEBUG){msg = "----[businessKey '"+businessKey+"']----";LOG.info(String.format(fmt, _f,msg));}
		if(log && _DEBUG){msg = "----[activityId '"+activityId+"']----";LOG.info(String.format(fmt, _f,msg));}
		if(log && _DEBUG){msg = "----[processInstanceId '"+processInstanceId+"']----";LOG.info(String.format(fmt, _f,msg));}
		if(log && _DEBUG){msg = "----[targetStatus '"+targetStatus+"']----";LOG.info(String.format(fmt, _f,msg));}
		
		Order o = OrderRestDao.instance()._update("status", orderNumber, targetStatus, log);
		//FIXME: TODO: set variable in all relevant order process instances via o.observerProcesses and correlating the appropriate processes
/* something like this but instanceId must be executionId if they are not the same thing
		for(OrderProcess op:o.observerProcesses().toList().value()) {
			if(!op.ended()&&!op.suspended()) {
				execution.getProcessEngineServices().getRuntimeService().setVariable(op.instanceId(), "orderId", String.valueOf(o.id()));
			}
		}
*/

//		execution.setVariable("orderId", String.valueOf(o.id()));
//		if(log && _DEBUG){msg = "----[updated orderId('"+o.id()+"')]----";LOG.info(String.format(fmt, _f,msg));}
//		execution.setVariable("orderStatus", o.status().name());
//		if(log && _DEBUG){msg = "----[updated orderStatus('"+o.status().name()+"')]----";LOG.info(String.format(fmt, _f,msg));}

		
		if(log && _DEBUG) {
			String time = "[ExecutionTime:"+(System.currentTimeMillis()-startTime)+")]====";
			msg = "====[_execute completed.]====";LOG.info(String.format(fmt,("<<<<<<<<<"+_f).toUpperCase(),msg+time));
		}
		
	}

}
