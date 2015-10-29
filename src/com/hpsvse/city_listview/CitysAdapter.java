package com.hpsvse.city_listview;

import java.util.Arrays;
import java.util.List;

import com.hpsvse.city_listview.PinnedHeaderListView.PinnedHeaderAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class CitysAdapter extends BaseAdapter implements SectionIndexer,
		PinnedHeaderAdapter, OnScrollListener {
	private int mLocationPosition = -1;
	private String[] mDatas;
	// Ê××ÖÄ¸¼¯
	private List<String> mFriendsSections;
	private List<Integer> mFriendsPositions;
	private LayoutInflater inflater;

	public CitysAdapter(Context context, String[] datas,
			List<String> friendsSections, List<Integer> friendsPositions) {
		// TODO Auto-generated constructor stub
		inflater = LayoutInflater.from(context);
		mDatas = datas;
		mFriendsSections = friendsSections;
		mFriendsPositions = friendsPositions;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDatas.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mDatas[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		int section = getSectionForPosition(position);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listview_item, null);
		}
		LinearLayout mHeaderParent = (LinearLayout) convertView
				.findViewById(R.id.friends_item_header_parent);
		TextView mHeaderText = (TextView) convertView
				.findViewById(R.id.friends_item_header_text);
		if (getPositionForSection(section) == position) {
			mHeaderParent.setVisibility(View.VISIBLE);
			mHeaderText.setText(mFriendsSections.get(section));
		} else {
			mHeaderParent.setVisibility(View.GONE);
		}
		TextView textView = (TextView) convertView
				.findViewById(R.id.friends_item);
		textView.setText(mDatas[position]);
		return convertView;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		if (view instanceof PinnedHeaderListView) {
			((PinnedHeaderListView) view).configureHeaderView(firstVisibleItem);
		}
	}

	@Override
	public int getPinnedHeaderState(int position) {
		int realPosition = position;
		if (realPosition < 0
				|| (mLocationPosition != -1 && mLocationPosition == realPosition)) {
			return PINNED_HEADER_GONE;
		}
		mLocationPosition = -1;
		int section = getSectionForPosition(realPosition);
		int nextSectionPosition = getPositionForSection(section + 1);
		if (nextSectionPosition != -1
				&& realPosition == nextSectionPosition - 1) {
			return PINNED_HEADER_PUSHED_UP;
		}
		return PINNED_HEADER_VISIBLE;
	}

	@Override
	public void configurePinnedHeader(View header, int position, int alpha) {
		// TODO Auto-generated method stub
		int realPosition = position;
		int section = getSectionForPosition(realPosition);
		String title = (String) getSections()[section];
		((TextView) header.findViewById(R.id.friends_list_header_text))
				.setText(title);
	}

	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		return mFriendsSections.toArray();
	}

	@Override
	public int getPositionForSection(int section) {
		if (section < 0 || section >= mFriendsSections.size()) {
			return -1;
		}
		return mFriendsPositions.get(section);
	}

	@Override
	public int getSectionForPosition(int position) {
		// TODO Auto-generated method stub
		if (position < 0 || position >= getCount()) {
			return -1;
		}
		int index = Arrays.binarySearch(mFriendsPositions.toArray(), position);
		return index >= 0 ? index : -index - 2;
	}
}
