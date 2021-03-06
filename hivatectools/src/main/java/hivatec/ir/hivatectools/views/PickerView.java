package hivatec.ir.hivatectools.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import hivatec.ir.hivatectools.R;
import hivatec.ir.hivatectools.adapters.HivaRecyclerAdapter;
import hivatec.ir.hivatectools.adapters.ItemBinder;
import hivatec.ir.hivatectools.adapters.ItemHolder;
import hivatec.ir.hivatectools.adapters.OnItemClickListener;
import hivatec.ir.hivatectools.helper.ViewUIHelper;

public class PickerView extends RelativeLayout {

	HivaRecyclerAdapter adapter;
	LinearLayoutManager layoutManager;
	RecyclerView recycler;
	RelativeLayout divider;
	SnapHelper snapHelper;
	OnItemPickedListener listener;

	int textColor = Color.BLACK;
	int textSize = ViewUIHelper.spToPx(15, getContext());
	String typeface = "";

	int selectedItem = 0;
	Boolean firstTime = true;
	int itemPadding = ViewUIHelper.dpToPx(10);

	int position = 0;

	private Boolean silentScroll = false;

	public PickerView(Context context) {
		super(context);

		init();
	}

	public PickerView(Context context, AttributeSet attrs) {
		super(context, attrs);

		if (attrs != null) {
			TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PickerView, 0, 0);

			textColor = a.getColor(R.styleable.PickerView_textColor, textColor);
			textSize = a.getDimensionPixelSize(R.styleable.PickerView_textSize, textSize);
			typeface = a.getString(R.styleable.PickerView_typeface);
			position = a.getInt(R.styleable.PickerView_position, position);
			itemPadding = a.getDimensionPixelSize(R.styleable.PickerView_itemPadding, itemPadding);

			a.recycle();
		}

