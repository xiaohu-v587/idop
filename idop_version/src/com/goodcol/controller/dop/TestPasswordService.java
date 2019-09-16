package com.goodcol.controller.dop;


import java.util.ResourceBundle;

import javax.xml.namespace.QName;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;

import com.goodcol.util.safe.MD5;

public class TestPasswordService {

    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) {
    	System.out.println(MD5.getMD5ofStr("Idop_14789"));
//    	testValidate();//验证
    	//testReset("admin");
//    	testReset();//重置密码
 //       testModify();//修改密码
    	
    	/*
    	for(int i=0;i<10;i++)
    	{
    		long st = System.currentTimeMillis();
    		testValidate();
    		long ed = System.currentTimeMillis();
    		System.out.println((ed-st));
    	}
    	*/
    	
    }

    private static void testValidate(){
        RPCServiceClient serviceClient=null;
		try {
			serviceClient = new RPCServiceClient();
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        Options options = serviceClient.getOptions();
        //ResourceBundle resourceBundle = ResourceBundle.getBundle("config/env/app");
        String endPoint="http://22.200.129.201:9080/ehr/services/password?wsdl";
        EndpointReference targetEPR = new EndpointReference(endPoint);
        options.setTo(targetEPR);
        
        String pwd = "111111";
//        String str = encodePwd(pwd);//此处注释，以前是传输密文，现在传输明斿
        QName qname = new QName("http://pwd.ws.neusoft.com", "validate");
        Object[] inputArgs = new Object[] { "040661", pwd };
        Class[] returnTypes = new Class[] {ResultPo.class};
        Object[] response=null;
        try {
			response = serviceClient.invokeBlocking(qname, inputArgs, returnTypes);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        ResultPo result = (ResultPo) response[0];
        //String result=null;
        // display results
        if (result == null) {
            System.out.println("PasswordService didn't initialize!");
        } else {
            if (result.isSuccess()) {
                System.out.println("密码验证通过＿ + result.isSuccess()");
            }
            else {
                System.out.println("错误＿ + result.getMsg()");
            }
        }
    }
    
  
}
