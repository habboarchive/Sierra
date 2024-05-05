/*
 * Copyright (c) 2012 Quackster <alex.daniel.97@gmail>. 
 * 
 * This file is part of Sierra.
 * 
 * Sierra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Sierra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Sierra.  If not, see <http ://www.gnu.org/licenses/>.
 */

package sierra.storage;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

public class StoragePooling
{
	private BoneCP boneCP;
	private BoneCPConfig boneCPConfig;
	
	public boolean getStoragePooling(Properties config)
	{
		boneCPConfig = new BoneCPConfig();
		boneCPConfig.setJdbcUrl("jdbc:mysql://" + config.getProperty("mysql.host") + "/" + config.getProperty("mysql.database"));
		boneCPConfig.setMinConnectionsPerPartition(0);
		boneCPConfig.setMaxConnectionsPerPartition(5);
		boneCPConfig.setConnectionTimeout(1000, TimeUnit.DAYS);
		boneCPConfig.setPartitionCount(1);
		boneCPConfig.setUsername(config.getProperty("mysql.user"));
		boneCPConfig.setPassword(config.getProperty("mysql.password"));
		
		try 
		{
			boneCP = new BoneCP(boneCPConfig);
		} 
		catch (Exception e) 
		{
			return false;
		}
		return true;
	}

	public BoneCP getBoneCP()
	{
		return boneCP;
	}
}
