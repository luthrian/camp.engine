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
		
		String objectBusinessId = (String) execution.getVariable("objectBusinessId");
		String objectId = String.valueOf((Integer) execution.getVariable("objectId"));
		String objectStatus = (String) execution.getVariable("objectStatus");
		String processName = ((ExecutionEntity) execution).getActivity().getProcessDefinition().getProcessDefinition().getName();
		String processInstanceId = ((ExecutionEntity) execution).getProcessInstanceId();
		String businessKey = ((ExecutionEntity) execution).getBusinessKey();
		String tenantId = ((ExecutionEntity) execution).getTenantId();
		String caseInstanceId = ((ExecutionEntity) execution).getCaseInstanceId();
		String definitionId = ((ExecutionEntity) execution).getProcessDefinitionId();
		boolean suspended = ((ExecutionEntity) execution).isSuspended();
		boolean ended = ((ExecutionEntity) execution).isEnded();
		String executionId = ((ExecutionEntity) execution).getId();

		if(_DEBUG){msg = "----[objectBusinessId('"+objectBusinessId+"') ]----";LOG.info(String.format(fmt, _f,msg));}
		if(_DEBUG){msg = "----[objectId('"+objectId+"') ]----";LOG.info(String.format(fmt, _f,msg));}
		if(_DEBUG){msg = "----[objectStatus('"+objectStatus+"')]----";LOG.info(String.format(fmt, _f,msg));}
		if(_DEBUG){msg = "----[processName('"+processName+"') ]----";LOG.info(String.format(fmt, _f,msg));}
		if(_DEBUG){msg = "----[executionId('"+executionId+"')]----";LOG.info(String.format(fmt, _f,msg));}
		if(_DEBUG){msg = "----[processInstanceId('"+processInstanceId+"')]----";LOG.info(String.format(fmt, _f,msg));}
		if(_DEBUG){msg = "----[businessKey('"+businessKey+"')]----";LOG.info(String.format(fmt, _f,msg));}
		if(_DEBUG){msg = "----[tenantId('"+tenantId+"')]----";LOG.info(String.format(fmt, _f,msg));}
		if(_DEBUG){msg = "----[caseInstanceId('"+caseInstanceId+"')]----";LOG.info(String.format(fmt, _f,msg));}
		if(_DEBUG){msg = "----[definitionId('"+definitionId+"')]----";LOG.info(String.format(fmt, _f,msg));}
		if(_DEBUG){msg = "----[suspended('"+suspended+"')]----";LOG.info(String.format(fmt, _f,msg));}
		if(_DEBUG){msg = "----[ended('"+ended+"')]----";LOG.info(String.format(fmt, _f,msg));}
		
		
//	Process<Order,OrderProcess> process = new Process<Order,OrderProcess>(executionId, processInstanceId, businessKey, processName, definitionId,tenantId, caseInstanceId,ended,suspended,Process.ProcessType.product_process);
		
		//TODO: Customer stuff
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
