package com.casic.alarm.manager;
 

import org.springframework.stereotype.Service;
 
import com.casic.alarm.domain.DeviceSensor; 
import com.casic.core.hibernate.HibernateEntityDao;

@Service
public class DeviceSensorManager extends HibernateEntityDao<DeviceSensor>{

}
