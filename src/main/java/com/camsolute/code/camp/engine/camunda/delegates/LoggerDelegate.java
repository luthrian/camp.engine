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
import org.camunda.bpm.engine.delegate.JavaDelegate;

import com.camsolute.code.camp.lib.utilities.Util;

public class LoggerDelegate implements JavaDelegate {
	private static final Logger LOG = LogManager.getLogger(LoggerDelegate.class);
	private static String fmt = "[%15s] [%s]";
	public static final String _F = "["+LoggerDelegate.class.getSimpleName()+"]";
	
	public void execute(DelegateExecution execution) throws Exception { _execute(execution,true);}
	public void _execute(DelegateExecution execution,boolean log){
		long startTime = System.currentTimeMillis();
		String _f = null;
		String msg = null;
		if(log && !Util._IN_PRODUCTION) {
			_f = "[_execute]";
			msg = "====[ Logger delegate executed ]====";LOG.traceEntry(String.format(fmt,_f,msg));
		}
		
		if(log && !Util._IN_PRODUCTION){msg = "----[processDefinitionId=" + execution.getProcessDefinitionId()+"]----";LOG.info(String.format(fmt, _f,msg));}
		if(log && !Util._IN_PRODUCTION){msg = "----[activtyId=" + execution.getCurrentActivityId()+"]----";LOG.info(String.format(fmt, _f,msg));}
		if(log && !Util._IN_PRODUCTION){msg = "----[activtyName='" + execution.getCurrentActivityName() + "']----";LOG.info(String.format(fmt, _f,msg));}
		if(log && !Util._IN_PRODUCTION){msg = "----[processInstanceId=" + execution.getProcessInstanceId()+"]----";LOG.info(String.format(fmt, _f,msg));}
		if(log && !Util._IN_PRODUCTION){msg = "----[businessKey=" + execution.getProcessBusinessKey()+"]----";LOG.info(String.format(fmt, _f,msg));}
		if(log && !Util._IN_PRODUCTION){msg = "----[executionId=" + execution.getId()+"]----";LOG.info(String.format(fmt, _f,msg));}
		
		if(log && !Util._IN_PRODUCTION) {
			String time = "[ExecutionTime:"+(System.currentTimeMillis()-startTime)+")]====";
			msg = "====[_execute completed.]====";LOG.info(String.format(fmt,("<<<<<<<<<"+_f).toUpperCase(),msg+time));
		}
		
	}

}
