package com.ws.service.interceptors;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.io.CacheAndWriteOutputStream;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.io.CachedOutputStreamCallback;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.log4j.Logger;

public class LoggingOutInterceptor extends
		AbstractPhaseInterceptor<SoapMessage> {

	private Logger logger = Logger.getLogger(LoggingOutInterceptor.class);

	public LoggingOutInterceptor() {
		super(Phase.PRE_STREAM);
	}

	@Override
	public void handleMessage(SoapMessage message) {
		String logID = "";
		try {
			// 获取之前声明的日志ID标识，单次请求输出的所有日志标识相同
			Object obj = message.getExchange().get("LOG.SEQUENCE");
			if (obj != null){
				logID = obj.toString();
			}
			OutputStream os = message.getContent(OutputStream.class);
			CacheAndWriteOutputStream cwos = new CacheAndWriteOutputStream(os);
			message.setContent(OutputStream.class, cwos);
			cwos.registerCallback(new LoggingOutCallBack(logID));
		} catch (Exception e) {
			logger.error("CXF记录日志错误(" + logID + "): ", e);
		}
	}

	class LoggingOutCallBack implements CachedOutputStreamCallback {
		private String logID;

		public LoggingOutCallBack(String logID) {
			// this.logID = logID;
		}

		@Override
		public void onClose(CachedOutputStream cos) {
			try {
				if (cos != null) {
					String xml = IOUtils.toString(cos.getInputStream());
					if (xml == null || xml.equals(""))
						return;
					logger.info("------发出的Soap报文(" + logID + ")" + xml);
				}
			} catch (Exception e) {
				logger.error("CXF记录日志错误(" + logID + "): ", e);
			}

			try {
				cos.lockOutputStream();
				cos.resetOut(null, false);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void onFlush(CachedOutputStream arg0) {
		}
	}
}