/*
 * Copyright (C) 2011 Patrik �kerfeldt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.viewflow;

import com.ky.mainactivity.BaseActivity;
import com.ky.mainactivity.NewDetailActivity;
import com.ky.mainactivity.VedioDetailActivity;
import com.ky.passenger.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private int[] images;
	// 定义一个字符串来表示当前传过来的页面,默认是从新闻里面传过来的
	private String fromWhere = "news";

	// int []
	// imageArray={R.drawable.image_new_top_one,R.drawable.image_new_top_two,R.drawable.image_news_top_three};
	public ImageAdapter(Context context, int[] image, String fromWhere) {
		mContext = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		images = image;
		this.fromWhere = fromWhere;
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE; // 返回很大的�?�使得getView中的position不断增大来实现循�?
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.news_image_item, null);
		}
		((ImageView) convertView.findViewById(R.id.imgView))
				.setImageResource(images[position % images.length]);
		// text_Main.setText(strName[position % strName.length].toString());
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 在这里我们应该判断一下是新闻传过来的值还是视频传过来的值
				if (fromWhere.equals("news")) {
					IntentNewsDetail(position);
				} else {
					IntentVedioDetail(position);
				}

			}
		});
		return convertView;
	}

	// 点击跳转到新闻的页面
	public void IntentNewsDetail(int position) {
		Intent intent = new Intent();
		intent.setClass(mContext, NewDetailActivity.class);
		intent.putExtra("image_id", images[position % images.length]);
		intent.putExtra("title", strName[position % strName.length]);
		intent.putExtra("type", "news");
		intent.putExtra("time", "2014-01-01");
		intent.putExtra("description", des);
		intent.putExtra("src", "百度客户端");
		mContext.startActivity(intent);
	}

	// 点击跳转到视屏的页面
	public void IntentVedioDetail(int position) {
		Intent intent = new Intent();
		intent.setClass(mContext, VedioDetailActivity.class);
		intent.putExtra("image_id", images[position % images.length]);
		intent.putExtra("title", strName[position % strName.length]);
		intent.putExtra("type", "vedio");
		intent.putExtra("actor", "刘亦菲");
		intent.putExtra("description", des);
		intent.putExtra("director", "黄晓明");
		intent.putExtra("years", "19世纪");
		mContext.startActivity(intent);
	}

	String[] strName = { "每一步只为你", "敢死队", "女特警，打手无虚发" };
	String des = "敢死队（The Expendables）是一部由西尔维斯特·史泰龙自编、自导、自演的美国动作片。电影是向1980年代至1990年代的动作电影致敬，"
			+ "也因此汇聚了大批动作片巨星，包括史泰龙、施瓦辛格、杰森·斯坦森、李连杰等美国政府想雇人深入南美某国推翻当地的独裁统治，但是却没人敢接受这样的任务，最终这个任务落到史泰龙率领的一支特别行动小组身上。";

	// 点击的跳转到图片详情的页面
	public void GetImage(int position) {

		Intent intent = new Intent(mContext, DetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("image_id", images[position % images.length]);
		intent.putExtras(bundle);
		mContext.startActivity(intent);
	}
}
