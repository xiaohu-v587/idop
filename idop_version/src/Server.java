import com.goodcol.core.core.Gcds;


/**
 * 服务器启动管理
 * @author lubin
 */ 
public class Server {
	public static void main(String[] args) {
		//test222444
		String webAppDir=Server.class.getResource("/").getPath().replace("/WEB-INF/classes/", "");
		int port=79; 
		String content="/";
		if(args!=null&&args.length>0){
			try{
			port=Integer.parseInt(args[0]);
			content=args[1];
			}catch(Exception e){}
		}
		try{
			//System.setProperty("org.mortbay.jetty.Request.maxFormContentSize", "900000");
			System.setProperty("org.eclipse.jetty.server.Request.maxFormContentSize","9000000");

			Gcds.start(webAppDir.substring(1), port,content, 10);
		}catch(Exception e){e.printStackTrace();}
	}
}
