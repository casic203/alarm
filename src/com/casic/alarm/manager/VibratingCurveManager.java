package com.casic.alarm.manager;

import org.springframework.stereotype.Service;

import com.casic.alarm.domain.VibratingCurve;
import com.casic.core.hibernate.HibernateEntityDao;

@Service
public class VibratingCurveManager extends HibernateEntityDao<VibratingCurve> {
}
