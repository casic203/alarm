package com.casic.alarm.manager;

import org.springframework.stereotype.Service;

import com.casic.alarm.domain.AlarmRecord;
import com.casic.core.hibernate.HibernateEntityDao;

@Service
public class AlarmRecordManager extends HibernateEntityDao<AlarmRecord> {

}
