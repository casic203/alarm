package com.casic.alarm.manager;

import org.springframework.stereotype.Service;

import com.casic.alarm.domain.PositionInfo;
import com.casic.core.hibernate.HibernateEntityDao;

@Service
public class PositionInfoManage extends HibernateEntityDao<PositionInfo> {

}
