package net.penganjian.proxyserver.httpinterparser;
/*
 * ����ӿڶ�����������е�ȥ���httpЭ��
 */
class IllegalHTTPRequestException extends Exception{
}

class InterpretNtCompletedException extends Exception{
}

public interface HttpRequestParser {
	/*
	 * �ⲿ�����᲻�ϴ���line,line��Ӧ��һ��http request�е�һ��
	 * line�ᰴ��request�е�˳����
	 * ��: һ�����ܵ�http request �ᰴ����˳�����
	 *    interpretNextLine("GET http://www.baidu.com HTTP/1.1");
	 *    interpretNextLine("Host: www.baidu.com");
	 *    interpretNextLine("Connection: Keep-Alive");
	 *    interpretNextLine("Accept-Encoding: gzip");
	 *    interpretNextLine("");
	 *    
	 * ����ֵΪһ��boolean�������Ƿ�����������־ ����
	 * �׳��쳣�������У�����������ʽ
	 *   
	 *    
	 */
	boolean parseNextLine(String line) throws IllegalHTTPRequestException;
	
	/*
	 * 	��һ��http request���������󣬵���resetʹ��interpreter�ܹ�������һ��http request
	 *  ����reset�᷵����һ��������http request��������HttpRequest����,
	 *  �����һ�εĽ���û�н�����throw Exception
	 */
	HttpRequest reset() throws InterpretNtCompletedException;
}
