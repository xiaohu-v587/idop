package com.goodcol.plugin.auth;

import java.security.SecureRandom;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;

/**
 * TODO 考虑改名为 SessionIdBuilder
 */
public class AccessTokenBuilder {
	
	private static Random random;
    private static boolean weakRandom;
    private static int hashCode = new AccessTokenBuilder().hashCode();
    
    private AccessTokenBuilder() {
		try {
			// This operation may block on some systems with low entropy. See
			// this page for workaround suggestions:
			// http://docs.codehaus.org/display/JETTY/Connectors+slow+to+startup
			// System.out.println("Init SecureRandom.");
			random = new SecureRandom();
			weakRandom = false;
		} catch (Exception e) {
			random = new Random();
			weakRandom = true;
			System.err.println("Could not generate SecureRandom for accessToken randomness");
		}
	}
	
	public static String getAccessToken(HttpServletRequest request) {
		String accessToken = null;
        while (accessToken == null || accessToken.length() == 0) {
            long r0 = weakRandom ? (hashCode ^ Runtime.getRuntime().freeMemory() ^ random.nextInt() ^ (((long)request.hashCode()) << 32)) : random.nextLong();
            long r1 = random.nextLong();
            if (r0 < 0) r0 = -r0;
            if (r1 < 0) r1 = -r1;
            accessToken = Long.toString(r0, 36) + Long.toString(r1, 36);
        }
        return accessToken;
	}
}
