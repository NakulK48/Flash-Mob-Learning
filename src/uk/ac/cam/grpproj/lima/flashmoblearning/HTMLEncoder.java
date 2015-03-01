package uk.ac.cam.grpproj.lima.flashmoblearning;

public class HTMLEncoder {
	
	static final String BLACKLIST = "<>&\"'/";
	
	static final String[] BLACKLIST_ENCODED = {
		"&lt;",
		"&gt;",
		"&amp;",
		"&quot;",
		"&#39;",
		"&#47;"
	};
	
	public static String encode(String s) {
		assert(BLACKLIST.length() == BLACKLIST_ENCODED.length);
		StringBuilder sb = new StringBuilder(s.length());
		for(int i=0;i<s.length();i++) {
			char c = s.charAt(i);
			int idx = BLACKLIST.indexOf(c);
			if(idx >= 0) {
				sb.append(BLACKLIST_ENCODED[idx]);
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

}
