package young.get.archive;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonFactory;  
import com.fasterxml.jackson.core.JsonGenerationException;  
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;  
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.TreeNode;
import young.get.archive.UncompressGZ;
/**
 * JsonParse
 * 
 * @author young
 * 
 */
public class JsonParse {
	public static int number_of_record;
	//event count
	public static int all_count;
	public static int pr_count;
	public static int hour_count;
	public static void inputStream() throws Exception {
		 FileReader reader = new FileReader("D:\\young\\results\\2014-03-03-12.json\\2012-11-01-15.json");
		 BufferedReader br = new BufferedReader(reader);
		 String s1 = null;
		 int line_count = 0;
		 number_of_record = 0;
		//output file
		 String outpath = "D:\\young\\results\\2012-11-01-15_commit_comments.csv";
	     FileWriter fw = new FileWriter(outpath);
		 BufferedWriter writer = new BufferedWriter(fw);
		 writer.write("no.,repo,pr_id,commit_id,diff_hunk,position,user,path,body,created_at");
		 writer.newLine();
			
		 while((s1 = br.readLine()) != null) {
			 line_count++;
			 System.out.println(line_count);
			 if(line_count==1)
			 {
				 System.out.println(s1);
			 }
			 parseCommitComments(s1,writer);
		 }
		 writer.flush();
		 br.close();
		 reader.close();
	}
	/*Parsing Pull Requests*/
	public static String extractPRId(String text) throws Exception {  
	//	String text = "https://github.com/mitar/PiplMesh/pull/117#discussion-diff-673793";
		String[] text_list = text.split("/");
		String[] pr_id = text_list[6].split("#");
	//	System.out.println(pr_id[0]);
		return pr_id[0];
	}
	/*replace function*/
	public static String replace(String text) throws Exception{
		String temp = null;
		if(text != null)
		{			
			temp = (String) text;
			temp = temp.replaceAll("\r|\n|\t", " ");
			temp = temp.replaceAll("\"", " ");
			temp = temp.replaceAll("\'", " ");
			temp = temp.replaceAll("\\s\\s", " ");
			temp = temp.replaceAll("\\s\\s", " ");
			temp = temp.replaceAll(",", " ");
		}
		return temp;
	}
	/*Parsing Repository*/
	public static void parseRepository() throws Exception { 
		//define the output factors
				String url = null;
				String language = null;
				String fork = null;
				String forks = null;
				String name = null;
				String created_at = null;
				String description = null;
				String owner = null;
				String watchers = null;
				String size = null;
				String timestamp = null;
				String writebuf = null;
				//output file
				String outpath = "D:\\YOUNG\\Results\\json\\projects.csv";
				FileWriter fw = new FileWriter(outpath);
				BufferedWriter writer = new BufferedWriter(fw);
				writer.write("id,url,language,fork,forks,name,created_at,description,owner,watchers,size,timestamp");
				writer.newLine();
				
				//parse the json file
				try {    
					JsonFactory jfactory = new JsonFactory();  
				    /*** read from file ***/  
					JsonParser jParser = jfactory.createParser(new File("D:\\young_jsons\\2012-04-11-15.json"));    
				    // loop until token equal to "}" 
					int id = 0;
				    while(jParser.nextToken() != null) 
				    {    
				        String fieldname = jParser.getCurrentName();
				        if ("repository".equals(fieldname)) 
				        {                
				            //current node is repository
				        	while (jParser.nextToken() != JsonToken.END_OBJECT) {    
				 		       String factorname = jParser.getCurrentName();
				 		       if ("url".equals(factorname)) {url = jParser.getText();}
				 		       if ("language".equals(factorname)) {language = jParser.getText();}
				 		       if ("fork".equals(factorname)) {fork = jParser.getText();} 
				 		       if ("forks".equals(factorname)) {forks = jParser.getText();} 
				 		       if ("name".equals(factorname)) {name = jParser.getText();}
				 		       if ("created_at".equals(factorname)) {created_at = jParser.getText();}
				 		       if ("description".equals(factorname)) {description = jParser.getText();}
				 		       if ("owner".equals(factorname)) {owner = jParser.getText();}
				 		       if ("watchers".equals(factorname)) {watchers = jParser.getText();}
				 		       if ("size".equals(factorname)) {size = jParser.getText();} 
				        	}
				        }
				        if("actor_attributes".equals(fieldname))
				        {
				        	jParser.skipChildren();
				        }
				        if("created_at".equals(fieldname))
				        {
				        	jParser.nextToken();
				        	timestamp = jParser.getText();
				        	id++;
			            	description = replace(description);
			            	// Write into file
			            	writebuf = String.format("%d,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",new Object[]{Integer.valueOf(id),url,language,fork,forks,name,created_at,description,owner,watchers,size,timestamp});
				        	writer.write(writebuf);
			            	writer.newLine();
			            	System.out.println(url);
			            	System.out.println(language);
			            	System.out.println(fork);
			            	System.out.println(forks);
			            	System.out.println(name);
			            	System.out.println(created_at);
			            	System.out.println(description);
			            	System.out.println(owner);
			            	System.out.println(watchers);
			            	System.out.println(size);
			            	System.out.println(timestamp);
				        }
				        if("payload".equals(fieldname))
				        {
				        	jParser.skipChildren();
				        } 
				     } 
				     writer.flush();
				     jParser.close();  		   
				     } catch (JsonGenerationException e) {  		   
				     e.printStackTrace();  	   
				     } catch (JsonParseException e) {  		   
				      e.printStackTrace();  		   
			     } catch (IOException e) {  		   
				     e.printStackTrace();  		   
			     }  		  
	}
	/*Parsing Commit Comments*/
	public static void parseCommitComments(String event,BufferedWriter writer) throws Exception {  
		//define the output factors
		int tag = 0;
		String repo = null;
		String position = null;
		String created_at = null;
		String body = null;
		String commit_id = null;
		String diff_hunk = null;
		String pr_id  = null;
		String path = null;	
		String user = null;
		String writebuf = null;
		//parse the json file
		try {    
			JsonFactory jfactory = new JsonFactory();  
		    /*** read from single event ***/  
			JsonParser jParser = jfactory.createParser(event);
			while(jParser.nextToken() != null)
			{ 
		        String fieldname = jParser.getCurrentName();
		        if("repository".equals(fieldname))
		        {
		        	while (jParser.nextToken() != JsonToken.END_OBJECT) {   
		        		String fieldname_repo = jParser.getCurrentName();
		        		if ("url".equals(fieldname_repo)) {repo = jParser.getText();}
		        	}
		        }
		        if("url".equals(fieldname))
		        {
		        	pr_id = jParser.getText();
			        System.out.println(pr_id);
		        }
		        if ("payload".equals(fieldname)) 
		        {                
		        	while (jParser.nextToken() != JsonToken.END_OBJECT) {    
		 		       String fieldname_pay = jParser.getCurrentName();
		 		       if ("comment".equals(fieldname_pay))
		 		       {
		 		    	  tag = 1;
		 		    	  while (jParser.nextToken() != JsonToken.END_OBJECT) { 
		 		    		 String fieldname_comment = jParser.getCurrentName();
		 		    		 if ("original_position".equals(fieldname_comment)) {position = jParser.getText();}
		 		    		 if ("created_at".equals(fieldname_comment)) {created_at = jParser.getText();}
		 		    		 if ("body".equals(fieldname_comment)) {body = jParser.getText();} 
		 		    		 if ("commit_id".equals(fieldname_comment)) {commit_id = jParser.getText();}
		 		    		 if ("diff_hunk".equals(fieldname_comment)) {diff_hunk = jParser.getText();}
		 		    		 if ("path".equals(fieldname_comment)) {path = jParser.getText();}
		 		    		 if ("user".equals(fieldname_comment)) 
		 		    		 { 
		 		    			while (jParser.nextToken() != JsonToken.END_OBJECT) { 
		 		    				String fieldname_user = jParser.getCurrentName();
		 		    				if ("login".equals(fieldname_user)) 
		 		    				{
		 		    					jParser.nextToken();
		 		    					user = jParser.getText();
		 		    				}
		 		    			}
		 		    	     }
		 		    		if("html_url".equals(fieldname_comment))
		 			        {
		 			        	pr_id = jParser.getText();
		 			        	System.out.println(pr_id);
		 			        }
		 		    		 if("_links".equals(fieldname_comment))
		 		    		 {
		 		    			jParser.skipChildren();
		 		    		 }
		 		    	  }
		 		       }
		        	}
		        }
		        if("actor_attributes".equals(fieldname))
		        {
		        	jParser.skipChildren();
		        }	
			}
		    jParser.close();
		    if(tag == 1)
	        {	
		    	    number_of_record++;
	        		writebuf = String.format("%d,%s,%s,%s,%s,%s,%s,%s,%s,%s",new Object[]{Integer.valueOf(number_of_record),repo,extractPRId(pr_id),commit_id,replace(diff_hunk),position,user,path,replace(body),created_at});
 					writer.write(writebuf);
 					writer.newLine();
 					System.out.println(number_of_record);
 					System.out.println(repo);
 					System.out.println(pr_id);
 					System.out.println(commit_id);
 					System.out.println(diff_hunk);
 					System.out.println(position);
 					System.out.println(user);
 					System.out.println(path);
 					System.out.println(body);
 					System.out.println(created_at);
	        }
		} catch (JsonGenerationException e) {  		   
		e.printStackTrace();  	   
		} catch (JsonParseException e) {  		   
		e.printStackTrace();  		   
	    } catch (IOException e) {  		   
		e.printStackTrace();  		   
	  }  		   
	} 
	/*Parsing Pull Requests*/
	public static void parsePullRequests() throws Exception {  
		//define the output factors
		int no =0;
		String url = null;
		String position = null;
		String created_at = null;
		String body = null;
		String commit_id = null;
		String path = null;	
		String user = null;
		String writebuf = null;
		//output file
		String outpath = "D:\\YOUNG\\Results\\json\\pull_reqeusts.csv";
		FileWriter fw = new FileWriter(outpath);
		BufferedWriter writer = new BufferedWriter(fw);
		writer.write("no.,url,commit_id,position,user,path,body,created_at");
		writer.newLine();
		
		//parse the json file
		try {    
			JsonFactory jfactory = new JsonFactory();  
		    /*** read from file ***/  
			JsonParser jParser = jfactory.createParser(new File("D:\\young_jsons\\2012-04-11-15.json"));    
		    // loop until token equal to "}" 
			int id = 0;
		    while(jParser.nextToken() != null) 
		    {    
		        String fieldname = jParser.getCurrentName();
		        if("repository".equals(fieldname))
		        {
		        	while (jParser.nextToken() != JsonToken.END_OBJECT) {   
		        		String fieldname_repo = jParser.getCurrentName();
		        		if ("url".equals(fieldname_repo)) {url = jParser.getText();}
		        	}
		        }
		        if ("payload".equals(fieldname)) 
		        {                
		            //current node is repository
		        	while (jParser.nextToken() != JsonToken.END_OBJECT) {    
		 		       String fieldname_pay = jParser.getCurrentName();
		 		       if ("comment".equals(fieldname_pay))
		 		       {
		 		    	  while (jParser.nextToken() != JsonToken.END_OBJECT) { 
		 		    		 String fieldname_comment = jParser.getCurrentName();
		 		    		 if ("position".equals(fieldname_comment)) {position = jParser.getText();}
		 		    		 if ("created_at".equals(fieldname_comment)) {created_at = jParser.getText();}
		 		    		 if ("body".equals(fieldname_comment)) {body = jParser.getText();} 
		 		    		 if ("commit_id".equals(fieldname_comment)) {commit_id = jParser.getText();}
		 		    		 if ("path".equals(fieldname_comment)) {path = jParser.getText();}
		 		    		 if ("user".equals(fieldname_comment)) 
		 		    		 { 
		 		    			while (jParser.nextToken() != JsonToken.END_OBJECT) { 
		 		    				String fieldname_user = jParser.getCurrentName();
		 		    				if ("login".equals(fieldname_user)) 
		 		    				{
		 		    					jParser.nextToken();
		 		    					user = jParser.getText();
		 		    					writebuf = String.format("%d,%s,%s,%s,%s,%s,%s,%s",new Object[]{Integer.valueOf(no),url,commit_id,position,user,path,replace(body),created_at});
		 		    					writer.write(writebuf);
		 		    					writer.newLine();
		 		    					no++;
		 		    					System.out.println(no);
		 		    					System.out.println(url);
		 		    					System.out.println(commit_id);
		 		    					System.out.println(position);
		 		    					System.out.println(user);
		 		    					System.out.println(path);
		 		    					System.out.println(body);
		 		    					System.out.println(created_at);
		 		    				}
		 		    			}
		 		    	     }
		 		    	  }
		 		       }
		        	}
		        }
		        if("actor_attributes".equals(fieldname))
		        {
		        	jParser.skipChildren();
		        }		        
		     } 
		     writer.flush();
		     jParser.close();  		   
		     } catch (JsonGenerationException e) {  		   
		     e.printStackTrace();  	   
		     } catch (JsonParseException e) {  		   
		      e.printStackTrace();  		   
	     } catch (IOException e) {  		   
		     e.printStackTrace();  		   
	     }  		   
	} 
	/*Analysis Events */
	public static void parseEvents(String path,String name,BufferedWriter writer) throws Exception {
		 FileReader reader = new FileReader(path+"\\"+name);
		 int to = name.lastIndexOf('-');
		 System.out.println(name.substring(0, to));
		 String timeline = name.substring(0, to);
		 BufferedReader br = new BufferedReader(reader);
		 String s1 = null;
		 hour_count++;
		 while((s1 = br.readLine()) != null) {
			 all_count++;
			 if(s1.contains("PullRequestEvent")||s1.contains("PullRequestReviewCommentEvent"))
				 pr_count++;	
		 }	 
		 if(hour_count==23)//full day 24 hours
		 {
			 hour_count = 0;
			 System.out.println(all_count);
			 String writebuf = String.format("%d,%d,%d,%s",new Object[]{Integer.valueOf(number_of_record),Integer.valueOf(all_count),Integer.valueOf(pr_count),timeline});
			 writer.write(writebuf);
			 writer.newLine();
			 number_of_record++;
			 all_count = 0;
			 pr_count = 0;
		 } 
		 br.close();
		 reader.close();
	}
	/*Main Processing*/
	public static void main(String[] args) throws Exception {  
		String base_path = "d:\\young\\results\\Json";
		String outpath = "D:\\young\\results\\events.csv";
	    FileWriter fw = new FileWriter(outpath);
		BufferedWriter writer = new BufferedWriter(fw);
		writer.write("no.,all_events,pr_events,timeline");
		writer.newLine();
		number_of_record = 0;
		//end time
		int end_year = 2013;
		int end_month = 1;
		int end_day = 14;
		//start Time
		int start_year = 2013;
		int start_month = 1;
		int start_day = 1;
		all_count = 0;
		pr_count = 0;
		hour_count = 0;
		for(int month=start_month;month<=12+end_month+(end_year-start_year-1)*12;month++)
		{
			int month_real = (month%12==0)?12:(month%12);
			int year = (month<=12)?start_year:(start_year+(month-1)/12);
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
			for(int day=1;day<=end_day;day++)
			{
				String name_year = String.valueOf(year);
				String name_month = (month_real>=10)?String.valueOf(month_real):String.format("0%d", new Object[]{Integer.valueOf(month_real)});
				String name_day = (day>=10)?String.valueOf(day):String.format("0%d", new Object[]{Integer.valueOf(day)});
			   	for(int hour=0;hour<=23;hour++)
			   	{
			   		String name_hour = String.valueOf(hour);
			   		String name = String.format("%s-%s-%s-%s.json",
			   				new Object[]{name_year,name_month,name_day,name_hour});
			   		String path = base_path +"\\"+ name_year +"\\"+ name_month +"\\"+ name_day;
			   		UncompressGZ un = new UncompressGZ();
			   		//un.ungz(path+"\\"+name);
			   		//un.delgz(path+"\\"+name);
			   	    parseEvents(path,name,writer);
			   		System.out.println(year+"-"+month_real+"-"+day+"-"+hour+" ==>OK!");
			   	}
			}
		}		
		//parseCommitComments();
		//inputStream(); 
		writer.flush();
	}
}
