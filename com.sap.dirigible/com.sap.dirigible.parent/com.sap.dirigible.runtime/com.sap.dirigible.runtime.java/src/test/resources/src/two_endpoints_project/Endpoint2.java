package two_endpoints_project;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Endpoint2 {
	
	public void service(HttpServletRequest request, HttpServletResponse response, Map<String, Object> scope) throws Exception {
		System.out.print("Hello from Endpoint 2!");
	}
}