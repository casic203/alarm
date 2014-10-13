package com.casic.core.Phone;

public enum WebUrl {
	GBK {
		public String getUrl() {
			return "http://gbk.sms.webchinese.cn";
		}
	},
	UTF {
		public String getUrl() {
			return "http://utf8.sms.webchinese.cn";
		}
	};
	public abstract String getUrl();
}
