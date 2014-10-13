package com.casic.alarm.manager;

import org.springframework.stereotype.Service;

import com.casic.alarm.domain.GasStrength;
import com.casic.core.hibernate.HibernateEntityDao;

@Service
public class GasStrengthManager extends HibernateEntityDao<GasStrength> {

}
