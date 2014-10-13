package com.casic.alarm.manager;

import org.springframework.stereotype.Service;

import com.casic.alarm.domain.SysLog;
import com.casic.core.hibernate.HibernateEntityDao;

@Service
public class SysLogManager extends HibernateEntityDao<SysLog> {

}
