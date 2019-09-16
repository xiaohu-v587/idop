import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import org.apache.commons.httpclient.HttpConnection;


public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(a(3));;
		
	}
	
	public static int a(int i) {
		int result = 0;
		switch(i) {
			case 1: result = i=0;
			case 2: result = 1-0;
			case 3: result = result + i*2;
			break;
		}
		return result;
	}

}










