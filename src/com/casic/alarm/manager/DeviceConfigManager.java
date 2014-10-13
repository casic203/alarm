package com.casic.alarm.manager;

import org.springframework.stereotype.Service;

import com.casic.alarm.domain.DeviceConfig; 
import com.casic.core.hibernate.HibernateEntityDao;

@Service
public class DeviceConfigManager extends HibernateEntityDao<DeviceConfig>{

}
