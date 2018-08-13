package com.gsy.translate.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


import android.content.Context;
import android.graphics.BitmapFactory;
import android.inputmethodservice.Keyboard.Key;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Tools {
	public static String key = "tjnitcom";
	/**
	 * dp转px
	 * @param context
	 * @param dp
	 * @return
	 */
	 public static int dp2px(Context context, float dp) { 
		    float scale = context.getResources().getDisplayMetrics().density; 
		    return (int) (dp * scale + 0.5f); 
		} 
	 /**
	  * sp转px
	  * @param context
	  * @param spValue
	  * @return
	  */
	 public static int sp2px(Context context, float spValue) {  
         final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
         return (int) (spValue * fontScale + 0.5f);  
     }  
	 /** 
	  * px转dp 
      */  
     public static int px2dip(Context context, float pxValue) {  
       final float scale = context.getResources().getDisplayMetrics().density;  
       return (int) (pxValue / scale + 0.5f);  
     }  
	 //rsa加密，用公钥加密
	 /**
	  * rsa加密
	  * @param data 待加密字符串
	  * @param publicKey 密钥
	  * @return
	  */
	 public static byte[] encryptData(String data,PublicKey publicKey){
		 try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return cipher.doFinal(data.getBytes());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		 return null;
	 }
	 
	 /**
	  * md5加密
	  * @param data
	  * @return
	  */
	 public static String getMD5(String data){
		 
		try {
			MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(data.getBytes("UTF-8"));
			byte[] result = mDigest.digest();
			int i; 
			StringBuffer buf = new StringBuffer(""); 
			for (int offset = 0; offset < result.length; offset++) { 
			i = result[offset]; 
			if(i<0) i+= 256; 
			if(i<16) 
			buf.append("0"); 
			buf.append(Integer.toHexString(i)); 
			} 
			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} 
		return null;
	 }
	 
	 public static final String ALGORITHM_DES = "DES/ECB/PKCS5Padding";
	  
	  /**
	   * DES算法，加密
	   *
	   * @param data 待加密字符串
	   * @param key 加密私钥，长度不能够小于8位
	   * @return 加密后的字节数组，一般结合Base64编码使用
	   */
	public static String encode(String key, String data) {
		try {
			SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "DES");
			Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
			//IvParameterSpec iv = new IvParameterSpec(key.getBytes());
			cipher.init(Cipher.ENCRYPT_MODE, keySpec);
			byte[] bytes = cipher.doFinal(data.getBytes());
			return Base64.encodeToString(bytes, Base64.DEFAULT);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} 
		return null;
	}
	  
	  /**
	   * DES算法，解密
	   *
	   * @param data 待解密字符串
	   * @param key 解密私钥，长度不能够小于8位
	   * @return 解密后的字节数组
	   * @throws Exception 异常
	   */
	public static String decode(String key, String data) {
		try {
			byte[] byteMi = Base64.decode(data, 0);
			SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "DES");
			Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
			//IvParameterSpec iv = new IvParameterSpec(key.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, keySpec);
			byte[] bytes = cipher.doFinal(byteMi);
			return new String(bytes);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	 
	/**
	 * hmacSha1加密
	 * @param src
	 * @return
	 */
	public static String getHmacSHA1(String src){  
	    Mac mac;
		try {
			
			mac = Mac.getInstance("HmacSHA1");
			SecretKeySpec secret = new SecretKeySpec(  
		            "tjnitcom".getBytes(), mac.getAlgorithm());  
		    mac.init(secret); 
		    byte[] bytes = mac.doFinal(src.getBytes());
		    StringBuffer sb = new StringBuffer();
		    for(int i=0;i<bytes.length;i++){
		    	sb.append(byteToHexString(bytes[i]));
		    }
		    return sb.toString();
		    //return new String(mac.doFinal(src.getBytes()));
		    //return Base64.encodeToString(sb.toString(), Base64.DEFAULT);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}  
	    return null;
	}  
	/**
	 * 二进制转16进制
	 * @param ib
	 * @return
	 */
	 private static String byteToHexString(byte ib){  
	      char[] Digit={  
	        '0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'  
	      };  
	      char[] ob=new char[2];  
	      ob[0]=Digit[(ib>>>4)& 0X0f];  
	      ob[1]=Digit[ib & 0X0F];  
	      String s=new String(ob);  
	      return s;           
	     }      
	 /**
	  * 修复listView的高度，scrollview嵌套listview时用
	  * @param listView
	  */
	 public static void fixListViewHeight(ListView listView) {   
		 try {
			// 如果没有设置数据适配器，则ListView没有子项，返回。  
		        ListAdapter listAdapter = listView.getAdapter();  
		        int totalHeight = 0;   
		        if (listAdapter == null) {   
		            return;   
		        }   
		        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {     
		            View listViewItem = listAdapter.getView(i , null, listView);  
		            try {
		            	// 计算子项View 的宽高   
			            listViewItem.measure(0, 0);   
					} catch (Exception e) {
					}
		             
		            // 计算所有子项的高度和
		            totalHeight += listViewItem.getMeasuredHeight();    
		        }   
		        ViewGroup.LayoutParams params = listView.getLayoutParams();   
		        // listView.getDividerHeight()获取子项间分隔符的高度   
		        // params.height设置ListView完全显示需要的高度    
		        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));   
		        
		        listView.setLayoutParams(params);   
		} catch (Exception e) {
			// TODO: handle exception
		}
	        
	    }  
	 /**
	  * 格式化double,保留小数点后两位
	  * @param d
	  * @return
	  */
	 public static double doubleFormat(double d){
		 DecimalFormat df = new DecimalFormat("#.00");  
		 df.setRoundingMode(RoundingMode.HALF_EVEN);
		 return Double.parseDouble(df.format(d));
	 }
	 /**
	  * 格式化double，保留小数点后一位
	  * @param d
	  * @return
	  */
	 public static double doubleFormat1(double d){
		 DecimalFormat df = new DecimalFormat("#.0");  
		 df.setRoundingMode(RoundingMode.FLOOR);
		 return Double.parseDouble(df.format(d));
	 }
	 /**
	  * 手机号中间5位变星号
	  * @param phone
	  * @return
	  */
