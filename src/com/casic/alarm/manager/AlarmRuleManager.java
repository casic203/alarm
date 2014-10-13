package com.casic.alarm.manager;

import org.springframework.stereotype.Service;

import com.casic.alarm.domain.AlarmRule; 
import com.casic.core.hibernate.HibernateEntityDao;
@Service
public class AlarmRuleManager extends HibernateEntityDao<AlarmRule> {


}
