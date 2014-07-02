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
	String [] data ={"ķ��" ,"����" ,"����̺�" ,"��ȭ" ,"����" };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_controller_test);
		//xml���� ������ ��ü���� ������
		edit = (EditText) findViewById(R.id.edit);
		button = (Button) findViewById(R.id.button);
		check = (CheckBox) findViewById(R.id.watermelon);
		yes = (RadioButton) findViewById(R.id.yes);
		no = (RadioButton) findViewById(R.id.no);
		togglebutton = (ToggleButton) findViewById(R.id.togglebutton);
		spinner = (Spinner) findViewById(R.id.spinner);
		//arrayadapterŬ������ �̿��Ͽ� ���ǳʿ� �����͸� ����
		ArrayAdapter<String> adapter = new ArrayAdapter<String>
		(this, android.R.layout.simple_dropdown_item_1line , data);
		//���ǳʿ� adapter�� ����
		spinner.setAdapter(adapter);
		//���ǳʿ� �̺�Ʈ�� ����
		spinner.setOnItemSelectedListener(this);
		
		
	}
	
	//��ư�� Ŭ���ϸ� �ڵ����� ȣ��Ǵ� �޼ҵ� =�ݵ�� ���ڰ����� View�޾���� ��ư��ü�� �ν� 
	public void setData(View v ){
		
		if(v.getId()==R.id.button){
		
			//����Ʈ�ؽ�Ʈ�� ��ư�� ���ȴٰ� ǥ������
			edit.setText("Login button clicked~~");
		
		}else if(v.getId()==R.id.watermelon){
			//üũ�� ���õǾ��ٸ� ����Ʈ�ؽ�Ʈ�� ���ڼ����̶�� ǥ���Ͻð� 
			//üũ�� �����Ǿ��ٸ� ����Ʈ �ؽ�Ʈ�� ���ڼ��������ϰ� ǥ���Ͻÿ�
			if(check.isChecked()){
				edit.setText("������ ���õǾ����ϴ�.");
			}else{
				edit.setText("������ �����Ǿ����ϴ�.");
			}			
		}else if(v.getId()==R.id.yes){
			edit.setText("�����ް� ���õǾ����ϴ�.");
			
		}else if(v.getId()==R.id.no){
			edit.setText("�����ް� ��������");
			
		}else if(v.getId()==R.id.togglebutton){
			//üũ�� ���õǾ��ٸ� ����Ʈ�ؽ�Ʈ�� ���ڼ����̶�� ǥ���Ͻð� 
			//üũ�� �����Ǿ��ٸ� ����Ʈ �ؽ�Ʈ�� ���ڼ��������ϰ� ǥ���Ͻÿ�
			if(togglebutton.isChecked()){
				edit.setText("Wifi On");
			}else{
				edit.setText("Wifi Off");
			}			
		}
		
		
	}

	//���ǳ� ��ü�� Ŭ���ϸ� �ڵ����� ȣ��Ǵ� �ݹ� �޼ҵ�
	@Override
	public void onItemSelected(AdapterView<?> arg0, View z, int arg2,
			long arg3) {
		//���ǳ� ��ü�� ��� �������� ���õǾ������� ���� ������ �����
				String item = (String) spinner.getSelectedItem();
				edit.setText(item +" �� ����");
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	

}

























