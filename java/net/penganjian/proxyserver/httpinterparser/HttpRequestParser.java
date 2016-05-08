package net.penganjian.proxyserver.httpinterparser;
/*
 * 这个接口定义了如何逐行地去理解http协议
 */
class IllegalHTTPRequestException extends Exception{
}

class InterpretNtCompletedException extends Exception{
}

public interface HttpRequestParser {
	/*
	 * 外部函数会不断传入line,line对应着一个http request中的一行
	 * line会按照request中的顺序传入
	 * 例: 一个可能的http request 会按以下顺序调用
	 *    interpretNextLine("GET http://www.baidu.com HTTP/1.1");
	 *    interpretNextLine("Host: www.baidu.com");
	 *    interpretNextLine("Connection: Keep-Alive");
	 *    interpretNextLine("Accept-Encoding: gzip");
	 *    interpretNextLine("");
	 *    
	 * 返回值为一个boolean，代表是否遇到结束标志 “”
	 * 抛出异常可能性有：错误的请求格式
	 *   
	 *    
	 */
	boolean parseNextLine(String line) throws IllegalHTTPRequestException;
	
	/*
	 * 	当一个http request解析结束后，调用reset使得interpreter能够解析下一个http request
	 *  调用reset会返回上一个完整的http request解析出的HttpRequest对象,
	 *  如果上一次的解析没有结束则throw Exception
	 */
	HttpRequest reset() throws InterpretNtCompletedException;
}
