package com.goodcol.util.zxgldbutil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketClient {
	private static String host = "22.200.25.34";
	private static int port = 10000;

	public String client(String msg) {
		Socket socket = null;
		try {
			socket = new Socket(host, port);
			DataOutputStream ps = new DataOutputStream(socket.getOutputStream());
			ps.write(msg.getBytes("utf-8"));
			ps.flush();
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			byte[] bdata = new byte[1024];
			int ll = dis.read(bdata, 0, 983);
			String str = new String(bdata, "utf-8");
			dis.close();
			ps.close();
			return str;
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
			return "";
		} finally {
			try {
				if (socket != null)
					socket.close();
			} catch (IOException e) {
			}
		}

	}

}
