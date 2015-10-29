package com.hpsvse.citydao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hpsvse.city_listview.DBManager;
import com.hpsvse.entity.City;
import com.hpsvse.util.DBHelper;

public class CityDao {
	private DBManager dbHelper;
	private SQLiteDatabase db;
	
	public CityDao(Context context) {
		this.dbHelper = new DBManager(context);
		dbHelper.openDatabase();
		db = SQLiteDatabase.openOrCreateDatabase(DBManager.DB_PATH + "/" + DBManager.DB_NAME, null);
	}
	
	/**
	 * 查询name，返回List
	 * @return
	 */
	public List<String> getCityList(){
		List<String> list = new ArrayList<String>();
		Cursor cursor = db.rawQuery("select name from city order by pinyin", null);
		while(cursor.moveToNext()){
			String cityname = cursor.getString(cursor.getColumnIndex("name"));
			
			list.add(cityname);
		}
		return list;
	}
	
	/**
	 * 查询pinyin，返回List
	 * @return
	 */
	public List<String> getFirstNameList(){
		List<String> list = new ArrayList<String>();
		Cursor cursor = db.rawQuery("select pinyin from city order by pinyin", null);
		while(cursor.moveToNext()){
			String firstname = cursor.getString(cursor.getColumnIndex("pinyin"));
			
			list.add(firstname);
		}
		return list;
	}
	
	
	/**
	 * 查询city，province 返回List
	 * @return
	 */
	public List<City> getNameProvinceList(String names){
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.rawQuery("select name,province from city where name like ? or pinyin like ? or py like ?", new String[]{"%"+names+"%",names+"%","%"+names+"%"});
		while(cursor.moveToNext()){
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String province = cursor.getString(cursor.getColumnIndex("province"));
			City city = new City( name, province);
			list.add(city);
		}
		return list;
	}
	
	
}
