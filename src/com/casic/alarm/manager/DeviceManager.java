package com.casic.alarm.manager;

import org.springframework.stereotype.Service;

import com.casic.alarm.domain.Device;
import com.casic.core.hibernate.HibernateEntityDao;

@Service
public class DeviceManager extends HibernateEntityDao<Device> {

}
