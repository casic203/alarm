package com.casic.alarm.manager;

import org.springframework.stereotype.Service;

import com.casic.alarm.domain.DataBaseOperator;
import com.casic.core.hibernate.HibernateEntityDao;

@Service
public class DataBaseOperatorManager extends HibernateEntityDao<DataBaseOperator> {

}
