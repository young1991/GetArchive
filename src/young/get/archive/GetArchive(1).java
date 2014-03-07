package young.get.archive;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date; 
/**
 * GetArchive
 * 
 * @author young
 * 
 */

public class GetArchive {
	/*Main Processing*/
	public static void main(String[] args){
		String base_url = "http://data.githubarchive.org/";
		String base_path = "d:\\young\\results\\Json\\";
		//Now time
	//	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd-HH");  
	//	String[] date=sdf.format(new java.util.Date()).split("-");
		int now_year = 2013;
		int now_month = 2;
	//	int now_day = Integer.parseInt(date[2]);
	//	int now_hour = Integer.parseInt(date[3]);
		//Base Time
		int base_year = 2012;
		int base_month = 1;
		int base_day = 1;
	//	int base_hour = 0;
		for(int month=base_month;month<=12+now_month+(now_year-base_year-1)*12;month++)
		{
			int month_real = (month%12==0)?12:(month%12);
			int year = (month<=12)?base_year:(base_year+(month-1)/12);
			int day_limit = 0;
			switch(month_real)
			{
				case 1: 
				case 3: 
				case 5: 
				case 7: 
				case 8: 
				case 10: 
				case 12: 
					 day_limit = 31;
				     break; 
				case 2: 
					 if(year%4==0)
					   	day_limit = 29;
					 else
					   	day_limit = 28; 
				     break; 
				case 4: 
				case 6: 
				case 9: 
				case 11: 
				     day_limit = 30; 
				     break; 
			}
			for(int day=base_day;day<=day_limit;day++)
			{
				String name_year = String.valueOf(year);
				String name_month = (month_real>=10)?String.valueOf(month_real):String.format("0%d", new Object[]{Integer.valueOf(month_real)});
				String name_day = (day>=10)?String.valueOf(day):String.format("0%d", new Object[]{Integer.valueOf(day)});
			   	for(int hour=0;hour<=23;hour++)
			   	{
			   		String name_hour = String.valueOf(hour);
			   		String name = String.format("%s-%s-%s-%s.json.gz",
			   				new Object[]{name_year,name_month,name_day,name_hour});
			   		String url = base_url + name;
			   		String path = base_path +"\\"+ name_year +"\\"+ name_month +"\\"+ name_day;
			   		getFiles(url,path,name);
			   		System.out.println(year+"-"+month_real+"-"+day+"-"+hour+" ==>OK!");
			   	}
			}
		}		
	}
	/*Get Files*/
	public static void getFiles(String i_url, String o_path, String name){
		String sURL = i_url;
		int nStartPos = 0;
		int nRead = 0;
		String sPath = o_path;
		try {
			URL url = new URL(sURL);
			//打开连接
			HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
			//获得文件长度
			long nEndPos =getFileSize(sURL);
			File writeFilefd = new File(sPath);  
	        if (!writeFilefd.exists()) {  
	                writeFilefd.mkdirs();  
	        } 
			RandomAccessFile oSavedFile= new RandomAccessFile(sPath+"\\"+name, "rw");
			httpConnection.setRequestProperty("User-Agent", "Internet Explorer");
			String sProperty = "bytes=" + nStartPos + "-";
			//告诉服务器book.rar这个文件从nStartPos字节开始传
			httpConnection.setRequestProperty("RANGE", sProperty);
			System.out.println(sProperty);
			InputStream input = httpConnection.getInputStream();
			byte[] b = new byte[1024];
			//读取网络文件,写入指定的文件中
			while ((nRead = input.read(b, 0, 1024)) > 0
					&& nStartPos < nEndPos ) {
				oSavedFile.write(b, 0, nRead);
				nStartPos += nRead;
			}
			httpConnection.disconnect();
			oSavedFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 
	/*获得文件长度*/
	public static long getFileSize(String sURL) {
		int nFileLength = -1;
		try {
			URL url = new URL(sURL);
			HttpURLConnection httpConnection = (HttpURLConnection) url
					.openConnection();
			httpConnection.setRequestProperty("User-Agent", "Internet Explorer");

			int responseCode = httpConnection.getResponseCode();
			if (responseCode >= 400) {
				System.err.println("Error Code : " + responseCode);
				return -2; // -2 represent access is error
			}
			String sHeader;
			for (int i = 1;; i++) {
				sHeader = httpConnection.getHeaderFieldKey(i);
				if (sHeader != null) {
					if (sHeader.equals("Content-Length")) {
						nFileLength = Integer.parseInt(httpConnection
								.getHeaderField(sHeader));
						break;
					}
				} else
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(nFileLength);
		return nFileLength;
	}
}