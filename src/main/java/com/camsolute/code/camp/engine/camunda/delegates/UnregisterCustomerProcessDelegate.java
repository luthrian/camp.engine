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

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;

import com.camsolute.code.camp.core.rest.process.OrderProcessRestDao;
import com.camsolute.code.camp.models.business.OrderProcess;


public class UnregisterCustomerProcessDelegate implements JavaDelegate {
	public static final String _F = "["+UnregisterCustomerProcessDelegate.class.getSimpleName()+"]";
	public static final boolean _DEBUG = true;
	private static String fmt = "[%15s] [%s]";
	public static final boolean _IN_PRODUCTION = false;
    private static final Logger LOG = LogManager.getLogger("RegisterCustomerProcessDelegate");
	
    private Expression processName;

    
	public void execute(DelegateExecution execution) throws Exception {
		long startTime = System.currentTimeMillis();
		String _f = null;
		String msg = null;
		if(_DEBUG) {
			_f = "[execute]";
			msg = "====[ camunda pe: unregistering customer order process. ]====";LOG.info(String.format(fmt,(_f+">>>>>>>>>").toUpperCase(),msg));
		}
		
		String orderNumber = (String) execution.getVariable("orderNumber");
		if(_DEBUG){msg = "----[orderNumber('"+orderNumber+"') ]----";LOG.info(String.format(fmt, _f,msg));}
		
		String orderId = String.valueOf((Integer) execution.getVariable("orderId"));
		if(_DEBUG){msg = "----[orderId('"+orderId+"') ]----";LOG.info(String.format(fmt, _f,msg));}
		
		String orderStatus = (String) execution.getVariable("orderStatus");
		if(_DEBUG){msg = "----[orderStatus('"+orderStatus+"')]----";LOG.info(String.format(fmt, _f,msg));}
		
		String processName = ((ExecutionEntity) execution).getActivity().getProcessDefinition().getProcessDefinition().getName();
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
		
		if(_DEBUG){msg = "----[creating OrderProcess instance '"+processInstanceId+"']----";LOG.info(String.format(fmt, _f,msg));}
		OrderProcess process = null;
		try {
			process = new OrderProcess(processInstanceId, businessKey, processName, definitionId,tenantId, caseInstanceId,ended,suspended);
		}catch(Exception e) {
			if(_DEBUG){msg = "----[EXCEPTION:'"+e.getMessage()+"']----";LOG.info(String.format(fmt, _f,msg));}
			e.printStackTrace();
		}
		if(_DEBUG){msg = "----[created OrderProcess instance '"+processInstanceId+"']----";LOG.info(String.format(fmt, _f,msg));}
		
		if(_DEBUG){msg = "----[Executing unregister order process Rest Service Call]----";LOG.info(String.format(fmt, _f,msg));}
		OrderProcessRestDao.instance().unregister(orderNumber, process);
		
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

}
