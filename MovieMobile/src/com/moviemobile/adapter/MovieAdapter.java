package com.moviemobile.adapter;

import java.util.ArrayList;

import com.moviemobile.R;
import com.moviemobile.utils.MovieBean;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MovieAdapter extends ArrayAdapter<Object>{
	
	private ArrayList<Object> items = new ArrayList<Object>();
	private LayoutInflater inflater;
	Context context;
	MovieBean mbean;
	
	public MovieAdapter(Context context, int textViewResourceId,ArrayList<Object> item) {
		super(context, textViewResourceId, item);
		this.context = context;
		this.items = item;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			v = inflater.inflate(R.layout.movie_list_item, null);
		}
		
		mbean = (MovieBean) items.get(position);
		System.out.println(" ide : "+ mbean.movieId);
		System.out.println(" movie : "+ mbean.movieTitle);
		
		if(mbean!=null){
			 
			TextView txtTitle = (TextView) v.findViewById(R.id.txtMovieTitle);
			
			txtTitle.setText(mbean.movieTitle);
			
			v.setTag(mbean.movieId);

		}	
		
		return v;
	}

}
