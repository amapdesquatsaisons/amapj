/*
 *  Copyright 2013-2014 AmapJ Team
 * 
 *  This file is part of AmapJ.
 *  
 *  AmapJ is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  AmapJ is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with AmapJ.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * 
 */
 package fr.amapj.service.services.notification;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import fr.amapj.service.engine.deamons.DeamonsImpl;
import fr.amapj.service.engine.deamons.DeamonsUtils;


public class NotificationService implements Job
{
	
	public NotificationService()
	{
		
	}
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException
	{
		DeamonsUtils.executeAsDeamon(getClass(), 
		new DeamonsImpl()
		{
			@Override
			public void perform()
			{
				new ProducteurNotificationService().sendProducteurNotification();
			}
		},
		new DeamonsImpl()
		{
			@Override
			public void perform()
			{
				new PermanenceNotificationService().sendPermanenceNotification();
			}
		},
		new DeamonsImpl()
		{
			@Override
			public void perform()
			{
				new PeriodiqueNotificationService().sendPermanenceNotification();
			}
		}
		);
	}
}