//	 public static String replacePhone(String phone){
//		 if(phone.length()<=3) return phone;
//		 String tmpString="";
//		 tmpString+=phone.substring(0, 3);
//		 if(phone.length()>=4) tmpString+="*";
//		 if(phone.length()>=5) tmpString+="*";
//		 if(phone.length()>=6) tmpString+="*";
//		 if(phone.length()>=7) tmpString+="*";
//		 if(phone.length()>=8) tmpString+=phone.substring(7, phone.length());
//		 return tmpString;
//	 }
	 /**
	  * 获取调整图片所需的比例
	  * @param options 解析图片所需的BitmapFactory.Options
	  * @param minSideLength 调整后图片最小的宽或高值
	  * @param maxNumOfPixels 调整后图片的内存占用量上限
	  * @return 返回一个调整比例，再用此比例调整原始图片并加载到内存中，此时图片所消耗的内存不会超出事先指定的大小
	  */
	 public static int computeSampleSize(BitmapFactory.Options options,

				int minSideLength, int maxNumOfPixels) {

					int initialSize = computeInitialSampleSize(options, minSideLength,

					maxNumOfPixels);

					int roundedSize;

					if (initialSize <= 8) {

						roundedSize = 1;

						while (roundedSize < initialSize) {

							roundedSize <<= 1;

						}

					} else {

						roundedSize = (initialSize + 7) / 8 * 8;

					}

					return roundedSize;

				}
				private static int computeInitialSampleSize(BitmapFactory.Options options,

				        int minSideLength, int maxNumOfPixels) {

				    double w = options.outWidth;

				    double h = options.outHeight;



				    int lowerBound = (maxNumOfPixels == -1) ? 1 :

				            (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));

				    int upperBound = (minSideLength == -1) ? 128 :

				            (int) Math.min(Math.floor(w / minSideLength),

				            Math.floor(h / minSideLength));



				    if (upperBound < lowerBound) {

				        // return the larger one when there is no overlapping zone.

				        return lowerBound;

				    }



				    if ((maxNumOfPixels == -1) &&

				            (minSideLength == -1)) {

				        return 1;

				    } else if (minSideLength == -1) {

				        return lowerBound;

				    } else {

				        return upperBound;

				    }

				} 
	public static String formatDate(Date date,String format){
	 	try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			String result = simpleDateFormat.format(date);
			return result;
		}catch (Exception e){
	 		e.printStackTrace();
		}
		return null;
	}

	public static void l(Object obj){
		if(obj!=null)
			Log.i("tag",obj.toString());
		else Log.i("tag","打印对象为空");
	}
}
