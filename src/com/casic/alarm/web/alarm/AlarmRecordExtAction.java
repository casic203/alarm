package com.casic.alarm.web.alarm;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRAbstractBeanDataSource;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Results;
import com.casic.alarm.form.AlarmRecordForm;
import com.casic.alarm.manager.AlarmRecordManager;
import com.casic.core.json.JSONTool;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

/**
 * 报警记录Action
 * @author liuxin
 *
 */
@Results({@org.apache.struts2.convention.annotation.Result(name = "reload", location = "alarm-record.do", type = "redirect"),
		@org.apache.struts2.convention.annotation.Result(name = "successExport", location = "/jasper/alarm_record_template.jasper", type = "jasper", params = {"dataSource", "alarmRecordReportList", "format", "HTML"})
})
public class AlarmRecordExtAction implements ModelDriven<AlarmRecordForm>, Preparable {
	
	/**
	 * 报警记录Manager
	 */
	private AlarmRecordManager alarmRecordManager;
	
	private long id;
	
	/**
	 * 报警记录请求参数表单
	 */
	private AlarmRecordForm model = new AlarmRecordForm();;
	
	/**
	 * 报表jrxml文件相对路径
	 */
	private static final String ALARM_RECORD_JRXML_PATH = "/jasper/alarm_record_template.jrxml"; 
	
	/**
	 * 报表jasper文件相对路径
	 */
	private static final String ALARM_RECORD_JASPER_PATH = "/jasper/alarm_record_template.jasper"; 
	
	/**
	 * 报表jasper文件名
	 */
	private static final String ALARM_RECORD_JASPER_FILENAME = "alarm_record_template.jasper"; 
	
	/**
	 * 报警记录查询结果
	 */
	private List<AlarmRecordReport> alarmRecordReportList;
	
	private SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
	
	public AlarmRecordManager getAlarmRecordManager() {
		return alarmRecordManager;
	}

