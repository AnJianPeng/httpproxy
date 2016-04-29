package net.penganjian.proxyserver.uris;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpRegex {
	public static final String[] methodsSupported = {"OPTIONS","GET","HEAD","POST","PUT","DELETE","TRACE","CONNECT"};
	public static final String[] headerFields = {
		"Accept-Charset","Accept-Encoding","Accept-Language","Authorization",
		"Expect","From","Host","If-Match","If-Modified-Since",
		"If-None-Match","If-Range","If-Unmodified-Since","Max-Forwards",
		"Proxy-Authorization","Range","Referer","TE","User-Agent"};
	
	private static final String regexConnectRequestLine = "(CONNECT) ([a-zA-z.0-9]+):([0-9]+) (HTTP/1\\.1|0)";
	private static final String regexNonConnectRequestLine = "([^ ]+) (.+) (HTTP/1\\.1|0)";
	private static final String regexHeaderLine = "(.+): (.+)";
	
	public static final Pattern connectRequestPattern = Pattern.compile(regexConnectRequestLine);
	public static final Pattern nonConnectRequestPattern = Pattern.compile(regexNonConnectRequestLine);
	public static final Pattern headerPattern = Pattern.compile(regexHeaderLine);
	
	public static final String testReturn = "HTTP/1.0 200 Connection established\n\n";
	public static final String testPage = "HTTP/1.1 200 Ok\r\nContent-Length: 25\r\n\r\n<body>hello world</body>";
	
	public static void main(String[] args) {
		Pattern pat = Pattern.compile(regexConnectRequestLine);
		Matcher matcher = pat.matcher("CONNECT leetcode.com:443 HTTP/1.1");
		if(matcher.find()){
			System.out.println(matcher.groupCount());
			for(int i = 0;i<=matcher.groupCount();i++)
				System.out.println(matcher.group(i));
		}else{
			System.out.println("fail");
		}
	}

}
