package com.tantuo.didicar.DriverLicenseNFC;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.nfc.NfcAdapter;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tantuo.didicar.Bean.DriverBean;
import com.tantuo.didicar.R;

public class Check_success_activity extends Activity implements OnClickListener,OnMenuItemClickListener { 

	
	private TextView tvUserid,tvBrand,tvProducedDate,tvLeaveFactoryDate,tvRetailSeller,tvProductInfo;
    private Button btnBack,btnExit;
    private ImageButton popUpMenu;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.check_found_activity_1);
		SysApplication.getInstance().addActivity(this);

		Intent intent = getIntent();
        String JsonData = intent.getStringExtra("person");
        DriverBean driverBean = new Gson().fromJson(JsonData,DriverBean.class);

        Toast.makeText(getApplicationContext(), "diverbean:" + driverBean.getCar_ID(), Toast.LENGTH_SHORT).show();


		
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		
		pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

		
		
		SoundPool soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 1);  
        soundPool.load(this, R.raw.positive, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {  
            @Override  
            public void onLoadComplete(SoundPool soundPool, int i, int i2) {  
                soundPool.play(1,  //声音id  
                        1, //左声道  
                        1, //右声道  
                        1, //优先级  
                        0, // 0表示不循环，-1表示循环播放  
                        1);//播放比率，0.5~2，一般为1  
            }  
        });  
        
        Vibrator vibrator = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
        vibrator.vibrate(500);
		
        popUpMenu = (ImageButton)findViewById(R.id.btnPopUp);
        popUpMenu.setOnClickListener(this);
		
		
        btnBack=(Button)findViewById(R.id.btn1);
        btnBack.setOnClickListener(new OnClickListener(){
     	   public void onClick(View v) {
              finish();
     	   }});
        
        btnExit=(Button)findViewById(R.id.btn2);
        btnExit.setOnClickListener(new OnClickListener(){
     	   public void onClick(View v) {
     		  SysApplication.getInstance().exit();
     	   }});
        
        
		
//		Toast.makeText(Check_success_activity.this, "产品"+MainActivity.CardId+"验证成功，感谢您购买正品服装，详情请关注我们的公众号", Toast.LENGTH_LONG).show();
		

		tvUserid = (TextView) findViewById(R.id.txt1); 
	    tvUserid.setText("产品标签UID: "+ DriverRFIDMainActivity.CardId);
	    
		tvBrand = (TextView) findViewById(R.id.txt2); 
		tvBrand.setText("品牌: "+DriverRFIDMainActivity.brand);
	    
/*		tvLeaveFactoryDate = (TextView) findViewById(R.id.txt3); 
		tvLeaveFactoryDate.setText("产品名称: "+MainActivity.leaveFactoryDate);
	    

	    
		tvRetailSeller = (TextView) findViewById(R.id.txt4); 
		tvRetailSeller.setText(MainActivity.retailSeller);
	    
		tvProductInfo = (TextView) findViewById(R.id.txt5); 
		tvProductInfo.setText(MainActivity.productInformation);*/
	    
	
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		// 前台分发系统,用于第二次检测NFC标签时该应用有最高的捕获优先权.
		nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null); 
		System.out.println("onResume performed...");

	}
	
	
	@Override
	   protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){	        	
             finish();

            Log.e("log_tag", "Tag读出成功,TagID:");             
	         }
	         else {
	             Log.e("log_tag", "on new intent Tag读出失败 ACTION_TAG_DISCOVERED ！equals(intent.getAction())),TagID:"); 
			}
	}

	
	   //点击按钮后，加载弹出式菜单
    @Override
    public void onClick(View v) {
        //创建弹出式菜单对象（最低版本11）
        PopupMenu popup = new PopupMenu(this, v);//第二个参数是绑定的那个view
        //获取菜单填充器
        MenuInflater inflater = popup.getMenuInflater();
        //填充菜单
        inflater.inflate(R.menu.popup_menu, popup.getMenu());
        //绑定菜单项的点击事件
        popup.setOnMenuItemClickListener(this);
        //显示(这一行代码不要忘记了)
        popup.show(); 
        
    }

    //弹出式菜单的单击事件处理
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
        case R.id.item1:
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        	builder.setTitle("公司介绍");
        	builder.setMessage("Final Check 防伪查询\n\n妥啦信息技术有限公司\n\n网址：www.tuola.com\n\n合作邮箱 :shulin.tan@paragon-id.com");
        	builder.setPositiveButton("确定", null);
        	builder.create().show();
            break;


        default:
            break;
        }
        return false;
    }
	
	
	
	
}
