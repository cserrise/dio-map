package de.unima.ki.dio.rockit;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

public class PyRockitAPI {
	static final int DEFAULT_ITERATIONS = 1000000;
	static final double DEFAULT_GAP = 0.1;

	public static void  runMapRockit(File input, File data, String gap) throws Exception{
		runRockIt(mapEntity(input, data, gap));
	}
	
	public static List<String> loglines = new ArrayList<String>();
	public static List<String> outputlines = new ArrayList<String>();



	private static MultipartEntity mapEntity(File input, File data, String gap) throws UnsupportedEncodingException{
		MultipartEntity mpEntity = new MultipartEntity();
		mpEntity.addPart("input", new FileBody(input, "multipart"));
		mpEntity.addPart("data", new FileBody(data, "multipart"));
		mpEntity.addPart("inference", new StringBody("map"));
		mpEntity.addPart("gap", new StringBody(gap));
		return mpEntity;

	}

	private static void runRockIt( MultipartEntity mpEntity) throws Exception{
		String id = startRockItFile(mpEntity);
		String state = PyExecutor.getAction(id, "state");

		while(state.startsWith("waiting")) {
			Thread.currentThread();
			Thread.sleep(2500);
			state = PyExecutor.getAction(id, "state");
		}
		int position = 0;
		while(state.equals("running")) {
			Thread.currentThread();
			Thread.sleep(2500);
			String lastLog =  PyExecutor.getAction(id, "log").substring(position).trim();
			position = PyExecutor.getAction(id, "log").length();
			state = PyExecutor.getAction(id, "state");
		}



		if(state.equals("complete")) {
			outputlines = PyExecutor.getResult(id, "rOutput");
			loglines = PyExecutor.getResult(id, "log");

		} else if(state.equals("error")){
			throw new Exception("Rockit error");
		}
	
	}  
	
	public static List<String> getLoglines() {
		return loglines;
	}
	
	public static List<String> getMAPState() {
		return outputlines;
	}
	
	
	public static double getObjective() {
		List<String> lines = loglines;
		String prefix = "Best objective ";
		String objective = "";
        for (String line : lines) {
        	if (line.startsWith(prefix)) {
        		objective = line.substring(prefix.length(), line.indexOf(','));
        		
        	}
        }
        return Double.parseDouble(objective);
	}

	private synchronized static String startRockItFile(MultipartEntity mpEntity) throws Exception
	{
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

		HttpPost httppost = new HttpPost(PyExecutor.server + "startRockIt");

		httppost.setEntity(mpEntity);

		HttpResponse response = httpclient.execute(httppost);
		HttpEntity resEntity = response.getEntity();
		if(response.getStatusLine().getStatusCode() != 200){
			throw new Exception("Rest Interface response: " + response.getStatusLine().toString());    	
		}

		String result = null;
		if(resEntity != null) {
			result = EntityUtils.toString(resEntity);
			EntityUtils.consume(resEntity);
		}
		httpclient.getConnectionManager().shutdown();
		return result;
	}
}
