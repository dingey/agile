import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.di.toolkit.HttpURLConnectionUtil;

/**
 * @author di
 */
public class HiTest {
	String url = "http://localhost:8090/hi.htm";

	@Test
	public void test() {
		Map<String, String> args = new HashMap<>();
		System.out.println(HttpURLConnectionUtil.post(url, args));
	}
}
