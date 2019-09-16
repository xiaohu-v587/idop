package msg;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import javax.xml.namespace.QName;

import com.goodcol.util.AppUtils;

public class SMSServiceSoapService {
	//服务地址
	//public static  String path="http://22.200.142.212:8888/SMSService.asmx?wsdl";
	//public static  String path="http://22.200.135.18:8888/SMSService.asmx";
	public static  String path = AppUtils.findDictRemark("pccm_msg", "1");
	//名字空间 
	//public static  String targetNamespace = "http://tempuri.org/";
	public static  String targetNamespace = AppUtils.findDictRemark("pccm_msg", "2");
	//服务名
	public static  String serName = "SMSService";
	//端口名
	public static  String pName = "SMSServiceSoap";
	
 	 public static void main(String[] args) throws MalformedURLException, RemoteException {
 		SMSServiceSoapService service=new SMSServiceSoapService();
		SMSServiceSoap  serviceSoap=service.getSMSServiceSoap();
		 System.out.println(serviceSoap.smsAdd("000001", "yihui@piservice1.1",
				"18852575640", "024523",
				"ke", "hello world", "1",
				"对公开户")); 
		
		System.out.println(serviceSoap.helloWorld());
	} 
 	 
 	 
 	public  SMSServiceSoap getSMSServiceSoap(){
// 		创建wsdl的URL
		URL url;
		try {
			url = new URL(path);
			QName qName=new QName(targetNamespace,serName);
			
			javax.xml.ws.Service service= javax.xml.ws.Service.create(url,qName);
			SMSServiceSoap  serviceSoap=service.getPort(SMSServiceSoap.class);
			return serviceSoap;
		} catch (MalformedURLException e) {
			// TODO 自动生成 catch 块
			System.out.println("获取 SMSServiceSoap 失败！");
			e.printStackTrace();
			return null;
		}
		//创建服务名称
		
 	}
 	
}
