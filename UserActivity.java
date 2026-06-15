///UserActivity.java
package com.prop.terracefarming;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserActivity extends MainActivity {

	Connection conn;
	EditText username,password,edtplant,edtcrop,edtplace,edtsize,edtdays;
	Button b1,b3,b2,bsensor;
	String s1,s2,s3,pass1,plant,soil,crop,type,croptype,growth,weather,temperature;
    Spinner spinner1;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_main);

		b1=(Button)findViewById(R.id.btn1);
		b2=(Button)findViewById(R.id.btn2);
		b3=(Button)findViewById(R.id.btn3);

		bsensor=(Button)findViewById(R.id.btnsensor);

		edtcrop=(EditText)findViewById(R.id.edtcrop);

        spinner1 = (Spinner) findViewById(R.id.spinner1);




//		conn=CONN();
		b1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				

				crop=edtcrop.getText().toString();



					
				new QuerySQL().execute();
				
			}
			
		});


		b2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				croptype= spinner1.getSelectedItem().toString();

				Intent intent=new Intent(UserActivity.this, CropResult.class);
				intent.putExtra("s1", croptype);


				startActivity(intent);

			}

		});

		b3.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				startActivity(new Intent(UserActivity.this, UserLoginActivity.class));

			}

		});

	bsensor.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			crop=edtcrop.getText().toString();


			new QuerySQL1().execute();

		}

	});

	}
	public class QuerySQL1 extends AsyncTask<String, Void, Boolean> {

		ProgressDialog pDialog ;
		Exception error;
		ResultSet rs;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(UserActivity.this);
			pDialog.setTitle("Sensor");
			pDialog.setMessage("Retreiving values...");
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Boolean doInBackground(String... args) {



			try {


				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://mysql-75344-0.cloudclusters.net:18880/terracefarming","admin","cU5zYktH");

			} catch (SQLException se) {
				Log.e("ERRO1",se.getMessage());
			} catch (ClassNotFoundException e) {
				Log.e("ERRO2",e.getMessage());
			} catch (Exception e) {
				Log.e("ERRO3",e.getMessage());
			}


			try {

				String COMANDOSQL="select * from cropdetails where cropname='"+crop+"'";
				Statement statement = conn.createStatement();
				rs = statement.executeQuery(COMANDOSQL);
				if(rs.next()) {

					soil = rs.getString(4);
				}


				s1= Read_URL("https://blynk.cloud/external/api/get?token=31l5uKD3vmmFBX5DEDPqXjceXndVgL0s&V0");
				s2= Read_URL("https://blynk.cloud/external/api/get?token=31l5uKD3vmmFBX5DEDPqXjceXndVgL0s&V1");
				s3= Read_URL("https://blynk.cloud/external/api/get?token=31l5uKD3vmmFBX5DEDPqXjceXndVgL0s&V2");


				return true;


				// Toast.makeText(getBaseContext(),
				// "Successfully Inserted.", Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				error = e;
				return false;
//				Toast.makeText(getBaseContext(),"Successfully Registered...", Toast.LENGTH_LONG).show();
			}


		}

		@SuppressLint("NewApi")
		@Override
		protected void onPostExecute(Boolean result1) {
			pDialog.dismiss ( ) ;
			if(result1)
			{



//					System.out.println("ELSE(JSON) LOOP EXE");
				try {// try3 open

					Intent intent=new Intent(UserActivity.this, UserActivity1.class);
					intent.putExtra("s1", s1);
					intent.putExtra("s2", s2);
					intent.putExtra("s3", s3);
					intent.putExtra("soil", soil);
					intent.putExtra("crop", crop);
					startActivity(intent);

				} catch (Exception e1) {
					Toast.makeText(getBaseContext(), e1.toString(),
							Toast.LENGTH_LONG).show();

				}


			}else
			{
				if(error!=null)
				{
					Toast.makeText(getBaseContext(),error.getMessage().toString() ,Toast.LENGTH_LONG).show();
				}
				else
				{
					Toast.makeText(getBaseContext(),"Check your credentials!!!" ,Toast.LENGTH_LONG).show();
				}
			}
			super.onPostExecute(result1);
		}
	}

	private static String Read_URL(String theUrl) {
		StringBuilder content = new StringBuilder();
		try {
			URL url = new URL(theUrl);
			URLConnection urlConnection = url.openConnection();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				content.append(line + "\n");
			}
			bufferedReader.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return content.toString();
	}

	public class QuerySQL extends AsyncTask<String, Void, Boolean> {

		ProgressDialog pDialog ;
		Exception error;
		ResultSet rs;
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        
	        pDialog = new ProgressDialog(UserActivity.this);
	        pDialog.setTitle("Searching");
	        pDialog.setMessage("Search Crop Details...");
	        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        pDialog.setIndeterminate(false);
	        pDialog.setCancelable(false);
	        pDialog.show();
	    }

	    @Override
	    protected Boolean doInBackground(String... args) {

	    	
			
			try {
				
				
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://mysql-75344-0.cloudclusters.net:18880/terracefarming","admin","cU5zYktH");

			} catch (SQLException se) {
				Log.e("ERRO1",se.getMessage());
			} catch (ClassNotFoundException e) {
				Log.e("ERRO2",e.getMessage());
			} catch (Exception e) {
			    Log.e("ERRO3",e.getMessage());
			}
			

			try {
				String COMANDOSQL="select * from cropdetails where cropname='"+crop+"'";
				Statement statement = conn.createStatement();
				rs = statement.executeQuery(COMANDOSQL);
			if(rs.next()){

				type = rs.getString(2);
				growth = rs.getString(3);
				soil = rs.getString(4);
				weather = rs.getString(5);
				temperature = rs.getString(6);

				
				return true;
			}

			return false;
				
				// Toast.makeText(getBaseContext(),
				// "Successfully Inserted.", Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				error = e;
				return false;
//				Toast.makeText(getBaseContext(),"Successfully Registered...", Toast.LENGTH_LONG).show();
			}


	    }

	    @SuppressLint("NewApi")
		@Override
	    protected void onPostExecute(Boolean result1) {
	    	pDialog.dismiss ( ) ;
	    	if(result1)
	    	{
                
			
					
//					System.out.println("ELSE(JSON) LOOP EXE");
					try {// try3 open
						
					Intent intent=new Intent(UserActivity.this, SearchResultActivity.class);
						intent.putExtra("s1", type);
						intent.putExtra("s2", growth);
						intent.putExtra("s3", soil);
						intent.putExtra("s4", weather);
						intent.putExtra("s5", temperature);

					
						startActivity(intent);
						
					} catch (Exception e1) {
						Toast.makeText(getBaseContext(), e1.toString(),
								Toast.LENGTH_LONG).show();

					}					
				
            
	    	}else
	    	{
	    		if(error!=null)
	    		{
	    			Toast.makeText(getBaseContext(),error.getMessage().toString() ,Toast.LENGTH_LONG).show();
	    		}
	    		else
	    		{
	    			Toast.makeText(getBaseContext(),"No Matches Found!!!" ,Toast.LENGTH_LONG).show();
	    		}
	    	}
	    	super.onPostExecute(result1);
	    }
	}
	
}
///UserActivity1.java
package com.prop.terracefarming;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class UserActivity1 extends Activity {

	String recvname = "", recvname1 = "", recvname2 = "";
	String mobile = "";
	String sendername = "";
	Connection conn;
	EditText edmessage;
	String complaint, area, landmark, description, date1, status, diet1, diet2, diag, dept;
	Button sendmsg;
	ImageButton template;
	TextView t1, t2, t3, t4, t5, t6, t7, t8, t9;
	String s1, s2, s3, s4, s5, s6, s7, s8, soil, crop;
	Button b1, b2, b3;
	HashMap<String, String> usersList1 = null;
	ArrayList<HashMap<String, String>> usersList2 = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comp_listreport);



		Intent intent = getIntent();
		s1 = intent.getStringExtra("s1");

		s2 = intent.getStringExtra("s2");

		s3 = intent.getStringExtra("s3");

		soil = intent.getStringExtra("soil");

		crop = intent.getStringExtra("crop");


		t1 = (TextView) findViewById(R.id.textView1);
		t1.setText(s1+" celcius");

		t2 = (TextView) findViewById(R.id.textView2);
		t2.setText(s2);


		t3 = (TextView) findViewById(R.id.textView3);
		t3.setText(s3);

		t4 = (TextView) findViewById(R.id.t23);
		t4.setText("This Soil " +soil+ " is suitable for cultivate this");

		t4 = (TextView) findViewById(R.id.t24);
		t4.setText("plant "+crop+" and moisture level having this soil");




		b3 = (Button) findViewById(R.id.btn2);
		b3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				try {

					//String solved = "solved";
					//new StatusUpdate().execute(solved);
					startActivity(new Intent(UserActivity1.this, UserActivity.class));

				} catch (Exception e) {
					//	Toast.makeText(applicationContext.getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
				}
			}
		});


	}


}

