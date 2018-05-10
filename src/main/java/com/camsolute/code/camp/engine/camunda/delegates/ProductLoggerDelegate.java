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

import com.camsolute.code.camp.lib.models.product.ProductDao;
import com.camsolute.code.camp.lib.utilities.LoggerDao;
import com.camsolute.code.camp.lib.utilities.Util;

public class ProductLoggerDelegate extends LoggerDelegate {
	private static final Logger LOG = LogManager.getLogger(ProductLoggerDelegate.class);
	private static String fmt = "[%15s] [%s]";
	public static final String _F = "["+ProductLoggerDelegate.class.getSimpleName()+"]";
	
	public void logObject(String objectBusinessId)  { _logObject(objectBusinessId,!Util._IN_PRODUCTION);}
	public void _logObject(String objectBusinessId,boolean log){
		long startTime = System.currentTimeMillis();
		String _f = null;
		String msg = null;
		if(log) {
			_f = "[_log]";
			msg = "====[ Logging order object('"+objectBusinessId+"') ... ]====";LOG.traceEntry(String.format(fmt,_f,msg));
		}
		LoggerDao.instance().log(
				ProductDao.instance().loadByBusinessId(objectBusinessId, log)
				,log);
		if(log) {
			String time = "[ExecutionTime:"+(System.currentTimeMillis()-startTime)+")]====";
			msg = "====[_logObject completed.]====";LOG.info(String.format(fmt,("<<<<<<<<<"+_f).toUpperCase(),msg+time));
		}
		
	}

}
