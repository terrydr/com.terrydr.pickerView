package com.terrydr.pickerview;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.terrydr.pickerview.wheelview.WheelView;
import com.terrydr.pickerview.wheelview.adapter.ArrayWheelAdapter;

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
    private TextView hideView;
    private WheelView wheelView;
    private LinearLayout pickerview_bottomLayou;
    private Button confirmButton;
    private Button cancleButton;
	private CallbackContext callbackContext;
    private String value = "[{key:'男'},{key:'女'}]";
    private ArrayWheelAdapter<String> arrayWheelAdapter;
    
	/**
	 * 初始化数据
	 */
	@Override
    protected void pluginInitialize() {
        super.pluginInitialize();
        initView();
    }
	
    private void initView(){
    	hideView = new TextView(this.cordova.getActivity());
    	hideView.setVisibility(View.GONE);
    	this.cordova.getActivity().addContentView(hideView, null);
		inflater = (LayoutInflater) this.cordova.getActivity().getSystemService(this.cordova.getActivity().LAYOUT_INFLATER_SERVICE);
		
		popupWindowView = inflater.inflate(R.layout.pickerview_popupwindow, null);
		pickerview_bottomLayou = (LinearLayout) popupWindowView.findViewById(R.id.pickerview_bottomLayou);
		popupWindow = new PopupWindow(popupWindowView,LayoutParams.MATCH_PARENT, 700,true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		
		confirmButton = (Button) popupWindowView.findViewById(R.id.confirmButton);
		cancleButton = (Button) popupWindowView.findViewById(R.id.cancleButton);
		confirmButton.setOnClickListener(this);
		cancleButton.setOnClickListener(this);

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
			args = value;
		}

		// 弹出窗口显示内容视图,默认以锚定视图的左下角为起点，这里为点击按钮
		popupWindow.showAtLocation(hideView, Gravity.BOTTOM, 0, 0);
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
		ArrayWheelAdapter<String> arrayWheelAdapter = new ArrayWheelAdapter<String>(
				this.cordova.getActivity(), arrayObj);
		wheelView.setViewAdapter(arrayWheelAdapter);
		// wheelView.setCyclic(true);
		// wheelView.addScrollingListener(scrollListener);
		// wheelView.setVisibility(View.GONE);

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT));
		wheelView.setLayoutParams(layoutParams);
		pickerview_bottomLayou.addView(wheelView);
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
		case R.id.confirmButton:
			System.out.println("点击了确定按钮:" + arrayWheelAdapter.getItemText(wheelView.getCurrentItem()));
			break;
		case R.id.cancleButton:
			popupWindow.dismiss();
			break;
		default:
			break;
		}
		
	}
}
