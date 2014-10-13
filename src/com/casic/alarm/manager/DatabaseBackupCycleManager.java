package com.casic.alarm.manager;

import org.springframework.stereotype.Service;

import com.casic.alarm.domain.DatabaseBackupCycle;
import com.casic.core.hibernate.HibernateEntityDao;

@Service
public class DatabaseBackupCycleManager extends HibernateEntityDao<DatabaseBackupCycle> {

}
