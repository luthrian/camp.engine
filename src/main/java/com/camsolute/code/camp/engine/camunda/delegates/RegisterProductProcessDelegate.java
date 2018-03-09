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

import java.text.SimpleDateFormat;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;

import com.camsolute.code.camp.core.rest.order.OrderRestDao;
import com.camsolute.code.camp.core.rest.process.OrderProcessRestDao;
import com.camsolute.code.camp.models.business.Order;
import com.camsolute.code.camp.models.business.OrderProcess;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;


public class RegisterProductProcessDelegate implements JavaDelegate {
//    private ObjectMapper mapper;
//    private PostRestClient postClient;
//    private GetRestClient getClient;
//    private String json;
//    private String result;
    private Expression processName;
    private Expression targetStatus;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
//		PostRestClient client = new PostRestClient();
		
		String orderNumber = (String) execution.getVariable("orderNumber");
		String orderId = (String) execution.getVariable("orderId");
		String orderStatus = (String) execution.getVariable("orderStatus");
		String processName = (String) getProcessName().getValue(execution);
//		String activityId = ((ExecutionEntity) execution).getActivityId();
		String processInstanceId = ((ExecutionEntity) execution).getProcessInstanceId();
		String businessKey = ((ExecutionEntity) execution).getBusinessKey();
		String tenantId = ((ExecutionEntity) execution).getTenantId();
		String caseInstanceId = ((ExecutionEntity) execution).getCaseInstanceId();
		String definitionId = ((ExecutionEntity) execution).getProcessDefinitionId();
		boolean suspended = ((ExecutionEntity) execution).isSuspended();
		boolean ended = ((ExecutionEntity) execution).isEnded();
		OrderProcess process = new OrderProcess(processInstanceId, businessKey, processName, definitionId,tenantId, caseInstanceId,ended,suspended);
		
//		ProductProcessRestDao.instance().registerProcess(orderNumber, process);
	
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
