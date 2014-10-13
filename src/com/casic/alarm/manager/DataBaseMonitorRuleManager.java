package com.casic.alarm.manager;

import org.springframework.stereotype.Service;

import com.casic.alarm.domain.DataBaseMonitorRule;
import com.casic.core.hibernate.HibernateEntityDao;

@Service
public class DataBaseMonitorRuleManager extends
		HibernateEntityDao<DataBaseMonitorRule> {

}