///UserLoginActivity.java

package com.prop.terracefarming;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserLoginActivity extends MainActivity {

	Connection conn;
	EditText username,password,hostIP;
	Button signin,signup,back;
	String user,pass,user1,pass1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_login);

		signin=(Button)findViewById(R.id.btn1);
		back=(Button)findViewById(R.id.btn2);

		
		username=(EditText)findViewById(R.id.edtusername);
		password=(EditText)findViewById(R.id.edtpassword);
//		conn=CONN();
		signin.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				user=username.getText().toString();
				pass=password.getText().toString();
				
				SharedPreferences.Editor editor =getSharedPreferences("username",Context.MODE_PRIVATE).edit();
                editor.putString("username",user);
                editor.commit();
                editor.apply();

					
				new QuerySQL().execute(user,pass);
				
			}
			
		});


		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				startActivity(new Intent(UserLoginActivity.this, MainActivity.class));

			}

		});
	}

	
	
	public class QuerySQL extends AsyncTask<String, Void, Boolean> {

		ProgressDialog pDialog ;
		Exception error;
		ResultSet rs;
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        
	        pDialog = new ProgressDialog(UserLoginActivity.this);
	        pDialog.setTitle("Authentication");
	        pDialog.setMessage("Verifying your credentials...");
	        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        pDialog.setIndeterminate(false);
	        pDialog.setCancelable(false);
	        pDialog.show();
	    }

	    @Override
	    protected Boolean doInBackground(String... args) {
	    	
	    	user1 = new String(args[0]);
	    	pass1 = new String(args[1]);
	    	
	    	
			
			try {
				
				
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://mysql-75344-0.cloudclusters.net:18880/fertifind","admin","cU5zYktH");

			} catch (SQLException se) {
				Log.e("ERRO1",se.getMessage());
			} catch (ClassNotFoundException e) {
				Log.e("ERRO2",e.getMessage());
			} catch (Exception e) {
			    Log.e("ERRO3",e.getMessage());
			}
			

			try {
				String COMANDOSQL="select * from userdetails where username='"+user1+"' && password='"+pass1+"'";
				Statement statement = conn.createStatement();
				rs = statement.executeQuery(COMANDOSQL);
			if(rs.next()){

				
				
				return true;
			}

			return false;
				
				// Toast.makeText(getBaseContext(),
				// "Successfully Inserted.", Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				error = e;
				return false;
//				Toast.makeText(getBaseContext(),"Successfully Registered...", Toast.LENGTH_LONG).show();
			}


	    }

	    @SuppressLint("NewApi")
		@Override
	    protected void onPostExecute(Boolean result1) {
	    	pDialog.dismiss ( ) ;
	    	if(result1)
	    	{
                
			
					
//					System.out.println("ELSE(JSON) LOOP EXE");
					try {// try3 open
						
						Intent intent=new Intent(UserLoginActivity.this, UserActivity.class);
						//intent.putExtra("latitude", lati);
						//intent.putExtra("longitude", longi);
						//intent.putExtra("loginuser", user1);
					
						startActivity(intent);			
						
					} catch (Exception e1) {
						Toast.makeText(getBaseContext(), e1.toString(),
								Toast.LENGTH_LONG).show();

					}					
				
            
	    	}else
	    	{
	    		if(error!=null)
	    		{
	    			Toast.makeText(getBaseContext(),error.getMessage().toString() ,Toast.LENGTH_LONG).show();
	    		}
	    		else
	    		{
	    			Toast.makeText(getBaseContext(),"Check your credentials!!!" ,Toast.LENGTH_LONG).show();
	    		}
	    	}
	    	super.onPostExecute(result1);
	    }
	}
	
	
	


}

