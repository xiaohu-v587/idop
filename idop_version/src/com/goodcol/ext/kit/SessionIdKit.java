
package com.goodcol.ext.kit;

import java.security.SecureRandom;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;

/**
 * SessionIdKit.
 */
public class SessionIdKit {
	
    protected static Random random;
    private static boolean weakRandom;
    
    /**
     * Lazy initialization holder class pattern
     */
    private static class FieldHolder {
    	static final SessionIdKit sessionIdKit = new SessionIdKit();
    }
    
    private SessionIdKit() {
    	try {
			// This operation may block on some systems with low entropy. See
			// this page
			// for workaround suggestions:
			// http://docs.codehaus.org/display/JETTY/Connectors+slow+to+startup
			System.out.println("Init SecureRandom.");
			random = new SecureRandom();
			weakRandom = false;
		} catch (Exception e) {
			System.err.println("Could not generate SecureRandom for session-id randomness");
			random = new Random();
			weakRandom = true;
		}
    }
    
    public static final SessionIdKit me() {
    	return FieldHolder.sessionIdKit;
    }
    
	public String generate(HttpServletRequest request) {
        synchronized(this) {
            String id = null;
            while (id == null || id.length() == 0) {	//)||idInUse(id))
                long r0 = weakRandom ? (hashCode()^Runtime.getRuntime().freeMemory()^random.nextInt()^(((long)request.hashCode())<<32)) : random.nextLong();
                long r1 = random.nextLong();
                if (r0<0) r0 = -r0;
                if (r1<0) r1 = -r1;
                id=Long.toString(r0,36)+Long.toString(r1,36);
            }
            return id;
        }
	}
}



