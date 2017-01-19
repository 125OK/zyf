package com.ws.service.interceptors;

import java.util.List;

import javax.xml.soap.SOAPException;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.springframework.cglib.core.Constants;
import org.w3c.dom.Element;
import org.w3c.dom.Node;



public class AuthInterceptors extends AbstractPhaseInterceptor<SoapMessage> {

	public AuthInterceptors() {
		super(Phase.PRE_PROTOCOL);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleMessage(SoapMessage message) throws Fault {
		List<Header> headers= message.getHeaders();
		String user =null,pwd=null;
		if(headers==null){
			SOAPException e=new SOAPException("5001::Authentication failed!");
			throw new Fault(e);
		}else{
			for (Header header : headers) {
				if(header == null){
					continue;
				}else{
					Element element=(Element) header.getObject();
					if(element==null){
							continue;
					}else{
						Node node= element.getFirstChild();
						if(node==null){
							continue;
						}else{
							if(element.getNodeName().equals("user")){
								 user=node.getTextContent();
							}
							if(element.getNodeName().equals("passwd")){
								 pwd=node.getTextContent();
							}
						}
					}
					
				}
			}
			if(user== null || pwd==null){
				SOAPException soapEx = new SOAPException( "5000::Authentication failed!");  
	            throw new Fault(soapEx);  
				
			}
		}
		
	}
	

}
