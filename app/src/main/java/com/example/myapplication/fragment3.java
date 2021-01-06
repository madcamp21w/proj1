package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment3 extends Fragment {

    public static ArrayList<DepartmentItem> departmentItemList = new ArrayList<DepartmentItem>();
    public static int check_pos_for_button = 0;

    public static int dep_icon[] = { R.drawable.civil ,R.drawable.business,R.drawable.mechanic,R.drawable.physics,
                        R.drawable.brain,R.drawable.system,R.drawable.dna,R.drawable.biochem,
                        R.drawable.design,R.drawable.math,R.drawable.material,R.drawable.nuclear,
                        R.drawable.electrical,R.drawable.computer,R.drawable.chemistry,R.drawable.aerospace};

    public static String dep_name[] = {  "건설환경공학과", "기술경영학과", "기계공학과", "물리학과", "바이오 및\n뇌공학과",
                        "산업 및\n시스템공학과", "생명과학과", "생명화학공학과", "산업디자인학과", "수리과학과",
                        "신소재공학과", "원자력 및\n양자공학과", "전기 및\n전자공학과", "전산학과", "화학과", "항공우주공학과" };

    public fragment3() { }

    // TODO: Rename and change types and number of parameters
    public static fragment3 newInstance() {
        fragment3 fragment = new fragment3();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment3, null) ;

        //GridView adapter
        GridViewAdapterForKaist adapter = new GridViewAdapterForKaist();
        GridView gridview = (GridView) view.findViewById(R.id.gridView_third_tab);
        gridview.setAdapter(adapter);
        adapter = add_item_to_gridviewadapter(adapter);


        //Spinner adapter
        String dep_name_for_spinner[] = { "-학과 선택-", "건설환경공학과", "기술경영학과", "기계공학과", "물리학과", "바이오 및\n뇌공학과",
                "산업 및\n시스템공학과", "생명과학과", "생명화학공학과", "산업디자인학과", "수리과학과",
                "신소재공학과", "원자력 및\n양자공학과", "전기 및\n전자공학과", "전산학과", "화학과", "항공우주공학과" };

        Spinner spiner = (Spinner) view.findViewById(R.id.spinner1);
        ArrayAdapter<String> spinadapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,dep_name_for_spinner);
        spinadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner.setAdapter(spinadapter);

        //
        //Button Control, Listener
        //
        Button button1 = (Button) view.findViewById(R.id.button_go_dep);

        button1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(),DepartmentActivity.class);
                intent.putExtra("position", check_pos_for_button);
                intent.putExtra("dep_icon",dep_icon);
                intent.putExtra("dep_name",dep_name);
                startActivity(intent);

            }
        });

        //
        //Gridview Listener
        //
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent intent = new Intent(getActivity(),DepartmentActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("dep_icon",dep_icon);
                intent.putExtra("dep_name",dep_name);
                startActivity(intent);

            }
        });

        //
        //spinner Listener
        //
        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity().getApplicationContext(), "selected department : " + dep_name[position],Toast.LENGTH_SHORT).show();
                check_pos_for_button = position-1;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



        return view ;
    }

    public GridViewAdapterForKaist add_item_to_gridviewadapter(GridViewAdapterForKaist myadapter){
        String json = "";
        json = getJsonString("DepartmentInformation.json");
        jsonParsing(json); // arraylist<childfragmentitem> 에 들어가게 됨.


        for(int i=0; i<dep_icon.length;i++){
            myadapter.addItem(ContextCompat.getDrawable(getActivity(), dep_icon[i]), dep_name[i]); //건설환경공학과
        }

        return myadapter;
    }


    public String getJsonString(String filename)
    {
        String json = "";

        try {
            InputStream is = getActivity().getAssets().open(filename);
            int fileSize = is.available();

            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        return json;
    }


    public void jsonParsing(String json)
    {
        try{
            JSONObject jsonObject = new JSONObject(json);

            JSONArray contentArray = jsonObject.getJSONArray("DepartmentInformation");

            for(int i=0; i<contentArray.length(); i++)
            {
                JSONObject contentObject = contentArray.getJSONObject(i);

                DepartmentItem depinfo = new DepartmentItem();

                depinfo.setDep_name(contentObject.getString("dep_name"));
                depinfo.setDep_num(contentObject.getString("dep_building_num"));
                depinfo.setDep_king_phone(contentObject.getString("dep_k_phone_number"));
                depinfo.setDep_king_email(contentObject.getString("dep_k_email"));
                depinfo.setDep_admin_phone(contentObject.getString("dep_a_phone_number"));
                depinfo.setDep_admin_email(contentObject.getString("dep_a_email"));
                depinfo.setDep_url(contentObject.getString("dep_url"));
                depinfo.setDep_latitude(Double.parseDouble(contentObject.getString("dep_latitude")));
                depinfo.setDep_longitude(Double.parseDouble(contentObject.getString("dep_longitude")));
                departmentItemList.add(depinfo);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

    }

}