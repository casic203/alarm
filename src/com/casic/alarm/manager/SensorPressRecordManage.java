package com.casic.alarm.manager;

import org.springframework.stereotype.Service;

import com.casic.alarm.domain.SensorPressRecord;
import com.casic.core.hibernate.HibernateEntityDao;

@Service
public class SensorPressRecordManage extends HibernateEntityDao<SensorPressRecord> {

}
