package com.goodcol.util;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class SocketClient {
	/**
	 * 
	 * @param msg
	 * @param host
	 * @param port
	 * @return
	 */
	public String client(String msg, String host, int port) {
		Socket socket = null;
		try {
			socket = new Socket(host, port);
			DataOutputStream ps = new DataOutputStream(socket.getOutputStream());
			InputStream is = new ByteArrayInputStream(msg.getBytes("gbk"));
			byte[] b = new byte[1024];
			while (is.read(b) == 1024) {
				ps.write(b);
			}
			ps.write(b);
			ps.flush();
			InputStream inputStream = socket.getInputStream();
			DataInputStream dis = new DataInputStream(inputStream);
			byte[] bdata = new byte[1024];
			dis.read(bdata, 0, 1024);
			String str = new String(bdata, "gbk");
			socket.shutdownInput();
			is.close();
			dis.close();
			ps.close();
			return str;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return "";
		} finally {
			try {
				if (socket != null)
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