///CropResult.java
package com.prop.terracefarming;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class CropResult extends Activity {
	ListView listView;
	Connection conn;
	Double lat,lon;
	String username;
	
	String lati,croptype,loginname,areaname1;
	
	String sendername;
	String collegecode;
	HashMap<String,String> usersList1 = null;
	ArrayList<HashMap<String,String>> usersList2 = new ArrayList<HashMap<String,String>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.crop_list);
		 listView = (ListView) findViewById(R.id.listView1);

		 Intent intent = getIntent();
		croptype = intent.getStringExtra("s1");
		

		try{



			new QuerySQL().execute();
		}
		catch (Exception e){
			System.out.println("NumberFormatException: " + e.getMessage());
		}
	}


	public class QuerySQL extends AsyncTask<Object, Void, Boolean> {

		ProgressDialog pDialog ;
		Exception error;


		ResultSet rs;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(CropResult.this);
			pDialog.setTitle("Crop List");
			pDialog.setMessage("Getting List...");
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Boolean doInBackground(Object... args) {



			try {


				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://mysql-75344-0.cloudclusters.net:18880/terracefarming","admin","cU5zYktH");

			} catch (SQLException se) {
				Log.e("ERRO1",se.getMessage());
			} catch (ClassNotFoundException e) {
				Log.e("ERRO22",e.getMessage());
			} catch (Exception e) {
				Log.e("ERRO3",e.getMessage());
			}


			try {
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				Date currentDate = new Date();


				// convert date to calendar
				Calendar c = Calendar.getInstance();
				c.setTime(currentDate);

				// manipulate date
				//c.add(Calendar.YEAR, 1);
				//c.add(Calendar.MONTH, 1);
				c.add(Calendar.DATE, -2); //same with c.add(Calendar.DAY_OF_MONTH, 1);
				//c.add(Calendar.HOUR, 1);
				//c.add(Calendar.MINUTE, 1);
				//c.add(Calendar.SECOND, 1);

				// convert calendar to date
				Date currentDatePlusOne = c.getTime();

				String tmp = dateFormat.format(currentDatePlusOne);

				//String solved = "solved";

				String COMANDOSQL="select * from cropdetails where type = '"+croptype+"'";
				Statement statement = conn.createStatement();
				rs = statement.executeQuery(COMANDOSQL);
				while(rs.next()){
					usersList1 = new HashMap<String, String>();
					//	usersList1.put("uname",rs.getString("name"));
					usersList1.put("cropname",rs.getString(1));

					
					
					//usersList1.put("totalseats",rs.getString(7));
					//usersList1.put("alloted",rs.getString(8));
					Log.d("Friend List Map :",usersList1.toString());

					usersList2.add(usersList1);


				}


				return true;
				// Toast.makeText(getBaseContext(),
				// "Successfully Inserted.", Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				error = e;
				return false;
//				Toast.makeText(getBaseContext(),"Successfully Registered...", Toast.LENGTH_LONG).show();
			}


		}

		@SuppressLint("NewApi")
		@Override
		protected void onPostExecute(Boolean result1) {
			pDialog.dismiss ( ) ;
			if(result1)
			{



//					System.out.println("ELSE(JSON) LOOP EXE");
				try {// try3 open

					listView.setAdapter(new CropResultAdapter(CropResult.this, usersList2));
					listView.setOnItemClickListener(new OnItemClickListener() {

						public void onItemClick(AdapterView<?> parent, View v,
												int position, long id) {

							Intent intent = new Intent(
									CropResult.this,
									CropResult1.class);
							intent.putExtra("cropname", usersList2.get(position)
									.get("cropname"));
							

							
							startActivity(intent);
							
							/*String totseats = usersList2.get(position)
									.get("totalseats");
							int totseats1 = Integer.parseInt(totseats);
							
							if(totseats1>0)
							{
							Intent intent = new Intent(
									ViewCollege.this,
									CollegeReport.class);
							intent.putExtra("collegecode", usersList2.get(position)
									.get("collegecode"));
							intent.putExtra("totalseats", usersList2.get(position)
									.get("totalseats"));
							
							
							startActivity(intent);
							}
							else
							{
								Toast.makeText(getBaseContext(), "College Almost full!!!",
										Toast.LENGTH_LONG).show();
							}
							*/
						}
					});

				} catch (Exception e1) {
					Toast.makeText(getBaseContext(), e1.toString(),
							Toast.LENGTH_LONG).show();

				}


			}else
			{
				if(error!=null)
				{
					Toast.makeText(getBaseContext(),error.getMessage().toString() ,Toast.LENGTH_LONG).show();
				}
			}
			super.onPostExecute(result1);
		}
	}


}
///CropResult1.java
package com.prop.terracefarming;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CropResult1 extends MainActivity {

	Connection conn;
	EditText username,password,edtplant,edtcrop,edtplace,edtsize,edtdays;
	Button b1,b3,b2;
	String user,pass,user1,pass1,plant,soil,crop,type,croptype,growth,weather,temperature;
    Spinner spinner1;
	String s1, s2, s3, s4, s5;
	TextView t1, t2, t3, t4, t5, t6, t7, t8, t9;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.crop_viewreport);


		Intent intent = getIntent();
		s1 = intent.getStringExtra("cropname");


		b1=(Button)findViewById(R.id.btn1);

					
				new QuerySQL().execute();


		b1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				startActivity(new Intent(CropResult1.this, UserActivity.class));

			}

		});
	}

	
	
	public class QuerySQL extends AsyncTask<String, Void, Boolean> {

		ProgressDialog pDialog ;
		Exception error;
		ResultSet rs;
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        
	        pDialog = new ProgressDialog(CropResult1.this);
	        pDialog.setTitle("Crop Details");
	        pDialog.setMessage("Getting Crop Details...");
	        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        pDialog.setIndeterminate(false);
	        pDialog.setCancelable(false);
	        pDialog.show();
	    }

	    @Override
	    protected Boolean doInBackground(String... args) {

	    	
			
			try {
				
				
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://mysql-75344-0.cloudclusters.net:18880/terracefarming","admin","cU5zYktH");

			} catch (SQLException se) {
				Log.e("ERRO1",se.getMessage());
			} catch (ClassNotFoundException e) {
				Log.e("ERRO2",e.getMessage());
			} catch (Exception e) {
			    Log.e("ERRO3",e.getMessage());
			}
			

			try {
				String COMANDOSQL="select * from cropdetails where cropname='"+s1+"'";
				Statement statement = conn.createStatement();
				rs = statement.executeQuery(COMANDOSQL);
			if(rs.next()){

				type = rs.getString(2);
				growth = rs.getString(3);
				soil = rs.getString(4);
				weather = rs.getString(5);
				temperature = rs.getString(6);

				
				return true;
			}

			return false;
				
				// Toast.makeText(getBaseContext(),
				// "Successfully Inserted.", Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				error = e;
				return false;
//				Toast.makeText(getBaseContext(),"Successfully Registered...", Toast.LENGTH_LONG).show();
			}


	    }

	    @SuppressLint("NewApi")
		@Override
	    protected void onPostExecute(Boolean result1) {
	    	pDialog.dismiss ( ) ;
	    	if(result1)
	    	{
				t1 = (TextView) findViewById(R.id.textView1);
				t1.setText(type);

				t2 = (TextView) findViewById(R.id.textView2);
				t2.setText(growth);

				t3 = (TextView) findViewById(R.id.textView3);
				t3.setText(soil);

				t4 = (TextView) findViewById(R.id.textView4);
				t4.setText(weather);

				t5 = (TextView) findViewById(R.id.textView5);
				t5.setText(temperature);
			
					
//
				
            
	    	}else
	    	{
	    		if(error!=null)
	    		{
	    			Toast.makeText(getBaseContext(),error.getMessage().toString() ,Toast.LENGTH_LONG).show();
	    		}
	    		else
	    		{
	    			Toast.makeText(getBaseContext(),"No Matches Found!!!" ,Toast.LENGTH_LONG).show();
	    		}
	    	}
	    	super.onPostExecute(result1);
	    }
	}
	
	
	


}
///CropresultAdapter.java
package com.prop.terracefarming;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class CropResultAdapter extends BaseAdapter {
	Context con;
	LayoutInflater layoutInflater;
	ArrayList<HashMap<String,String>> listvalue;

	public CropResultAdapter(CropResult listOfFriendsActivity,
                             ArrayList<HashMap<String,String>> usersList) {
		// TODO Auto-generated constructor stub
		con = listOfFriendsActivity;
		listvalue = usersList;
		layoutInflater = LayoutInflater.from(listOfFriendsActivity);
	}

	

	public int getCount() {
		// TODO Auto-generated method stub
		return listvalue.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listvalue.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.crop_listview, null);
			viewHolder = new ViewHolder();

			viewHolder.txtUsername = (TextView) convertView
					.findViewById(R.id.textView_name);
		

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.txtUsername.setText(listvalue.get(position).get("cropname")
				.toString());




		return convertView;

	}

	class ViewHolder {
		TextView txtUsername,txtmobile,txttime;

	}

}
///HomePage.java
package com.prop.terracefarming;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class HomePage extends Activity {
	int splashTime = 2500;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		Thread thread = new Thread() {
			public void run() {
				try {
					synchronized (this) {
						wait(splashTime);
					}

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					startActivity(new Intent(HomePage.this,
							MainActivity.class));
					finish();
				}
			}
		};
		thread.start();
		 
	}

	
}
///Mainactivity.java
package com.prop.terracefarming;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	Button b1,b2,b3,alarm,feedback,video1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		b1=(Button)findViewById(R.id.btn1);
		
		b2=(Button)findViewById(R.id.btn2);


		
		
		b1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(MainActivity.this, UserLoginActivity.class));
				
			}
			
		});
		
		
				
		b2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(MainActivity.this, RegisterActivity.class));
				
			}
			
		});



		
		
	/*	feedback.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent homeActivity=new Intent(MainActivity.this,Login.class);
				startActivity(homeActivity);		
				
			}
			
		}); */
	}

	

}
///RegiaterActivity.java
package com.prop.terracefarming;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends MainActivity {

	EditText edtName, edtMobileNo, edtEmail, edtusername,	edtPassword, edtprof,edtfathername,edtaddr,edtcaste,edtreligion,edtssc,edthsc;
Button btnSubmit,btnSubmit1;
Connection conn;

private String name, mobilenumber, email, username,prof, password,fathername,addr,caste,religion,ssc,hsc;


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		edtName = (EditText) findViewById(R.id.register_name);
		edtusername = (EditText) findViewById(R.id.register_username);
		edtEmail = (EditText) findViewById(R.id.register_email);
		edtPassword = (EditText) findViewById(R.id.register_password);
		edtMobileNo = (EditText) findViewById(R.id.register_phno);
		edtprof = (EditText) findViewById(R.id.register_prof);
		
		edtaddr = (EditText) findViewById(R.id.register_addr);
		
		//edtssc = (EditText) findViewById(R.id.register_ssc);
		//edthsc = (EditText) findViewById(R.id.register_hsc);
	
		btnSubmit = (Button) findViewById(R.id.register_btn_reg);
		btnSubmit.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				name = edtName.getText().toString();
				username = edtusername.getText().toString();
				email = edtEmail.getText().toString();
				password = edtPassword.getText().toString();
				mobilenumber = edtMobileNo.getText().toString();
				prof = edtprof.getText().toString();
				
				addr = edtaddr.getText().toString();
				
				//ssc = edtssc.getText().toString();
				//hsc = edthsc.getText().toString();
				try {
					if(verify())
					{
						new QuerySQL().execute();
					}
					
		
					} catch (Exception e) {
		        Log.e("ERRO",e.getMessage());
				}

				
			}
		});
		
		btnSubmit1 = (Button) findViewById(R.id.register_btn_cancel);
		btnSubmit1.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(i);
				
				
			}
		});

	}
	
	public boolean verify()
	{
//		EditText name, userName, password, cpassword, email, phoneNumber;
		Boolean ret=true;
		if(edtName.getText().toString().length()<1){edtName.setError("Field Required");ret=false;}
		if(edtusername.getText().toString().length()<1){edtusername.setError("Field Required");ret=false;}
		if(edtPassword.getText().toString().length()<1){edtPassword.setError("Field Required");ret=false;}
		if(edtprof.getText().toString().length()<1){edtprof.setError("Field Required");ret=false;}
		if(!edtPassword.getText().toString().equals(edtprof.getText().toString())){edtPassword.setError("Password not same");ret=false;}
		
		
		if(edtaddr.getText().toString().length()<1){edtaddr.setError("Field Required");ret=false;}
		
		//if(edtssc.getText().toString().length()<1){edtssc.setError("Field Required");ret=false;}
		//if(edthsc.getText().toString().length()<1){edthsc.setError("Field Required");ret=false;}
				
		if(!edtEmail.getText().toString().contains("@")){edtEmail.setError("E-Mail ID Invalid");ret=false;}
		if(edtEmail.getText().toString().length()<1){edtEmail.setError("Field Required");ret=false;}
		if(edtMobileNo.getText().toString().length()<10){edtMobileNo.setError("Invalid Phone Number");ret=false;}//It will Set but ok it wont be visible
		if(edtMobileNo.getText().toString().length()<1){edtMobileNo.setError("Field Required");ret=false;}
		
		String expression = "^([0-9\\+]|\\(\\d{0,1}\\))[0-9\\-\\. ]{0,15}$";
        CharSequence inputString = edtMobileNo.getText().toString();
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputString);
        if (matcher.matches())
        {
		
        }
        else
        {
        	edtMobileNo.setError("Invalid Number");ret=false;
        }
		
		
		return ret;
	}
	
	
	public class QuerySQL extends AsyncTask<String, Void, Boolean> {

		ProgressDialog pDialog ;
		Exception error;
		ResultSet rs;
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        
	        pDialog = new ProgressDialog(RegisterActivity.this);
	        pDialog.setTitle("Registration");
	        pDialog.setMessage("Registering your credentials...");
	        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        pDialog.setIndeterminate(false);
	        pDialog.setCancelable(false);
	        pDialog.show();
	    }

	    @Override
	    protected Boolean doInBackground(String... args) {
	    	
	    	
	    	
	    	
			try {
				
				
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://mysql-75344-0.cloudclusters.net:18880/fertifind","admin","cU5zYktH");
			} catch (SQLException se) {
				Log.e("ERRO1",se.getMessage());
			} catch (ClassNotFoundException e) {
				Log.e("ERRO2",e.getMessage());
			} catch (Exception e) {
			    Log.e("ERRO3",e.getMessage());
			}
			

			try {
				Statement statement = conn.createStatement();
				int success=statement.executeUpdate("insert into userdetails values('"+name+"','"+username+"','"+password+"','"+addr+"','"+email+"','"+mobilenumber+"')");
			
				if (success >= 1) {
					// successfully created product
					
					return true;
					// closing this screen
//					finish();
				} else {
					// failed to create product
					return false;
				}


				
				// Toast.makeText(getBaseContext(),
				// "Successfully Inserted.", Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				error = e;
				return false;
//				Toast.makeText(getBaseContext(),"Successfully Registered...", Toast.LENGTH_LONG).show();
			}


	    }

	    @SuppressLint("NewApi")
		@Override
	    protected void onPostExecute(Boolean result1) {
	    	pDialog.dismiss ( ) ;
	    	if(result1)
	    	{
                
	    		Toast.makeText(getBaseContext(),"Successfully created your credentials." ,Toast.LENGTH_LONG).show();
					
//					System.out.println("ELSE(JSON) LOOP EXE");
					try {// try3 open
						
						Intent i = new Intent(getApplicationContext(),
								UserLoginActivity.class);
						startActivity(i);		
						
					} catch (Exception e1) {
						Toast.makeText(getBaseContext(), e1.toString(),
								Toast.LENGTH_LONG).show();

					}					
				
            
	    	}else
	    	{
	    		if(error!=null)
	    		{
	    			Toast.makeText(getApplicationContext(),error.toString() ,Toast.LENGTH_LONG).show();
	    			Log.d("Error not null...", error.toString());
	    		}
	    		else
	    		{
	    			Toast.makeText(getBaseContext(),"Not crreated your credentials!!!" ,Toast.LENGTH_LONG).show();
	    		}
	    	}
	    	super.onPostExecute(result1);
	    }
	}


}
///SearchResultActivity.java
package com.prop.terracefarming;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

