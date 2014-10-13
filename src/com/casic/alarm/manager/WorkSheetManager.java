package com.casic.alarm.manager;

import org.springframework.stereotype.Service;

import com.casic.alarm.domain.WorkSheet;
import com.casic.core.hibernate.HibernateEntityDao;

@Service
public class WorkSheetManager extends HibernateEntityDao<WorkSheet> {

}
