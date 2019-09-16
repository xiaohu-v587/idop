
package com.goodcol.util.date;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
 

/**
 * <p>数据格式转换类。</p>
 *
 * <br>
 * <br>使用说明:
 * <br>本来主要提供各种数据格式或类型互换。提供的方法都是静态static,方便使用。
 * <br>
 * <br>
 * <br>修改记录
 *
 */
public class BolusData {
	static String sysCharsetName=null;
	
	
	/**
	 * 金融记数法转换成普通数字型。
	 * <br>例如 132,000,123.00 转换后为 132000123.00
	 * @param data - 要转换的数字字符串
	 * @return String - 返回转换后的数字字符串
	 */
	public static String finalToNormal(String data) {
		//String newData = data.replaceAll(",", ""); //since jdk 1.4
		String newData = data;
		int index = newData.indexOf(',');
		while(index != -1){
			newData = newData.substring(0, index) + newData.substring(index+1);
			index = newData.indexOf(',');
		}

		/*
		int pos = newData.lastIndexOf('.');
		int len = 0; //小数位数
		if (pos != -1) {
			len = newData.length() - 1 - pos;
		}

		try {
			double d = Double.parseDouble(newData);
			NumberFormat form = NumberFormat.getInstance();
			String mask = "###0";
			if (len > 0) {
				mask = "###0.";
				for (int i = 0; i < len; i++) {
					mask = mask + "0";
				}
			}
			((DecimalFormat) form).applyPattern(mask);
			newData = form.format(d);
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		return newData;
	}

	/**
	 * 普通数字型转换成金融记数法。
	 * <br>例如 132000123.00 转换后为 132,000,123.00
	 * @param data - 要转换的数字字符串
	 * @return String - 返回转换后的数字字符串
	 */
	public static String normalToFinal(String data) {
		int pos = data.lastIndexOf('.');
		int len = 0; //小数位数
		if (pos != -1) {
			len = data.length() - 1 - pos;
		}
		
		try {
			double d = Double.parseDouble(data); 
			NumberFormat form = NumberFormat.getInstance();
			String mask = "#,##0";
			if (len > 0) {
				mask = "#,##0.";
				for (int i = 0; i < len; i++) {
					mask = mask + "0";
				}
			}
						
			((DecimalFormat) form).applyPattern(mask);
			return form.format(d);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 普通数字型转换成金融记数法。
	 * <br>例如 132000123.00 转换后为 132,000,123.00
	 * <br>小数点保留两位
	 * @param data - 要转换的数字
	 * @return String - 返回转换后的数字字符串
	 */
	public static String normalToFinal(double data) {
		try {
			NumberFormat form = NumberFormat.getInstance();
			String mask = "#,##0.00";
			((DecimalFormat) form).applyPattern(mask);
			return form.format(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 格式化浮点值为字符串型, 指定小数位数长度。
	 * <br>例如 132000123.000000 转换成两位小数点后为 "132000123.00"
	 * @param data - 要转换的浮点数
	 * @param len - 保留小数位数
	 * @return String - 返回转换后的数字字符串
	 */
	public static String formatDouble(double data, int len) {
		String ret = null;
		try {			
			NumberFormat form = NumberFormat.getInstance();
			String mask = "###0";
			if (len > 0) {
				mask = "###0.";
				for (int i = 0; i < len; i++) {
					mask = mask + "0";
				}
			}
			((DecimalFormat) form).applyPattern(mask);
			ret = form.format(data);
		} catch (Exception e) {
			e.printStackTrace();
			ret = null;
		}
		return ret;
	}

	/**
	 * 格式化浮点值为字符串型, 默认小数位数长度为二。
	 * <br>例如 132000123.000000 转换成后为 "132000123.00"
	 * @param data - 要转换的浮点数
	 * @return String - 返回转换后的数字字符串
	 */
	public static String formatDouble(double data) {
		String ret = null;
		try {
			NumberFormat form = NumberFormat.getInstance();
			String mask = "###0.00";
			((DecimalFormat) form).applyPattern(mask);
			ret = form.format(data);
		} catch (Exception e) {
			e.printStackTrace();
			ret = null;
		}
		return ret;
	}
	/**
	 * 格式化浮点值为定长,取四舍五入数据
	 * <br>例如 132000123.000000 转换成后为 "132000123.00"
	 * @param data - 要转换的浮点数
	 * @param len  - 小数位长度
	 * @return String - 返回转换后的数字字符串
	 */
	public static double double2double(double data,int len) {
		double ret = data;
		try {
			NumberFormat form = NumberFormat.getInstance();
			String mask = "###0";
			if (len > 0) {
				mask = "###0.";
				for (int i = 0; i < len; i++) {
					mask = mask + "0";
				}
			}
			((DecimalFormat) form).applyPattern(mask);
			ret = Double.parseDouble(form.format(data));
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return ret;
	}
	
	/**
	 * 格式化浮点，取小数点后第len位前的整数值
	 * <br>例如 0.003000，4 转换成后为 "30"
	 * @param data - 要转换的浮点数
	 * @param len  - 小数位长度
	 * @return String - 返回转换后的数字字符串
	 */
	public static int double2point(double data,int len) {
		double decade=1.0;
		for(int i=0;i<len;i++)
			decade=decade*10.0;
		return (int)Math.round(data*decade);
	}

	/**
	 * 格式化科学记数的值为普通数字的字符串型, 默认小数位数长度为二。
	 * <br>例如 123.10E3 转换成后为 "132000123.00"
	 * @param data - 要转换的浮点数
	 * @return String - 返回转换后的数字字符串
	 */
	public static String expToNormal(String data) {
		String ret = null;
		try {
			double d = Double.parseDouble(data);
			NumberFormat form = NumberFormat.getInstance();
			String mask = "###0.00";
			((DecimalFormat) form).applyPattern(mask);
			ret = form.format(d);
		} catch (Exception e) {
			ret = null;
		}
		return ret;
	}

	/**
	 * 格式化科学记数的值为普通数字的字符串型,指定小数位数长度。
	 * <br>例如 123.10E3 转换成后为 "132000123.00"
	 * @param data - 要转换的浮点数
	 * @param len - 小数位数
	 * @return String - 返回转换后的数字字符串
	 */
	public static String expToNormal(String data, int len) {
		String ret = null;
		try {
			double d = Double.parseDouble(data);
			NumberFormat form = NumberFormat.getInstance();
			String mask = "###0";
			if (len > 0) {
				mask = "###0.";
				for (int i = 0; i < len; i++) {
					mask = mask + "0";
				}
			}
			((DecimalFormat) form).applyPattern(mask);
			ret = form.format(d);
		} catch (Exception e) {
			ret = null;
		}
		return ret;
	}

        /**************************字节数组操作***********************************/
	/**
	 * 字节数据相连
	 */   
	public static  byte[] arrayUnion(byte[] array1,byte[] array2 ){
	   	byte[] newArray=new byte[array1.length+array2.length];
	   	System .arraycopy(array1,0,newArray,0,array1.length);
	   	System .arraycopy(array2,0,newArray,array1.length,array2.length);
	   	return newArray; 
	   }
	
        /**************************字符串操作***********************************/



              /**
               *    判断字符串是否为空
               *    @param  s 字符串
               *    @return true为空 false 不为空
               */
          public static boolean isNullStr(String s)
          {
                  if (s==null || s.trim().length()<=0)
                          return true;
                  else return false;
          }

      		/**
           	*    判断字符串数组是否为空
           	*    @param  s 字符串数组
	        *    @return true为空 false 不为空
	        */

          public static boolean isNullStr(String[] s)
          {
                  if ((s==null) || (s.length<=0))
                          return true;
                  else return false;
          }

          /**
           * 取GBK编码下字符串 与 目标字符集的对应字符串
           * @param gbkString
           * @param charsetName
           * @return  目标字符集的对应字符串
           */
          public static byte[] getBuf(String gbkString,String charsetName){
          	try{
              	return  gbkString.getBytes(charsetName);
             }catch(UnsupportedEncodingException e){
             	e.printStackTrace();
             	return null;
             }
          }
          
          /**
           * 补字符串
           * @param in      输入操作系统编码字符串
           * @param before  在前还是在后
           * @param fix     0或空格
           * @param len     补满后至少长度
           * @param charsetName
           * @return  补后的字符串
           */
     
          public static byte[] fixString(String in,boolean before,String fix,int len,String charsetName)
          {
          	byte[]  ins = null;
          	byte[]  out=new byte[len];
			  
        	if (isNullStr(in))
        	  	in="";
            
            try {
            	ins=in.getBytes(charsetName);
			} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					return out;
			}
			if(len<=ins.length)
				return ins;
			
            byte[] fixBuf=getBuf(fix,charsetName);
            
           if(before){    
           	  	
           	for (int i=0;i<len-ins.length;){
           		if(i+fixBuf.length>len-ins.length)
           			break;
           		System.arraycopy(fixBuf,0,out,i,fixBuf.length); 
	           	i=i+fixBuf.length;
	           	
	            }  
           	System.arraycopy(ins,0,out,len-ins.length,ins.length); 
           }else{
           	System.arraycopy(ins,0,out,0,ins.length);   	
           	for (int i=ins.length;i<len;){
           		if(i+fixBuf.length>len)
           			break;
           		System.arraycopy(fixBuf,0,out,i,fixBuf.length); ;
	           	i=i+fixBuf.length;
	           	
	            }
           	
               	
           }	
            
            
            return out;
          }
          
          public static String getSysCharset(){
          	if(sysCharsetName==null){
	          	try {
					FileWriter filewriter = 
					    new FileWriter("out");
					sysCharsetName = 
					    filewriter.getEncoding();
					filewriter.close();
				} catch (IOException e) {
//					e.printStackTrace();
					sysCharsetName="GBK";
				}
          	}
            return sysCharsetName;
          }
          /**
           *    按长度把字符串前补0
           *    @param s 需要前补0的字符串
           *    @param len 生成后的字符串长度
           *    @return len长度前补0的字符串
           */
          public static String fix0BeforeString(String in, int len){
          	return new String(fix0BeforeString(in,len,getSysCharset()));
          }
          
          public static byte[] fix0BeforeString(String in, int len,String charsetName)
          {
          	
            return fixString(in,true,"0",len,charsetName);
          }

      /**
       *    按长度把字符串前补0，超过长度截去前面的部分
       *    @param in 需要前补0的字符串
       *    @param len 生成后的字符串长度
       *    @return len长度前补0的字符串
       */
          public static String fix0AndLenBeforeString(String in, int len) {
          	return new String(fix0AndLenBeforeString( in,len,getSysCharset()));
          }
          
	    public static byte[] fix0AndLenBeforeString(String in, int len,String charsetName) {
	    	byte[] out=fixString(in,true,"0",len,charsetName);
	    	if(out.length>len){ 
				byte[] returnOut=new byte[len];
				System.arraycopy(out,0,returnOut,out.length-len,len); 
				return returnOut;
	    	}else
	    		return out;
	      }
              
              
          /**
           *    按长度把字符串后补0
           *    @param in 需要后补0的字符串
           *    @param len 生成后的字符串长度
           *    @return len长度后补0的字符串
           */
	    public static String fix0AfterString(String in, int len){
	    	return new String(fix0AfterString(in,len,getSysCharset()));
	    	
	    }
	    
          public static byte[] fix0AfterString(String in, int len,String charsetName)
          {
              
          	 return fixString(in,false,"0",len,charsetName);
          }
          /**
             * 返回前补0的固定长度字符串
             * @param num long  数字
             * @param len int   补0后总共长度
             * @return String 返回前补0的固定长度字符串
             */
          public static String fix0BeforeNumber(long num, int len){
          	    return new String(fix0BeforeNumber( num,  len,getSysCharset()));
          }
            public static byte[] fix0BeforeNumber(long num, int len,String charsetName)
             {
            	 return fixString(""+num,true,"0",len,charsetName);
             }

             /**
              * 字符串后补空格
              * @param in String  已有字符串
              * @param len int    补空格后字节长度
              * @return String    如果原字符串长度>=len，返回原字符串；否则后补空格后的字符串
              */
            public static String fixSpaceAfterString(String  in, int len){
                 return new String(fixSpaceAfterString(in,len,getSysCharset()));
            }
            
            /**
             * 字符串后补空格
             * @param in  已有ASCII码字符串
             * @param len
             * @param charsetName
             * @return  转码后字符串
             */
            public static byte[] fixSpaceAfterString(String  in, int len,String charsetName)
             {
            	 return fixString(in,false," ",len,charsetName);
             }
             
             /**
              * 字符串前补空格
              * @param in String  已有字符串
              * @param len int    补空格后字节长度
              * @return String    如果原字符串长度>=len，返回原字符串；否则后补空格后的字符串
              */
             public static String fixSpaceBeforeString(String  in, int len){
             	return new String(fixSpaceBeforeString(in,len,getSysCharset()));
             }
             
             /**
              * 字符串前补空格
              * @param in  已有ASCII码字符串
              * @param len
              * @param charsetName
              * @return  转码后字符串
              */
             public static byte[] fixSpaceBeforeString(String  in, int len,String charsetName)
             {
             	 return fixString(in,true," ",len,charsetName);
             }
             
             
             
     /**
      * 得到16进制string
      * @param byteData
      * @return
      */
     public static String getHexNum(byte byteData){  
       return Integer.toHexString(byteData);
     }


    /**
	 * 截取指定长度的中文字符
	 * cnSubstr("我是中国人",4,"") 结果为 "我是"
	 * cnSubstr("我是中国人",7,"") 结果为 "我是中","国"字因为占两个字节,所以省略
	 * cnSubstr("我是中国人",7,"...") 结果为 "我是中...",省略"国"字并加上后缀
	 * @param str 字符串
	 * @param length 指定长度,以字节数为单位
	 * @param tail 当字符串长度超过指定长度时加上的后缀
	 * @return
	 */
	public static String cnSubstr(String str, int length, String tail)
	{
		String result = null;
		try {
			int pos = 0;
			int end = 0;
			char[] strChars = str.toCharArray();
			int strLength = strChars.length;
			for (int i = 0; i < strLength; i++, end++) {
				int ascii = strChars[i];
				if (ascii > 255)
					pos += 2;
				else
					pos++;
				if (pos > length)
					break;
			}
			result = (end < strLength) ? (str.substring(0, end) + tail) : (str);
		} catch (Exception e) {
		}
		return result;
	}
	/**
	 * IBM  AS400 上使用的EBDCDIC码 转换成 ascii码
	 * @param input
	 * @return ascii码字节数组
	 */
	public static byte[] ebcdic2ascii(byte[] ebcdic){
		 
		try {
			return (new String(ebcdic,"cp935")).getBytes();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null; 
	}
	
	/**
	 * IBM  AS400 上使用的EBDCDIC码 转换成 ascii码
	 * @param input  被格式化成本机字符集后的字符串
	 * @return  ascii码字符串
	 */
	public static String ebcdic2ascii(String ebcdic){
		 
		try {
			return new String(ebcdic.getBytes(),"cp935");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null; 
	}
	
	/**
	 * ascii码转换成IBM  AS400 上使用的EBDCDIC码
	 * @param input
	 * @return
	 */
	public static byte[] ascii2ebcdic(byte[] ascii){
		try {
			return (new String(ascii)).getBytes("cp935");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null; 
	}
	
	/**
	 * ascii码转换成IBM  AS400 上使用的EBDCDIC码
	 * @param ascii  被格式化成本机字符集后的字符串
	 * @return
	 */
	public static String ascii2ebcdic(String ascii){
		try {
			return new String(ascii.getBytes("cp935"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null; 
	}

    public static void main(String[] args) {
    	 
			byte[] bytes=fixSpaceAfterString("144",5,"cp935");
			System.out.println(bytes.length);
			for(int i=0;i<bytes.length;i++){ 

				System.out.println("|"+bytes[i]+"|");
			}
			System.out.println("|"+(new String(bytes))+"|");
		  	
    	
    	/*char[] input=new char[]{0X0E,0X5A,0XAC,0X0F};
             	//"一".getBytes();
             	
    	        byte[] output= HsData.ebcdic2ascii((new String(input)).getBytes());
             	
    	        for(int i=0;i<output.length;i++){
    	        		System.out.print((output[i]>0?output[i]:(256+output[i]))+" ");
    	        //System.out.print(new String(new char[]{(char)output[i]}));
    	        if(i>0&&i%16==0)
    	        	System.out.println("");
    	        }
    	        System.out.println("");
    	        System.out.println(new String(output));
    	        
    	       // System.out.println( HsData.ascii2ebcdic("一"));*/
             }
}
