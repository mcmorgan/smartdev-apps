package com.moviemobile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.moviemobile.R;
import com.moviemobile.utils.Adapter;
import com.moviemobile.utils.Constant;
import com.moviemobile.utils.MovieBean;
import com.moviemobile.utils.Utils;
import com.moviemobile.utils.XMLParser;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class NowShowing extends Fragment {
	

    ListView list;
    Adapter adapter;
    public static TextView txtError;
    Context context;
	
	
	ArrayList<MovieBean> arr = null;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_now_showing,
				container, false);
		
		context = getActivity();
		
		
		txtError = (TextView) rootView.findViewById(R.id.txtError);
		Utils.showProgressDialog(context);
		
		list=(ListView) rootView.findViewById(R.id.listNowShowing);
		
		if (Utils.isNetworkAvailable(context))
		{		
			new getMovies().execute();
		}

		else
		{
			Utils.closeProgressDialog(context);
			
			txtError.setVisibility(View.VISIBLE);
			txtError.setText("No Internet Connection");
			
		}
		
		return rootView;
	}
	
	
	
	@Override
    public void onDestroy()
    {
        list.setAdapter(null);
        super.onDestroy();
    }
    
//    public OnClickListener listener=new OnClickListener(){
//        @Override
//        public void onClick(View arg0) {
//            adapter.imageLoader.clearCache();
//            adapter.notifyDataSetChanged();
//        }
//    };
	
	public class getMovies extends AsyncTask<Void, Void,Integer>{
		
		private final String TAG = getMovies.class.getSimpleName();
		ArrayList<String> items = new ArrayList<String>();
		
		public getMovies()
		{
			
		}
		
		
		//private ArrayList<MovieBean> list = new ArrayList<MovieBean>();
		

		
		@Override
		protected void onPostExecute(Integer result)
		{
			 if (Constant.movieJsonArr != null)
			 {
				 
				 //txtCinema.setVisibility(View.VISIBLE);
				 
				 //txtCinema.setText("Now Showing");
				 adapter=new Adapter(getActivity());
				 
			     list.setAdapter(adapter);
		     
		    
			 }
			 else
			 {
				 //txtCinema.setVisibility(View.GONE);
				 //txtError.setVisibility(View.VISIBLE);
				//txtError.setText("No Movies Found");
			 } 
			 Utils.closeProgressDialog(context);
		}
		
		@Override
		protected Integer doInBackground(Void... params)
		{
			
		try
		{
			
			//String movieJSONString = getResponseString(Constant.MOVIE_LIST_URL);
			getXmlData(Constant.CARIB_MOVIES_URL);
			
			//Constant.movieJsonArr = new JSONArray(movieJSONString);
			

			
		}
		catch(Exception e)
		{
			Utils.closeProgressDialog(context);
		}
		
			
			return null;
		}
		
		
		public void getXmlData (String url)
		{

			 ArrayList<HashMap<String, String>> exampleList = new ArrayList<HashMap<String, String>>();
			 
			 JSONArray jsonArr = new JSONArray();
			 JSONObject movieJson = new JSONObject();
			 JSONObject movieJsonCheck = new JSONObject();
			 XMLParser parser = new XMLParser();
			 String xml = parser.getXmlFromUrl(url);
			 String movieJSONString = "";
			 Document doc = parser.getDomElement(xml);
			 NodeList nl = doc.getElementsByTagName("item");
			 String movieUrl="";
			 String strYear="";
			 String notFound = "{\"Response\":\"False\",\"Error\":\"Movie not found!\"}";
			 for (int i = 0; i < nl.getLength(); i++) 
			 {
				 JSONObject json = new JSONObject();
	            // creating new HashMap
	            HashMap<String, String> map = new HashMap<String, String>();
	            Element e = (Element) nl.item(i);
	            
	            String title = parser.getValue(e, "title");
	            title=title.replace(" (3D)" , "");
	            title=title.replace(" " , "+");
	            
	            Calendar now = Calendar.getInstance();
	            int year = now.get(Calendar.YEAR);
	            strYear = String.valueOf(year);
	            
	            for(int m=0;m<=2;m++)
	            {
	            	strYear = String.valueOf(year);
	            	movieUrl ="http://www.omdbapi.com/?&t="+title+"&y="+strYear;
	            	movieJSONString = getResponseString(movieUrl);
	            	try {
	            		if(!movieJSONString.equals(notFound))
	            		{
	            			movieJson = new JSONObject(movieJSONString);
	            			if((movieJson.getString("Type")).equals("movie") && !(movieJson.getString("Plot")).equals("N/A") && !(movieJson.getString("Poster")).equals("N/A"))
	            				break;
	            			else
	            				year--;
	            		}
	            		else
	            		{
	            			year--;
	            		}
					} catch (JSONException e1) {
						
						e1.printStackTrace();
					}
	            }
	            
	            
	            try {
	            	if(!movieJSONString.equals(notFound))
	            	{
		            	
		            	
			            	json.put("poster",movieJson.getString("Poster"));
							json.put("genre",movieJson.getString("Genre"));
							json.put("release_date",movieJson.getString("Released"));
							json.put("rating",movieJson.getString("imdbRating"));
							json.put("runtime",movieJson.getString("Runtime"));
							json.put("actors",movieJson.getString("Actors"));
							json.put("synopsis",movieJson.getString("Plot"));
		          
	            	}
	            	else
	            	{
	            		json.put("poster","");
						json.put("genre","N/A");
						json.put("release_date","N/A");
						json.put("rating","N/A");
						json.put("runtime","N/A");
						json.put("actors","N/A");
						json.put("synopsis","Movie details not found.");
						
	            	}
	            	json.put("id", i);
					json.put("title", parser.getValue(e, "title"));
					json.put("description", parser.getValue(e, "description"));
					
					
					
				} catch (JSONException e1) {

					e1.printStackTrace();
				}


	            jsonArr.put(json);
	            

		     }
			 
			 Constant.movieJsonArr = jsonArr;
		}
		

		
		public String getResponseString (String url) 
		{
			String responseString = "";
			try {
				// Setup the object that makes the http request
				
				HttpClient httpclient = new DefaultHttpClient();
				HttpRequestBase request = new HttpGet(url);
//				request.addHeader(Utils.getBasicAuthenticationHeader(context));
//				request.addHeader("User-Agent", Constant.HTTP_USER_AGENT);
				HttpResponse response = httpclient.execute(request);
				StatusLine statusLine = response.getStatusLine();
				
				// If there was no error in the http request
				if(statusLine.getStatusCode() == HttpStatus.SC_OK){
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					responseString = out.toString();
				} else {
					response.getEntity().getContent().close();
					//Log.i("getResponseString", responseString);
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {
				e.printStackTrace();
				Utils.closeProgressDialog(context);
				
				
				txtError.setVisibility(View.VISIBLE);
				txtError.setText("No Internet Connection");
				
			}
			return responseString;
		}
		
		
	}



		      
		
		


	
    
    
}