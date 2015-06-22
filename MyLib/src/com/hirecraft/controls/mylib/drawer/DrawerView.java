package com.hirecraft.controls.mylib.drawer;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hirecraft.controls.R;
import com.hirecraft.controls.mylib.drawer.structure.DrawerDividerItem;
import com.hirecraft.controls.mylib.drawer.structure.DrawerItem;
import com.hirecraft.controls.mylib.drawer.structure.DrawerProfile;
import com.hirecraft.controls.mylib.drawer.widget.LinearListView;
import com.hirecraft.controls.mylib.drawer.widget.ScrimInsetsScrollView;

/**
 * View to be used with {@link android.support.v4.widget.DrawerLayout} to
 * display a drawer which is fully compliant with the Material Design
 * specification.
 */
public class DrawerView extends ScrimInsetsScrollView implements
		ScrimInsetsScrollView.OnInsetsCallback {
	private DrawerProfile mProfile;

	private DrawerAdapter mAdapter;
	private DrawerItem.OnItemClickListener mOnItemClickListener;

	private LinearLayout layout;

	private FrameLayout frameLayoutProfile;
	private RelativeLayout relativeLayoutProfileContent;
	private ImageView imageViewBackgroundProfile;
	private ImageView imageViewAvatarProfile;
	private TextView textViewNameProfile;
	private TextView textViewDescriptionProfile;

	private LinearListView linearListView;

	private int statusBarHeight = 0;

	public DrawerView(Context context) {
		this(context, null);
	}

	public DrawerView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DrawerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		inflate(context, R.layout.drawer, this);

		findViews();

		setClipToPadding(false);
		setBackgroundColor(getResources().getColor(R.color.drawer_background));
		setInsetForeground(new ColorDrawable(getResources().getColor(
				R.color.scrim)));

		setOnInsetsCallback(this);

		mAdapter = new DrawerAdapter(context, new ArrayList<DrawerItem>());
		linearListView.setAdapter(mAdapter);
	}

	private void findViews() {
		layout = (LinearLayout) findViewById(R.id.mdLayout);

		frameLayoutProfile = (FrameLayout) findViewById(R.id.mdLayoutProfile);
		relativeLayoutProfileContent = (RelativeLayout) findViewById(R.id.mdLayoutProfileContent);
		imageViewBackgroundProfile = (ImageView) findViewById(R.id.mdBackgroundProfile);
		imageViewAvatarProfile = (ImageView) findViewById(R.id.mdAvatarProfile);
		textViewNameProfile = (TextView) findViewById(R.id.mdNameProfile);
		textViewDescriptionProfile = (TextView) findViewById(R.id.mdDescriptionProfile);

		linearListView = (LinearListView) findViewById(R.id.mdLinearListViewPrimary);
	}

	private void updateSpacing() {
		if (mProfile != null && mProfile.getAvatar() != null
				&& mProfile.getName() != null && !mProfile.getName().isEmpty()) {
			frameLayoutProfile.getLayoutParams().height = Math
					.round(getLayoutParams().width / 16 * 9) + statusBarHeight;
			relativeLayoutProfileContent.getLayoutParams().height = Math
					.round(getLayoutParams().width / 16 * 9);

			layout.setPadding(0, 0, 0, 0);
		} else {
			layout.setPadding(0, statusBarHeight, 0, 0);
		}
	}

	private void updateProfile() {
		if (mProfile != null && mProfile.getAvatar() != null
				&& mProfile.getName() != null && !mProfile.getName().isEmpty()) {
			imageViewAvatarProfile.setImageDrawable(mProfile.getAvatar());
			textViewNameProfile.setText(mProfile.getName());

			if (mProfile.getBackground() != null) {
				imageViewBackgroundProfile.setImageDrawable(mProfile
						.getBackground());
			} else {
				imageViewBackgroundProfile
						.setImageDrawable(new ColorDrawable(getResources()
								.getColor(R.color.primary_material_dark)));
			}

			if (mProfile.getDescription() != null
					&& !mProfile.getDescription().equals("")) {
				textViewDescriptionProfile.setVisibility(VISIBLE);
				textViewDescriptionProfile.setText(mProfile.getDescription());
			} else {
				textViewDescriptionProfile.setVisibility(GONE);
			}

			if (mProfile.hasOnProfileClickListener()) {
				frameLayoutProfile.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mProfile.getOnProfileClickListener().onClick(mProfile);
					}
				});

				frameLayoutProfile.setEnabled(true);
			} else {
				frameLayoutProfile.setEnabled(false);
			}
			frameLayoutProfile.setVisibility(VISIBLE);
		} else {
			frameLayoutProfile.setVisibility(GONE);
		}
		updateSpacing();
	}

	private void updateList() {
		if (mAdapter.getCount() == 0) {
			linearListView.setVisibility(GONE);
		} else {
			linearListView.setVisibility(VISIBLE);
		}

		linearListView
				.setOnItemClickListener(new LinearListView.OnItemClickListener() {
					@Override
					public void onItemClick(LinearListView parent, View view,
							int position, long id) {
						DrawerItem item = mAdapter.getItem(position);
						if (!item.isDivider()) {
							if (item.hasOnItemClickListener()) {
								item.getOnItemClickListener().onClick(item,
										item.getId(), position);
							} else {
								if (hasOnItemClickListener()) {
									mOnItemClickListener.onClick(item,
											item.getId(), position);
								}
							}
						}
					}
				});
	}

	/**
	 * Sets a profile to the drawer view
	 * 
	 * @param profile
	 *            Profile to set
	 */
	public DrawerView setProfile(DrawerProfile profile) {
		mProfile = profile;
		updateProfile();
		return this;
	}

	/**
	 * Gets the profile of the drawer view
	 * 
	 * @return Profile of the drawer view
	 */
	private DrawerProfile getProfile() {
		return mProfile;
	}

	/**
	 * Adds items to the drawer view
	 * 
	 * @param items
	 *            Items to add
	 */
	public DrawerView addItems(List<DrawerItem> items) {
		for (DrawerItem item : items) {
			item.attachTo(mAdapter);
		}
		mAdapter.addAll(items);
		updateList();
		return this;
	}

	/**
	 * Adds an item to the drawer view
	 * 
	 * @param item
	 *            Item to add
	 */
	public DrawerView addItem(DrawerItem item) {
		item.attachTo(mAdapter);
		mAdapter.add(item);
		updateList();
		return this;
	}

	/**
	 * Adds a divider to the drawer view
	 */
	public DrawerView addDivider() {
		addItem(new DrawerDividerItem());
		return this;
	}

	/**
	 * Gets all items from the drawer view
	 * 
	 * @return Items from the drawer view
	 */
	public List<DrawerItem> getItems() {
		return mAdapter.getItems();
	}

	/**
	 * Gets an item from the drawer view
	 * 
	 * @param position
	 *            The item position
	 * @return Item from the drawer view
	 */
	public DrawerItem getItem(int position) {
		return mAdapter.getItem(position);
	}

	/**
	 * Gets an item from the drawer view
	 * 
	 * @param id
	 *            The item ID
	 * @return Item from the drawer view
	 */
	public DrawerItem findItemById(int id) {
		for (int i = 0; i < mAdapter.getCount(); i++) {
			if (mAdapter.getItem(i).getId() == id) {
				return mAdapter.getItem(i);
			}
		}
		return null;
	}

	/**
	 * Selects an item from the drawer view
	 * 
	 * @param position
	 *            The item position
	 */
	public DrawerItem selectItem(int position) {
		mAdapter.select(position);
		return null;
	}

	/**
	 * Gets the selected item position of the drawer view
	 * 
	 * @return Position of the selected item
	 */
	public int getSelectedPosition() {
		return mAdapter.getSelectedPosition();
	}

	/**
	 * Selects an item from the drawer view
	 * 
	 * @param id
	 *            The item ID
	 */
	public void selectItemById(int id) {
		for (int i = 0; i < mAdapter.getCount(); i++) {
			if (mAdapter.getItem(i).getId() == id) {
				mAdapter.select(i);
			}
		}
	}

	/**
	 * Removes an item from the drawer view
	 * 
	 * @param item
	 *            Item to remove
	 */
	public DrawerView removeItem(DrawerItem item) {
		item.detach();
		mAdapter.remove(item);
		updateList();
		return this;
	}

	/**
	 * Removes an item from the drawer view
	 * 
	 * @param position
	 *            Position to remove
	 */
	public DrawerView removeItem(int position) {
		mAdapter.getItem(position).detach();
		mAdapter.remove(mAdapter.getItem(position));
		updateList();
		return this;
	}

	/**
	 * Removes all items from the drawer view
	 */
	public DrawerView clearItems() {
		for (DrawerItem item : mAdapter.getItems()) {
			item.detach();
		}
		mAdapter.clear();
		updateList();
		return this;
	}

	/**
	 * Sets an item click listener to the drawer view
	 * 
	 * @param listener
	 *            Listener to set
	 */
	public DrawerView setOnItemClickListener(
			DrawerItem.OnItemClickListener listener) {
		mOnItemClickListener = listener;
		updateList();
		return this;
	}

	/**
	 * Gets the item click listener of the drawer view
	 * 
	 * @return Item click listener of the drawer view
	 */
	public DrawerItem.OnItemClickListener getOnItemClickListener() {
		return mOnItemClickListener;
	}

	/**
	 * Gets whether the drawer view has an item click listener set to it
	 * 
	 * @return True if the drawer view has an item click listener set to it,
	 *         false otherwise.
	 */
	public boolean hasOnItemClickListener() {
		return mOnItemClickListener != null;
	}

	/**
	 * Removes the item click listener from the drawer view
	 */
	public DrawerView removeOnItemClickListener() {
		mOnItemClickListener = null;
		updateList();
		return this;
	}

	@Override
	public void onInsetsChanged(Rect insets) {
		statusBarHeight = insets.top;
		updateSpacing();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		if (w != oldw) {
			// Window width
			int windowWidth = ((Activity) getContext()).getWindow()
					.getDecorView().getWidth();

			// Minus the width of the vertical nav bar
			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				int navigationBarWidthResId = getResources().getIdentifier(
						"navigation_bar_width", "dimen", "android");
				if (navigationBarWidthResId > 0) {
					windowWidth -= getResources().getDimensionPixelSize(
							navigationBarWidthResId);
				}
			}

			// App bar size
			TypedValue typedValue = new TypedValue();
			getContext().getTheme().resolveAttribute(R.attr.actionBarSize,
					typedValue, true);
			int actionBarSize = TypedValue.complexToDimensionPixelSize(
					typedValue.data, getResources().getDisplayMetrics());

			int width = windowWidth - actionBarSize;

			int maxWidth = getResources().getDimensionPixelSize(
					R.dimen.drawer_max_width);

			getLayoutParams().width = Math.min(width, maxWidth);

			updateSpacing();
		}
	}
}
