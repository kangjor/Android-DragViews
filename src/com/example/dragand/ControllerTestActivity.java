package com.example.dragand;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.ToggleButton;


public class ControllerTestActivity extends Activity implements OnItemSelectedListener {

	EditText edit;
	Button button;
	CheckBox check;
	RadioButton yes,no;
	ToggleButton togglebutton;
	Spinner spinner;
	String [] data ={"캠핑" ,"낚시" ,"드라이브" ,"영화" ,"음주" };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_controller_test);
		//xml에서 선언한 객체들을 생성함
		edit = (EditText) findViewById(R.id.edit);
		button = (Button) findViewById(R.id.button);
		check = (CheckBox) findViewById(R.id.watermelon);
		yes = (RadioButton) findViewById(R.id.yes);
		no = (RadioButton) findViewById(R.id.no);
		togglebutton = (ToggleButton) findViewById(R.id.togglebutton);
		spinner = (Spinner) findViewById(R.id.spinner);
		//arrayadapter클래스를 이용하여 스피너에 데이터를 부착
		ArrayAdapter<String> adapter = new ArrayAdapter<String>
		(this, android.R.layout.simple_dropdown_item_1line , data);
		//스피너에 adapter를 부착
		spinner.setAdapter(adapter);
		//스피너에 이벤트를 부착
		spinner.setOnItemSelectedListener(this);
		
		
	}
	
	//버튼을 클릭하면 자동으로 호출되는 메소드 =반드시 인자값으로 View받아줘야 버튼객체가 인식 
	public void setData(View v ){
		
		if(v.getId()==R.id.button){
		
			//에디트텍스트에 버튼이 눌렸다고 표시해줌
			edit.setText("Login button clicked~~");
		
		}else if(v.getId()==R.id.watermelon){
			//체크가 선택되었다면 에디ㄷ트텍스트에 수박선택이라고 표시하시고 
			//체크가 해제되었다면 에디트 텍스트에 수박선택해제하고 표시하시오
			if(check.isChecked()){
				edit.setText("수박이 선택되었습니다.");
			}else{
				edit.setText("수박이 해제되었습니다.");
			}			
		}else if(v.getId()==R.id.yes){
			edit.setText("여름휴가 선택되었습니다.");
			
		}else if(v.getId()==R.id.no){
			edit.setText("여름휴가 가지못함");
			
		}else if(v.getId()==R.id.togglebutton){
			//체크가 선택되었다면 에디ㄷ트텍스트에 수박선택이라고 표시하시고 
			//체크가 해제되었다면 에디트 텍스트에 수박선택해제하고 표시하시오
			if(togglebutton.isChecked()){
				edit.setText("Wifi On");
			}else{
				edit.setText("Wifi Off");
			}			
		}
		
		
	}

	//스피너 객체를 클릭하면 자동으로 호출되는 콜백 메소드
	@Override
	public void onItemSelected(AdapterView<?> arg0, View z, int arg2,
			long arg3) {
		//스피너 객체중 어느 아이템이 선택되었는지에 대한 정보를 얻오옴
				String item = (String) spinner.getSelectedItem();
				edit.setText(item +" 이 선택");
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	

}

























