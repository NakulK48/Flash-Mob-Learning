package uk.ac.cam.grpproj.lima.flashmoblearning;

import org.junit.Assert;
import org.junit.Test;

public class HTMLEncoderTest {
	
	String in1 = "<test>";
	String out1 = "&lt;test&gt;";
	String in2 = "";
	String out2 = "";
	String in3 = "\"";
	String out3 = "&quot;";
	String in4 = "'";
	String out4 = "&#39;";
	String in5 = "hello";
	String out5 = in5;
	String in6 = "///";
	String out6 = "&#47;&#47;&#47;";
	
	
	@Test
	public void testEncode() {
		test(in1, out1);
		test(in2, out2);
		test(in3, out3);
		test(in4, out4);
		test(in5, out5);
		test(in6, out6);
	}

	private void test(String in, String out) {
		Assert.assertEquals(out, HTMLEncoder.encode(in));
	}

}
