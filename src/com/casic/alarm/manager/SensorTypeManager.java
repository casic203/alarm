package com.casic.alarm.manager;
 

import org.springframework.stereotype.Service;
  
import com.casic.alarm.domain.SensorType;
import com.casic.core.hibernate.HibernateEntityDao;

@Service
public class SensorTypeManager extends HibernateEntityDao<SensorType>{

}
