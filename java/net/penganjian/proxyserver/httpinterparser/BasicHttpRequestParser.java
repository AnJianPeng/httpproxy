package net.penganjian.proxyserver.httpinterparser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.penganjian.proxyserver.uris.HttpRegex;

public class BasicHttpRequestParser implements HttpRequestParser{
	private HttpRequest request = new HttpRequest();
	
	private enum RequestState {REQUEST_LINE,HEADER_LINE};
	private RequestState state = RequestState.REQUEST_LINE;
	
	
	@Override
	public boolean parseNextLine(String line)
			throws IllegalHTTPRequestException {
		//a DFA
		switch(state){
		case REQUEST_LINE:
			Matcher matcher1 = HttpRegex.nonConnectRequestPattern.matcher(line);
			if(matcher1.find()){
				state = RequestState.HEADER_LINE;
				return false;
			}else{
				//something wrong
				throw new IllegalHTTPRequestException();
			}
		case HEADER_LINE:
			Matcher matcher2 = HttpRegex.headerPattern.matcher(line);
			if(matcher2.find() && matcher2.groupCount() == 2){
				//add the part into header fields
				if(matcher2.group(1).equals("Host")){
					request.setHostName(matcher2.group(2));
					Pattern pattern = Pattern.compile("(.+):(.+)");
					Matcher matcher = pattern.matcher(matcher2.group(2));
					if(matcher.find() && matcher.groupCount() == 2){
						request.setHostName(matcher.group(1));
						request.setPortNum(Integer.valueOf(matcher.group(2)));
					}
				}
				request.addOption(matcher2.group(1), matcher2.group(2));
				return false;
			}else{
				if(line.equals("")){
					state = RequestState.REQUEST_LINE;
					return true;
				}
				//something wrong
				throw new IllegalHTTPRequestException();
			}
		}
		return true;
	}

	@Override
	public HttpRequest reset() throws InterpretNtCompletedException {
		HttpRequest temp = request;
		request = new HttpRequest();
		return temp;
	}

}
