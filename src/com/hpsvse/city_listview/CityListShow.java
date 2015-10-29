package com.hpsvse.city_listview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hpsvse.city_listview.BladeView.OnItemClickListener;
import com.hpsvse.citydao.CityDao;
import com.hpsvse.entity.City;

import android.opengl.Visibility;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.view.ActionProvider.VisibilityListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class CityListShow extends Activity {

	private static final String FORMAT = "^[a-z,A-Z].*$";
	private PinnedHeaderListView mListView;
	private BladeView mLetter;
	private CitysAdapter mAdapter;
	private String[] datas;
	private String[] citys;
	// 首字母集
	private List<String> mSections;
	// 根据首字母存放数据
	private Map<String, List<String>> mMap;
	// 首字母位置集
	private List<Integer> mPositions;
	// 首字母对应的位置
	private Map<String, Integer> mIndexer;
	
	EditText searchEditText;
	ImageView searchemptyImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city_listview);
		searchEditText = (EditText) findViewById(R.id.search_edit);
		searchemptyImageView = (ImageView) findViewById(R.id.search_empty);
		initData();
		initView();
		setView();
	}

	private void setView() {
		searchEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				ListView listView = (ListView) findViewById(R.id.search_list);
				CityDao cityDao = new CityDao(CityListShow.this);
				List<City> list = cityDao.getNameProvinceList(searchEditText.getText().toString());
				if(searchEditText.getText().toString().equals(""))
				{
					listView.setVisibility(View.INVISIBLE);
					mListView.setVisibility(View.VISIBLE);
					mLetter.setVisibility(View.VISIBLE);
						
				}
				else
				{
					listView.setVisibility(View.VISIBLE);
					mListView.setVisibility(View.INVISIBLE);
					mLetter.setVisibility(View.INVISIBLE);
					List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
					for (int i = 0; i < list.size(); i++) {
						// 给键值对Map依依赋值，数据来自于数组
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("name", list.get(i).getName());
						map.put("province", list.get(i).getProvince());
						// 在把Map里的数组添加到list中
						listMap.add(map);
					}
					// 实例化一个SimpleAdapter并设置所有参数
					SimpleAdapter simpleAdapter = new SimpleAdapter(CityListShow.this, listMap,
							R.layout.search_city_item, new String[] { "name", "province" },
							new int[] { R.id.column_title, R.id.search_province });
					listView.setAdapter(simpleAdapter);
					if(list.size()==0)
					{
						searchemptyImageView.setVisibility(View.VISIBLE);
					}
					else{
						searchemptyImageView.setVisibility(View.INVISIBLE);
					}
				}
				
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	private void initData() {
		CityDao cityDao = new CityDao(CityListShow.this);
		List<String> data = cityDao.getFirstNameList();
		datas = new String[data.size()];
		int k = 0;
		for (String item : data) {
			datas[k] = item;
			k++;
		}

		List<String> city = cityDao.getCityList();
		citys = new String[city.size()];
		int f = 0;
		for (String item : city) {
			citys[f] = item;
			f++;
		}
		
		// datas = (String[]) data.toArray();
		mSections = new ArrayList<String>();
		mMap = new HashMap<String, List<String>>();
		mPositions = new ArrayList<Integer>();
		mIndexer = new HashMap<String, Integer>();
		for (int i = 0; i < datas.length; i++) {
			String firstName = datas[i].substring(0, 1).toUpperCase();
			if (firstName.matches(FORMAT)) {
				if (mSections.contains(firstName)) {
					mMap.get(firstName).add(citys[i]);
				} else {
					mSections.add(firstName);
					List<String> list = new ArrayList<String>();
					list.add(citys[i]);
					mMap.put(firstName, list);
				}
			} else {
				if (mSections.contains("#")) {
					mMap.get("#").add(citys[i]);
				} else {
					mSections.add("#");
					List<String> list = new ArrayList<String>();
					list.add(citys[i]);
					mMap.put("#", list);
				}
			}
		}

		Collections.sort(mSections);
		int position = 0;
		for (int i = 0; i < mSections.size(); i++) {
			mIndexer.put(mSections.get(i), position);// 存入map中，key为首字母字符串，value为首字母在listview中位置
			mPositions.add(position);// 首字母在listview中位置，存入list中
			position += mMap.get(mSections.get(i)).size();// 计算下一个首字母在listview的位置
		}
	}

	//这个方法主要实现联动效果和显示所有城市
	private void initView() {
		// TODO Auto-generated method stub
		mListView = (PinnedHeaderListView) findViewById(R.id.friends_display);
		mLetter = (BladeView) findViewById(R.id.friends_myletterlistview);
		mLetter.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(String s) {
				if (mIndexer.get(s) != null) {
					mListView.setSelection(mIndexer.get(s));
				}
			}
		});
		mAdapter = new CitysAdapter(this, citys, mSections, mPositions);
		mListView.setAdapter(mAdapter);
		mListView.setOnScrollListener(mAdapter);
		mListView.setPinnedHeaderView(LayoutInflater.from(this).inflate(
				R.layout.listview_head, mListView, false));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
