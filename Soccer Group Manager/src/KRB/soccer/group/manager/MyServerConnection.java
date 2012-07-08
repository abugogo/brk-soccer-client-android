package KRB.soccer.group.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;


public class MyServerConnection extends AsyncTask<String, Void, String> {
	
	private GroupOfPlayers myGroup;
	public String _result = "Not Yet";
	
	@Override
	protected String doInBackground(String... params) {
		String result = "";
		InputStream is = null;
				
//		try{
//			
//			result = OpenHttpConnection("http://23.23.186.205/json/ser1");
//		}
//		catch (IOException ex){
//			
//		}
		return result;
	}
	
	public GroupOfPlayers GetGroupOfPlayers( String name ){
/*		
		try{
    	InputStream in = OpenHttpConnection("");
		}
		catch(IOException ex){
			
		}
*/		
		GroupOfPlayers group = new DemoSupply().MakeDemoGroup("demo");
		return group;
	}

    private String OpenHttpConnection(String urlString) throws IOException{
       	
    	String responseString =  null;
    	InputStream inputStream = null; 
    	HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(urlString);
		
		try {
			StringEntity myEntity = new StringEntity("{\"id\":\"1\",\"method\":\"getPlayers\",\"params\":[]}");
			httpPost.setEntity(myEntity);

			try {
				HttpResponse httpResponse = httpClient.execute(httpPost);
			
				// According to the JAVA API, InputStream constructor do nothing. 
				//So we can't initialize InputStream although it is not an interface
				inputStream = httpResponse.getEntity().getContent();

				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

				StringBuilder stringBuilder = new StringBuilder();

				String bufferedStrChunk = null;

				while((bufferedStrChunk = bufferedReader.readLine()) != null){
					stringBuilder.append(bufferedStrChunk);
				}

				responseString =  stringBuilder.toString();
				System.out.println("SERVERS RESPONSE:" + responseString);

			} catch (ClientProtocolException cpe) {
				System.out.println("First Exception caz of HttpResponese :" + cpe);
				cpe.printStackTrace();
			} catch (IOException ioe) {
				System.out.println("Second Exception caz of HttpResponse :" + ioe);
				ioe.printStackTrace();
			}

		} catch (UnsupportedEncodingException uee) {
			System.out.println("An Exception given because of UrlEncodedFormEntity argument :" + uee);
			uee.printStackTrace();
		}

    	return responseString;
    }
    
	@Override
	protected void onPostExecute(String result) {
		_result = result;
		super.onPostExecute(result);
		

	}			


}
