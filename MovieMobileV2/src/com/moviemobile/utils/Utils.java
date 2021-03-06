package com.moviemobile.utils;

import java.io.InputStream;
import java.io.OutputStream;

import com.moviemobile.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;

public class Utils {
	
	private static ProgressDialog progressDialog;
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
    
    public static void showProgressDialog(Context context) {
		progressDialog = new ProgressDialog(context);
		//progressDialog.setMessage(message);
		progressDialog.show();
		progressDialog.setContentView(R.layout.progress);
		//progressDialog.setCancelable(false);
		
		
	
	}
    
    public static void closeProgressDialog(Context context)
    {
    	progressDialog.cancel();
    }
    
    public static boolean isNetworkAvailable(Context context) 
    {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }
    
}