	@Resource
	public void setAlarmRecordManager(AlarmRecordManager alarmRecordManager) {
		this.alarmRecordManager = alarmRecordManager;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public void prepare() throws Exception {
	}

	public AlarmRecordForm getModel() {
		return this.model;
	}
	
	public void setModel(AlarmRecordForm model) {
		this.model = model;
	}
	
	public List<AlarmRecordReport> getAlarmRecordReportList() {
		return alarmRecordReportList;
	}

	public void setAlarmRecordReportList(
			List<AlarmRecordReport> alarmRecordReportList) {
		this.alarmRecordReportList = alarmRecordReportList;
	}

	/**
	 * 生成报警记录报表
	 */
	@SuppressWarnings("unchecked")
	public String generatorReport() {
		HttpServletRequest request = null;
		String beginDate = model.alarmBeginDateStr;
		String endDate   = model.alarmEndDateStr;
		JRHtmlExporter htmlExporter = null;
		Map<String, String> parameters = null;;
		JasperReport jasperReport = null;
		JRAlarmRecordDataSource mJRDataSource = null;
		JasperPrint jasperPrint = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		/**
		 * 根据开始日期和结束日期查询报警记录
		 */
		alarmRecordReportList = new ArrayList<AlarmRecordReport>();
		String hql = "select device.devName, device.outDate, device.installDate, device.factory, count(device.id), device.latitude, device.longtitude, device.height" +
				" from AlarmRecord aRecord, Device device" +
				" where aRecord.device.id = device.id and aRecord.recordDate >= :beginDate and aRecord.recordDate <= :endDate" +
				" group by device.id, device.devName, device.outDate, device.installDate, device.factory, device.latitude, device.longtitude, device.height";
		Map<String, Object> paraMap = new HashMap<String, Object>();
		try {
			paraMap.put("beginDate", dateFormat.parse(beginDate));
			paraMap.put("endDate", dateFormat.parse(endDate));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		List<Object[]> aResultList = alarmRecordManager.find(hql, paraMap);
		if(aResultList == null || aResultList.size() == 0) {
			try {
				JSONTool.writeMsgResult(false, "没有记录");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		Long alarmSumNum = 0l;
		for (Object[] objects : aResultList) {
			String deviceName     = (String)objects[0];
			String outDate = "", installDate = "";
			if(null != objects[1]) {
			    outDate = simpleDateFormat.format((Date)objects[1]);
			}
			if(null != objects[2]) {
			    installDate = simpleDateFormat.format((Date)objects[2]);
			}
			String factory        = (String)objects[3];
			long alarmNumber      = (Long)objects[4];
			String latitude       = (String)objects[5];
			String longtitude     = (String)objects[6];
			String height         = (String)objects[7];;
			alarmSumNum += alarmNumber;
			AlarmRecordReport alarmRecordReport = new AlarmRecordReport(deviceName, outDate, installDate, 
					factory, alarmNumber, beginDate,
					endDate, latitude, longtitude,
					height);
			alarmRecordReportList.add(alarmRecordReport);
		}
		
		/**
		 * 将.jrxml模板文件编译成为.jasper文件
		 */
		request = ServletActionContext.getRequest();
        String reportSource;
        reportSource = ServletActionContext.getServletContext()
                .getRealPath(ALARM_RECORD_JRXML_PATH);
        File parent = new File(reportSource).getParentFile();
        try {
			JasperCompileManager.compileReportToFile(reportSource, new File(
			        parent, ALARM_RECORD_JASPER_FILENAME)
			        .getAbsolutePath());
		} catch (JRException e) {
			e.printStackTrace();
			return null;
		}
        
		/**
		 * 加载.jasper文件并导出报表
		 */
        parameters = new HashMap<String, String>();
        File file = new File(ServletActionContext.getServletContext().getRealPath(ALARM_RECORD_JASPER_PATH));
        htmlExporter = new JRHtmlExporter();
        try {
	         jasperReport = (JasperReport)JRLoader.loadObject(file.getPath());
	         mJRDataSource = new JRAlarmRecordDataSource(false, alarmRecordReportList, alarmSumNum);
	         jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, mJRDataSource);
	         
	         htmlExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
	         StringBuffer stringBuffer = new StringBuffer();
	         htmlExporter.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER, stringBuffer);
	         //htmlExporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE);
//	         htmlExporter.setParameter(JRHtmlExporterParameter.HTML_HEADER, "qqqq");
	         htmlExporter.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML, "");
	         File imageDir = new File(ServletActionContext.getServletContext().getRealPath("/images"));
	         htmlExporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, request.getContextPath() + "/images/");
	         htmlExporter.setParameter(JRHtmlExporterParameter.IMAGES_DIR, imageDir);
	         htmlExporter.setParameter(JRHtmlExporterParameter.IS_OUTPUT_IMAGES_TO_DIR, Boolean.TRUE);
	         htmlExporter.setParameter(JRHtmlExporterParameter.HTML_FOOTER, "");
	         htmlExporter.exportReport();
		} catch (JRException e) {
			e.printStackTrace();
			return null;
		}
        return "successExport";
	}
	
	public String execute() {
		return "success";
	}
}

/**
 * 
 * 报表数据源类
 * 
 * @author liuxin
 *
 */
class JRAlarmRecordDataSource extends JRAbstractBeanDataSource {

	/**
	 * 数据源数据集合
	 */
	private Iterator<AlarmRecordReport> alarmRecordReportIterator;
	
	/**
	 * 报警总数
	 */
	private Long alarmSumNum;
	
	/**
	 * 当前数据实体
	 */
	private AlarmRecordReport curAlarmRecordReport;
	
	public JRAlarmRecordDataSource(boolean isUseFieldDescription, List<AlarmRecordReport> aRecordReportList, Long alarmSumNum) {
		super(isUseFieldDescription);
		this.alarmRecordReportIterator = aRecordReportList.iterator();
		this.alarmSumNum = alarmSumNum;
	}

	public void moveFirst() throws JRException {
		
	}