		init();
	}


	private void init() {

		recycler = new RecyclerView(getContext());
		LayoutParams recyclerParams = new LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT ,
				ViewGroup.LayoutParams.MATCH_PARENT );
		recycler.setLayoutParams(recyclerParams);

		layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
		recycler.setLayoutManager(layoutManager);

		recycler.setClipToPadding(false);
		recycler.setOverScrollMode(OVER_SCROLL_NEVER);

		snapHelper = new LinearSnapHelper();
		snapHelper.attachToRecyclerView(recycler);

		adapter = new HivaRecyclerAdapter();
		recycler.setAdapter(adapter);

		recycler.addOnScrollListener(scrollListener);

		this.addView(recycler);


		divider = new RelativeLayout(getContext());
		LayoutParams viewParams = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewUIHelper.dpToPx(60) );
		viewParams.addRule(CENTER_IN_PARENT);
		divider.setLayoutParams(viewParams);

		this.addView(divider);

		View line1 = new View(getContext());
		line1.setBackgroundColor(Color.LTGRAY);
		RelativeLayout.LayoutParams line1Params = new RelativeLayout
				.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1 );
		line1Params.addRule(ALIGN_PARENT_TOP);
		line1.setLayoutParams(line1Params);

		View line2 = new View(getContext());
		line2.setBackgroundColor(Color.LTGRAY);
		RelativeLayout.LayoutParams line2Params = new RelativeLayout
				.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1 );
		line2Params.addRule(ALIGN_PARENT_BOTTOM);
		line2.setLayoutParams(line2Params);

		divider.addView(line1);
		divider.addView(line2);


		adapter.setOnItemClickListener(PickerItemHolder.class, itemClickListener);


		ArrayList<String> items = new ArrayList<>();
		items.add("1");
		items.add("2");
		items.add("3");
		items.add("4");
		items.add("5");
		items.add("6");
		items.add("7");
		items.add("8");
		items.add("9");
		items.add("10");
		items.add("11");
		items.add("12");
		items.add("13");
		items.add("14");
		items.add("15");
		items.add("16");
		items.add("17");
		items.add("18");
		items.add("19");
		items.add("20");
		setItemsStr(items);
	}


	RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {


		@Override
		public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
			super.onScrollStateChanged(recyclerView, newState);

			if(silentScroll) {

				return;
			}

			if(newState == RecyclerView.SCROLL_STATE_SETTLING){

				recyclerView.postDelayed(new Runnable() {
					@Override
					public void run() {

						int halfHeight = PickerView.this.getHeight() / 2;
						int foundPos = -1;
						int lastDiff = Integer.MAX_VALUE;


						for(int i = 0; i < adapter.getItems().size(); i++) {
							RecyclerView.ViewHolder vh = recycler.findViewHolderForAdapterPosition(i);

							if(vh != null) {
								View v = vh.itemView;

								int viewCenter = v.getTop();

								if (lastDiff > Math.abs(halfHeight - viewCenter)) {
									foundPos = i;
									lastDiff = Math.abs(halfHeight - viewCenter);
								}
							}
						}


						if(foundPos >= 0) {
							selectedItem = foundPos;

							if(listener != null && !silentScroll){

								listener.onItemPicked(((PickerItemHolder) adapter.getItems().get(foundPos)).item, foundPos);
							}


						}
					}
				}, 500);


			}
		}

		@Override
		public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
			super.onScrolled(recyclerView, dx, dy);

			transformItems();

		}
	};


	public PickerItem getPickerItem(int index){
		return ((PickerItemHolder) adapter.getItems().get(index)).item;
	}

	public int getSelectedIndex(){
		return selectedItem;
	}

	public void setSelectedIndex(int index){

		recycler.postDelayed(new Runnable() {
			@Override
			public void run() {

				silentScroll = true;
				recycler.smoothScrollToPosition(index);
				silentScroll = false;

			}
		},100);

//		recycler.post(new Runnable() {
//			@Override
//			public void run() {
//				selectedItem = index;
//				silentScroll = true;
//				layoutManager.scrollToPosition(index);
//			}
//		});

	}


	private OnItemClickListener itemClickListener = new OnItemClickListener<PickerItemHolder>() {
		@Override
		public void onItemClicked(PickerItemHolder item, ItemHolder holder) {

			recycler.smoothScrollToPosition(item.index);
		}
	};

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);


	}

	private void transformItems(){
		for(int i = 0; i < recycler.getChildCount(); i++) {
			View v = recycler.getChildAt(i);

			double circleR = PickerView.this.getHeight() / 2;
			double Padding3dy = PickerView.this.getHeight() * 0.3;
			double Padding3dx = PickerView.this.getWidth() * 0.25;

			int halfHeight = PickerView.this.getHeight() / 2;
			//- recyclerView.computeVerticalScrollOffset()
			int distanceToTop = halfHeight - v.getHeight() / 2 - v.getTop();
			double percent = (double) distanceToTop / (double) halfHeight;
			v.setRotationX((float) Math.ceil(percent * 90));

			v.setAlpha(1 - Math.abs((float) percent));

			double deg;
			if(percent > 0) {
				deg = 90 * percent;
			}else{
				deg = 180 - 90 * Math.abs(percent);
			}
			double x = circleR * Math.cos(deg * 0.0174533); //+ PickerView.this.getHeight();

			v.setTranslationY((float) Math.ceil(percent * Math.abs(percent) * 0.5 * Padding3dy));
			v.setTranslationX((float) Math.ceil(percent * percent * 0.4 * Padding3dx) * position);

			v.setScaleX((float) (1 - Math.abs((float) percent * 0.3)));
			v.setScaleY((float) (1 - Math.abs((float) percent * 0.3)));
		}
	}

	private void checkSize(int index, View v){
		//Log.i("pickerView", getHeight() + " <- picker height");

		if(v != null && index == 0) {


			v.post(() -> {
				int height = v.getMeasuredHeight();
				int padding = getHeight() / 2 - height / 2;

				recycler.setPadding(0, padding, 0 , padding);

				RelativeLayout.LayoutParams params = (LayoutParams) divider.getLayoutParams();
				params.height = height;
				divider.setLayoutParams(params);
				divider.setTop(v.getTop());

			});

		}
		//Log.i("pickerView", "-------------");
	}

	public void setItems(ArrayList<PickerItem> items) {

		adapter.clearItems();

		ArrayList<PickerItemHolder> holders = new ArrayList<>();

		int id = 0;

		for(PickerItem item : items) {
			adapter.addItem(new PickerItemHolder(id++, item, this));
		}

		//
		adapter.notifyDataSetChanged();

		if(firstTime && !isInEditMode()) {
			recycler.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
			recycler.setPadding(0, recycler.getMeasuredHeight() / 2, 0, 0);
			firstTime = false;
		}
	}

	public void setItemsStr(ArrayList<String> items) {

		ArrayList pickerItems = new ArrayList();
		int id = 0;
		for(String str : items) {

			FakeItem item = new FakeItem(id++, str);
			pickerItems.add(item);
		}

		setItems(pickerItems);
	}

	public void setOnItemPickedListener(OnItemPickedListener listener){
		this.listener = listener;
	}


	public interface PickerItem {

		int getId();
		String getTitle();
	}

	private class PickerItemHolder implements ItemBinder {


		public int index;
		public PickerItem item;
		private PickerView pickerView;

		public PickerItemHolder(int index, PickerItem item, PickerView pickerView){

			this.index = index;
			this.item = item;
			this.pickerView = pickerView;
		}

		@Override
		public int getResourceId() {
			return R.layout.item_picker;
		}

		@Override
		public void bindToHolder(ItemHolder binder, Object listener) {

			TextView textView = binder.find(R.id.text);
			binder.itemView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
			pickerView.checkSize(index, binder.itemView);

			textView.setText(item.getTitle());

			if(!(textView.getTag() instanceof String)) {

				binder.find(R.id.main).setPadding(pickerView.itemPadding, pickerView.itemPadding,
						pickerView.itemPadding,pickerView.itemPadding);

				textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, pickerView.textSize);
				textView.setTextColor(pickerView.textColor);

				if (typeface != null && !typeface.equals("")) {
					try {
						Typeface t = Typeface.createFromAsset(getContext().getAssets(), String.format("fonts/%s", pickerView.typeface));
						textView.setTypeface(t);
					} catch (Exception e) {
					}
				}

				textView.setTag("");
			}
		}
	}

	private class FakeItem implements PickerItem {

		private final int id;
		private final String title;

		FakeItem(int id, String title){

			this.id = id;
			this.title = title;
		}

		@Override
		public int getId() {
			return id;
		}

		@Override
		public String getTitle() {
			return title;
		}
	}


	public interface OnItemPickedListener {
		void onItemPicked(PickerItem item, int index);
	}
}

