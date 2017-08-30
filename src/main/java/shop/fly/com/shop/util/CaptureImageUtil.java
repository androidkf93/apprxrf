package shop.fly.com.shop.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CaptureImageUtil {

	public static final int GET_IMAGE_BY_CAMERA = 5001;
	public static final int GET_IMAGE_FROM_PHONE = 5002;
	public static final int CROP_IMAGE = 5003;
	public static Uri imageUriFromCamera;// 获取相机
	public static Uri cropImageUri;// 修剪

	public static void openCameraImage(final Activity activity) {// 相机图像
		CaptureImageUtil.imageUriFromCamera = CaptureImageUtil
				.createImagePathUri(activity);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// MediaStore.EXTRA_OUTPUT参数不设置时,系统会自动生成一个uri,但是只会返回一个缩略图
		// 返回图片在onActivityResult中通过以下代码获取
		// Bitmap bitmap = (Bitmap) data.getExtras().get("data");
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				CaptureImageUtil.imageUriFromCamera);
		activity.startActivityForResult(intent,
				CaptureImageUtil.GET_IMAGE_BY_CAMERA);
	}

	public static void openLocalImage(final Activity activity) {// 本地图像
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		activity.startActivityForResult(intent,
				CaptureImageUtil.GET_IMAGE_FROM_PHONE);
	}

	public static void cropImage(Activity activity, Uri srcUri) {// 裁剪图像
		CaptureImageUtil.cropImageUri = CaptureImageUtil
				.createImagePathUri(activity);
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(srcUri, "image/*");
		intent.putExtra("crop", "true");

		// //////////////////////////////////////////////////////////////
		// 1.宽高和比例都不设置时,裁剪框可以自行调整(比例和大小都可以随意调整)
		// //////////////////////////////////////////////////////////////
		// 2.只设置裁剪框宽高比(aspect)后,裁剪框比例固定不可调整,只能调整大小
		// //////////////////////////////////////////////////////////////
		// 3.裁剪后生成图片宽高(output)的设置和裁剪框无关,只决定最终生成图片大小
		// //////////////////////////////////////////////////////////////
		// 4.裁剪框宽高比例(aspect)可以和裁剪后生成图片比例(output)不同,此时,
		// 会以裁剪框的宽为准,按照裁剪宽高比例生成一个图片,该图和框选部分可能不同,
		// 不同的情况可能是截取框选的一部分,也可能超出框选部分,向下延伸补足
		// //////////////////////////////////////////////////////////////

		// aspectX aspectY 是裁剪框宽高的比例
//		intent.putExtra("aspectX", 4);
//		intent.putExtra("aspectY", 3);
		// outputX outputY 是裁剪后生成图片的宽高

		intent.putExtra("outputX", 640);
		intent.putExtra("outputY", 480);

		// return-data为true时,会直接返回bitmap数据,但是大图裁剪时会出现问题,推荐下面为false时的方式
		// return-data为false时,不会返回bitmap,但需要指定一个MediaStore.EXTRA_OUTPUT保存图片uri
		intent.putExtra(MediaStore.EXTRA_OUTPUT, CaptureImageUtil.cropImageUri);
		intent.putExtra("return-data", false);
		activity.startActivityForResult(intent, CROP_IMAGE);
	}

	/**
	 * 创建一条图片地址uri,用于保存拍照后的照片
	 *
	 * @param context
	 * @return 图片的uri
	 */
	private static Uri createImagePathUri(Context context) {
		Uri imageFilePath = null;
		String status = Environment.getExternalStorageState();
		SimpleDateFormat timeFormatter = new SimpleDateFormat(
				"yyyyMMdd_HHmmss", Locale.CHINA);
		long time = System.currentTimeMillis();
		String imageName = timeFormatter.format(new Date(time));
		// ContentValues是我们希望这条记录被创建时包含的数据信息
		ContentValues values = new ContentValues(5);
		values.put(MediaStore.Images.Media.DISPLAY_NAME, imageName);
		values.put(MediaStore.Images.Media.DATE_TAKEN, time);
		values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
		values.put(MediaStore.Images.Media.HEIGHT, 200);
		values.put(MediaStore.Images.Media.WIDTH, 200);
		if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
			imageFilePath = context.getContentResolver().insert(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		} else {
			imageFilePath = context.getContentResolver().insert(
					MediaStore.Images.Media.INTERNAL_CONTENT_URI, values);
		}
		Log.i("", "生成的照片输出路径：" + imageFilePath.toString());
		return imageFilePath;
	}

	public static String getRealPath(Context mContext, Uri fileUrl) {
		String fileName = null;
		Uri filePathUri = fileUrl;
		if (fileUrl != null) {
			if (fileUrl.getScheme().toString().compareTo("content") == 0) // content://开头的uri
			{
				Cursor cursor = mContext.getContentResolver().query(fileUrl,
						null, null, null, null);
				if (cursor != null && cursor.moveToFirst()) {
					int column_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					fileName = cursor.getString(column_index); // 取出文件路径
					if (!fileName.startsWith("/mnt")) {
						// 检查是否有”/mnt“前缀

						fileName = "/mnt" + fileName;
					}
					cursor.close();
				}
			} else if (fileUrl.getScheme().compareTo("file") == 0) // file:///开头的uri
			{
				fileName = filePathUri.toString();
				fileName = filePathUri.toString().replace("file://", "");
				// 替换file://
				if (!fileName.startsWith("/mnt")) {
					// 加上"/mnt"头
					fileName += "/mnt";
				}
			}
		}
		return fileName;
	}

	/**
	 * 获取照片
	 */
	public static void showImagePickDialog(final Activity activity) {
		// TODO Auto-generated method stub
		String title = "获取图片方式";
		String[] choices = new String[]{"拍照", "从手机中选择"};
		new AlertDialog.Builder(activity)
				.setTitle(title)
				.setItems(choices, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						switch (which) {
							case 0:
								CaptureImageUtil.openCameraImage(activity);
								break;
							case 1:
								CaptureImageUtil.openLocalImage(activity);
								break;
						}
					}
				})
				.setNegativeButton("返回", null)
				.show();
	}

	/**
	 * 文件转base64字符串
	 * @param file
	 * @return
	 */
	public static String fileToBase64(File file) {
		String base64 = null;
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			byte[] bytes = new byte[in.available()];
			int length = in.read(bytes);
			base64 = Base64.encodeToString(bytes, 0, length, Base64.DEFAULT);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return base64;
	}

}
