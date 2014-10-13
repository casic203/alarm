package com.casic.alarm.manager;

import org.springframework.stereotype.Service;
import com.casic.alarm.domain.SensorFlowRecord;
import com.casic.core.hibernate.HibernateEntityDao;

@Service
public class SensorFlowRecordManager extends HibernateEntityDao<SensorFlowRecord> {

}