public class SearchResultActivity extends Activity {

	String recvname = "", recvname1 = "", recvname2 = "";
	String mobile = "";
	String sendername = "";
	Connection conn;
	EditText edmessage;
	String item, price, ownername, description, date1, status, diet1, diet2, diag, dept;
	Button sendmsg;
	ImageButton template;
	TextView t1, t2, t3, t4, t5, t6, t7, t8, t9;
	String s1, s2, s3, s4, s5, address, s7, s8, username, facility;
	Button b1, b2, b3,b4;
	HashMap<String, String> usersList1 = null;
	ArrayList<HashMap<String, String>> usersList2 = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_result);



		Intent intent = getIntent();
		s1 = intent.getStringExtra("s1");
		s2 = intent.getStringExtra("s2");
		s3 = intent.getStringExtra("s3");
		s4 = intent.getStringExtra("s4");
		s5 = intent.getStringExtra("s5");




		t1 = (TextView) findViewById(R.id.textView1);
		t1.setText(s1);

		t2 = (TextView) findViewById(R.id.textView2);
		t2.setText(s2);

		t3 = (TextView) findViewById(R.id.textView3);
		t3.setText(s3);

		t4 = (TextView) findViewById(R.id.textView4);
		t4.setText(s4);

		t5 = (TextView) findViewById(R.id.textView5);
		t5.setText(s5);




		b1 = (Button) findViewById(R.id.btn1);
		b1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				try {


						Intent intent = new Intent(SearchResultActivity.this, UserActivity.class);

					startActivity(intent);



				} catch (Exception e) {
					//	Toast.makeText(applicationContext.getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
				}
			}
		});






	}



}