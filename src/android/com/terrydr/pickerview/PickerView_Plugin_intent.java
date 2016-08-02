package com.terrydr.pickerview;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.terrydr.pickerview.wheelview.OnWheelChangedListener;
import com.terrydr.pickerview.wheelview.OnWheelScrollListener;
import com.terrydr.pickerview.wheelview.WheelView;
import com.terrydr.pickerview.wheelview.adapter.ArrayWheelAdapter;
import com.terrydr.resource.R;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * cordovaplugin启动插件类
 *
 */
public class PickerView_Plugin_intent extends CordovaPlugin implements OnClickListener{
	private final static String TAG = "PickerView_Plugin_intent";
	private String infos;
	private LayoutInflater inflater;
	//自定义的弹出框类
    private PopupWindow popupWindow;
    private View popupWindowView;
    private TextView hideView,pickerview_confirm,pickerview_cancle;
    private WheelView wheelView;
    private LinearLayout pickerview_bottomLayou;
	private CallbackContext callbackContext;
    private String value = "[{key:'男'},{key:'女'},{key:'女'}]";
    private ArrayWheelAdapter<String> arrayWheelAdapter;
    private String result;
    
	/**
	 * 启动插件初始化方法
	 */
	@Override
    protected void pluginInitialize() {
        super.pluginInitialize();
        Log.e(TAG, "tdShowPickerView1:" + callbackContext);
//        initView();
    }
	
	/**
	 * 初始化数据
	 */
    private void initView(){
    	hideView = new TextView(this.cordova.getActivity());
    	hideView.setVisibility(View.GONE);
    	
//    	this.cordova.getActivity().setContentView(hideView);
		inflater = (LayoutInflater) this.cordova.getActivity().getSystemService(this.cordova.getActivity().LAYOUT_INFLATER_SERVICE);
		
		popupWindowView = inflater.inflate(R.layout.pickerview_popupwindow, null);
		pickerview_bottomLayou = (LinearLayout) popupWindowView.findViewById(R.id.pickerview_bottomLayou);
		int height = this.cordova.getActivity().getWindowManager().getDefaultDisplay().getHeight();
		int getHeight = height * 650 / 1920;
		Log.i("MainActivity", "getHeight:" + getHeight);
		popupWindow = new PopupWindow(popupWindowView,LayoutParams.MATCH_PARENT, getHeight,true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		
		pickerview_confirm = (TextView) popupWindowView.findViewById(R.id.pickerview_confirm);
		pickerview_cancle = (TextView) popupWindowView.findViewById(R.id.pickerview_cancle);
		pickerview_confirm.setOnClickListener(this);
		pickerview_cancle.setOnClickListener(this);

		//设置PopupWindow的弹出和消失效果
		popupWindow.setAnimationStyle(R.style.pickerview_popupAnimation);
    }
    
	/**
	 * 启动插件的主要方法
	 */
	@Override
	public boolean execute(String action, org.json.JSONArray args,
			CallbackContext callbackContext) throws org.json.JSONException {
		
		if (action.equals("tdShowPickerView")) { 
			this.callbackContext = callbackContext;
			Log.e(TAG, "tdShowPickerView:" + callbackContext);
			initView();
			infos = args.getString(0);
			this.showPickerView(infos);
			return true;
		}
		return true;
	}

	/**
	 * 解析json数据并动态展示pickerview
	 * @param args json格式数据  如：  "[{key:'男'},{key:'女'}]";
	 */
	private void showPickerView(String args) {
		if(args==null || "".equals(args)){
			return ;
		}

		JSONArray dataArray = null;
		String[] arrayObj = null;
		try {
			dataArray = new JSONArray(args);
			arrayObj = new String[dataArray.length()];
			for (int i = 0; i < dataArray.length(); i++) {
				JSONObject jsonObj = (JSONObject) dataArray.get(i);
				String keyValue = jsonObj.getString("key");
				arrayObj[i] = keyValue;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		wheelView = new WheelView(this.cordova.getActivity().getBaseContext());
		arrayWheelAdapter = new ArrayWheelAdapter<String>(
				this.cordova.getActivity(), arrayObj);
		wheelView.setViewAdapter(arrayWheelAdapter);
//		wheelView.addChangingListener(onChangedLsitener);
		wheelView.addScrollingListener(scrollListener);
		// wheelView.setCyclic(true);
		// wheelView.setVisibility(View.GONE);

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT));
		wheelView.setLayoutParams(layoutParams);
		pickerview_bottomLayou.addView(wheelView);
		
		// 弹出窗口显示内容视图,默认以锚定视图的左下角为起点，这里为点击按钮
		popupWindow.showAtLocation(hideView, Gravity.BOTTOM, 0, 0);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		switch (resultCode) { // resultCode为回传的标记，回传的是RESULT_OK
		case 0:
			break;
		case 5:
			Bundle b = intent.getExtras();
			String result_Json = b.getString("result_Json");
			org.json.JSONObject result = null;
			try {
				result = new org.json.JSONObject(result_Json);
			} catch (JSONException e) {
				Log.e(TAG, "String to Json error!",e);
			}
			callbackContext.success(result);
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pickerview_confirm:
			if(result==null){
				result = String.valueOf(arrayWheelAdapter.getItemText(wheelView.getCurrentItem()));
			}
			callbackContext.success(result);
			Log.i(TAG, "result:" + result);
			popupWindow.dismiss();
//			this.cordova.getActivity().finish();
			break;
		case R.id.pickerview_cancle:
			popupWindow.dismiss();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 内容改变事件
	 */
	private OnWheelChangedListener onChangedLsitener = new OnWheelChangedListener() {
		
		@Override
		public void onChanged(WheelView wheel, int oldValue, int newValue) {
			result = String.valueOf(arrayWheelAdapter.getItemText(wheel.getCurrentItem()));
			Log.i(TAG, "onChangedLsitenerResult:" + result);
		}
	};
	
	/**
	 * 添加滚动事件，调用adapter中的显示文字
	 */
	private OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
		@Override
		public void onScrollingStarted(WheelView wheel) {

		}

		@Override
		public void onScrollingFinished(WheelView wheel) {
			result = String.valueOf(arrayWheelAdapter.getItemText(wheel.getCurrentItem()));
			Log.i(TAG, "scrollListenerResult:" + result);
		}
	};
}