	public Object getFieldValue(JRField jrField) throws JRException {
		try {
			String fieldName = jrField.getName();
			if("deviceName".equals(fieldName)) {
				return curAlarmRecordReport.getDeviceName();
			} else if ("outDate".equals(fieldName)) {
				return curAlarmRecordReport.getOutDate();
			} else if ("installDate".equals(fieldName)) {
				return curAlarmRecordReport.getInstallDate();
			} else if ("factory".equals(fieldName)) {
				return curAlarmRecordReport.getFactory();
			} else if ("alarmNumber".equals(fieldName)) {
				return curAlarmRecordReport.getAlarmNumber();
			} else if ("reportBeginDate".equals(fieldName)) {
				return curAlarmRecordReport.getReportBeginDate();
			} else if ("reportEndDate".equals(fieldName)) {
				return curAlarmRecordReport.getReportEndDate();
			} else if ("alarmSumNum".equals(fieldName)) {
				return this.alarmSumNum;
			} else if ("alarmNumPercent".equals(fieldName)) {
				double percentFloat = (double)curAlarmRecordReport.getAlarmNumber()/(double)alarmSumNum;
				String percentFloatStr = percentFloat + "";
				DecimalFormat decimalFormat = new DecimalFormat("##.##");
				percentFloatStr = decimalFormat.format(percentFloat);
//				percentFloatStr = percentFloatStr.substring(0, percentFloatStr.lastIndexOf(".") + 4);
				return "%" + Float.parseFloat(percentFloatStr)*100;
			} else if ("latitude".equals(fieldName)) {
				return curAlarmRecordReport.getLatitude();
			} else if ("longtitude".equals(fieldName)) {
				return curAlarmRecordReport.getLongtitude();
			} else if ("height".equals(fieldName)) {
				return curAlarmRecordReport.getHeight();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean next() throws JRException {
		if(alarmRecordReportIterator.hasNext()) {
			AlarmRecordReport aRecordReport = alarmRecordReportIterator.next();
	  	    if(aRecordReport != null) {
	  	    	curAlarmRecordReport = aRecordReport;
		        return true;
		    }
		}
		return false;
	}
}

/**
 * 
 * 报表实体类
 * @author liuxin
 *
 */
class AlarmRecordReport {
	
	/**
	 * 设备名
	 */
	private String deviceName;
	
	/**
	 * 出厂日期
	 */
	private String outDate;
	
	/**
	 * 安装日期
	 */
	private String installDate;
	
	/**
	 * 工厂
	 */
	private String factory;
	
	/**
	 * 报警次数
	*/
	private long alarmNumber;
	
	/**
	 * 记录开始时间
	 */
	private String reportBeginDate;
	
	/**
	 * 记录结束时间
	 */
	private String reportEndDate;
	
	/**
	 * 经度
	 */
	private String latitude;
	
	/**
	 * 纬度
	 */
	private String longtitude;
	
	/**
	 * 高度
	 */
	private String height;
	
	public AlarmRecordReport(String deviceName, String outDate,
			String installDate, String factory, long alarmNumber, String reportBeginDate,
			String reportEndDate, String latitude, String longtitude, String height) {
		super();
		this.deviceName = deviceName;
		this.outDate = outDate;
		this.installDate = installDate;
		this.factory = factory;
		this.alarmNumber = alarmNumber;
		this.reportBeginDate = reportBeginDate;
		this.reportEndDate = reportEndDate;
		this.latitude = latitude;
		this.longtitude = longtitude;
		this.height = height;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getOutDate() {
		return outDate;
	}

	public void setOutDate(String outDate) {
		this.outDate = outDate;
	}

	public String getInstallDate() {
		return installDate;
	}

	public void setInstallDate(String installDate) {
		this.installDate = installDate;
	}

	public String getFactory() {
		return factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

	public long getAlarmNumber() {
		return alarmNumber;
	}

	public void setAlarmNumber(long alarmNumber) {
		this.alarmNumber = alarmNumber;
	}

	public String getReportBeginDate() {
		return reportBeginDate;
	}

	public void setReportBeginDate(String reportBeginDate) {
		this.reportBeginDate = reportBeginDate;
	}

	public String getReportEndDate() {
		return reportEndDate;
	}

	public void setReportEndDate(String reportEndDate) {
		this.reportEndDate = reportEndDate;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(String longtitude) {
		this.longtitude = longtitude;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}
}
