package com.casic.alarm.manager;

import org.springframework.stereotype.Service;

import com.casic.alarm.domain.DeviceType;
import com.casic.core.hibernate.HibernateEntityDao;

@Service
public class DeviceTypeManager extends HibernateEntityDao<DeviceType> {

}
