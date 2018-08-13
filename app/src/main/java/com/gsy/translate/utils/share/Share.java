package com.gsy.translate.utils.share;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gsy.translate.R;
import com.gsy.translate.utils.Constants;
import com.gsy.translate.utils.CustomToast;
import com.gsy.translate.utils.Tools;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class Share implements WbShareCallback{

	Activity context;
	Dialog dialog;//分享的dialog
	public IWXAPI api;
	public Tencent mTencent;
	private WbShareHandler shareHandler;
	String url;
	String title;
	String brief;
	Bitmap share_bitmap;
	String share_bitmap_url;
	Class<? extends Activity> class1;
	private BaseUiListener mBaseUiListener;

	public Share(Activity context, String url,String title,String brief,String share_bitmap_url){
		this.context = context;
		this.url = url;
		this.title = title;
		this.brief = brief;
		this.share_bitmap_url = share_bitmap_url;
	}
	
	/**分享*/
	public void share(){
		View view = LayoutInflater.from(context).inflate(R.layout.share_dialog, null);
		dialog = new Dialog(context,R.style.transparentFrameWindowStyle);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(view);
		ShareClickListener shareClickListener = new ShareClickListener();
		LinearLayout qqhaoyou = (LinearLayout) view.findViewById(R.id.qqhaoyou);
		LinearLayout qqkongjian = (LinearLayout) view.findViewById(R.id.qqkongjian);
		LinearLayout sinaweibo = (LinearLayout) view.findViewById(R.id.sinaweibo);
		LinearLayout weixinhaoyou = (LinearLayout) view.findViewById(R.id.weixinhaoyou);
		LinearLayout pengyouquan = (LinearLayout) view.findViewById(R.id.pengyouquan);
		Button close = (Button) view.findViewById(R.id.share_dialog_close);
		qqhaoyou.setOnClickListener(shareClickListener);
		qqkongjian.setOnClickListener(shareClickListener);
		sinaweibo.setOnClickListener(shareClickListener);
		weixinhaoyou.setOnClickListener(shareClickListener);
		pengyouquan.setOnClickListener(shareClickListener);
		close.setOnClickListener(shareClickListener);
		dialog.setCanceledOnTouchOutside(true);
		Window window = dialog.getWindow();
		// 设置显示动画
		window.setWindowAnimations(R.style.main_menu_animstyle);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		wl.y = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
		// 以下这两句是为了保证按钮可以水平满屏
		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		// 设置显示位置
		dialog.onWindowAttributesChanged(wl);
		dialog.show();
	}


	class ShareClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.qqhaoyou:
				mTencent = Tencent.createInstance(Constants.QQ_APP_ID,context.getApplicationContext());
				Bundle bundle = new Bundle();
				//这条分享消息被好友点击后的跳转URL。
				if(url!=null) bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
				//分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_	SUMMARY不能全为空，最少必须有一个是有值的。
				if(title!=null) bundle.putString(QQShare.SHARE_TO_QQ_TITLE, title);
				//分享的图片URL
				if(share_bitmap_url!=null) bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,share_bitmap_url);
				//分享的消息摘要，最长50个字
				if(brief!=null) bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY,  brief);
				/*//手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
				bundle.putString(Constants.PARAM_APPNAME, "??我在测试");*/
				//bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
				if(mBaseUiListener==null) mBaseUiListener = new BaseUiListener();
				mTencent.shareToQQ(context, bundle ,mBaseUiListener);
				dialog.dismiss();
				break;
			case R.id.qqkongjian:
				mTencent = Tencent.createInstance(Constants.QQ_APP_ID, context.getApplicationContext());
				Bundle params = new Bundle();
				params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT );
				if(title!=null) params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);//必填
				if(brief!=null) params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, brief);//选填
				if(url!=null) params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, url);//必填
			    ArrayList<String> images = new ArrayList<String>();
			    images.add(share_bitmap_url);
			    params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, images);
				if(mBaseUiListener==null) mBaseUiListener = new BaseUiListener();
			    mTencent.shareToQzone(context, params, mBaseUiListener);
			    dialog.dismiss();
				break;
			case R.id.sinaweibo:
				WbSdk.install(context, new AuthInfo(context,Constants.WB_APP_ID,"http://www.gsy.com",""));
				shareHandler = new WbShareHandler(context);
				shareHandler.registerApp();
				final WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
				final TextObject textObject = new TextObject();
				textObject.text = brief;
				textObject.title = title;
				textObject.actionUrl = url;
				final ImageObject imageObject = new ImageObject();
				MyInterface myInterfaceWb = new MyInterface(){
					@Override
					public void callBack() {
						Bitmap thumbBmp = Bitmap.createScaledBitmap(share_bitmap, 100, 100, true);
						imageObject.setImageObject(thumbBmp);
						weiboMultiMessage.textObject = textObject;
						weiboMultiMessage.imageObject = imageObject;
						shareHandler.shareMessage(weiboMultiMessage,false);
					}
				};
				if(share_bitmap==null)
					GetLocalOrNetBitmap(share_bitmap_url,myInterfaceWb);
				else
					myInterfaceWb.callBack();


				dialog.dismiss();
				break;
			case R.id.weixinhaoyou:
				api = WXAPIFactory.createWXAPI(context, Constants.WX_APP_ID, true);
				api.registerApp(Constants.WX_APP_ID);
				WXWebpageObject webpage = new WXWebpageObject();
			    if(url!=null) webpage.webpageUrl = url;  
				final WXMediaMessage msg = new WXMediaMessage(webpage);
				if(title!=null) msg.title = title;
				if(brief!=null) msg.description = brief;
				MyInterface myInterface = new MyInterface(){
					@Override
					public void callBack() {
						Bitmap thumbBmp = Bitmap.createScaledBitmap(share_bitmap, 100, 100, true);
						msg.setThumbImage(thumbBmp);
						SendMessageToWX.Req req = new SendMessageToWX.Req();
						req.scene = SendMessageToWX.Req.WXSceneSession;
						req.message = msg;
						req.transaction = "share";
						api.sendReq(req);
					}
				};
				if(share_bitmap==null){
					GetLocalOrNetBitmap(share_bitmap_url,myInterface);
				}else{
					myInterface.callBack();
				}
				dialog.dismiss();
				break;
			case R.id.pengyouquan:
				api = WXAPIFactory.createWXAPI(context, Constants.WX_APP_ID, true);
				api.registerApp(Constants.WX_APP_ID);
				WXWebpageObject webpage2 = new WXWebpageObject();  
				if(url!=null) webpage2.webpageUrl = url;  
				final WXMediaMessage msg2 = new WXMediaMessage(webpage2);
				if(title!=null) msg2.title = title;
				if(brief!=null) msg2.description = brief;
				MyInterface myInterface2 = new MyInterface(){
					@Override
					public void callBack() {
						Bitmap thumbBmp2 = Bitmap.createScaledBitmap(share_bitmap, 100, 100, true);
						msg2.setThumbImage(thumbBmp2);
						SendMessageToWX.Req req2 = new SendMessageToWX.Req();
						req2.scene = SendMessageToWX.Req.WXSceneTimeline;
						req2.message = msg2;
						req2.transaction = "share";
						api.sendReq(req2);
					}
				};
				if(share_bitmap==null){
					GetLocalOrNetBitmap(share_bitmap_url,myInterface2);
				}else{
					myInterface2.callBack();
				}
				dialog.dismiss();
				break;
			case R.id.share_dialog_close:
				dialog.dismiss();
				break;
			default:
				break;
			}
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data){
		if(mTencent!=null)
			mTencent.onActivityResultData(requestCode,resultCode,data,mBaseUiListener);
	}
	public void onNewIntent(Intent intent){
		if(shareHandler!=null)
			shareHandler.doResultIntent(intent,this);
	}

	public void GetLocalOrNetBitmap(final String url,final MyInterface myInterface)  {
		new Thread(new Runnable() {
			@Override
			public void run() {
			try {
				InputStream in = new URL(url).openStream();
				share_bitmap = BitmapFactory.decodeStream(in);
				Matrix matrix = new Matrix();
				while(share_bitmap.getWidth()>100&&share_bitmap.getHeight()>100){
					matrix.postScale(0.6f,0.6f);
					share_bitmap = Bitmap.createBitmap(share_bitmap, 0, 0, share_bitmap.getWidth(), share_bitmap.getHeight(),matrix,true);
				}
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				share_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
				int options = 100;
				while ( baos.toByteArray().length / 1024>30) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
					baos.reset();//重置baos即清空baos
					share_bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
					options -= 10;//每次都减少10
				}
				ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
				share_bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
				myInterface.callBack();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			}
		}).start();
    }
	private class BaseUiListener implements IUiListener {
		@Override
		public void onError(UiError e) {
			CustomToast.makeText(context,"分享失败", Toast.LENGTH_SHORT).show();
		}
		@Override
		public void onCancel() {
			CustomToast.makeText(context,"用户取消", Toast.LENGTH_SHORT).show();
		}
		@Override
		public void onComplete(Object arg0) {
			CustomToast.makeText(context,"分享成功", Toast.LENGTH_SHORT).show();
		}
	}


	@Override
	public void onWbShareSuccess() {
		CustomToast.makeText(context,"分享成功", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onWbShareCancel() {
		CustomToast.makeText(context,"用户取消", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onWbShareFail() {
		CustomToast.makeText(context,"分享失败", Toast.LENGTH_SHORT).show();
	}
    public interface MyInterface{
		void callBack();
	}
}
