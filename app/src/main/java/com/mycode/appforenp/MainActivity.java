package com.mycode.appforenp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.mycode.appforenp.adapter.MyListAdapter;
import com.mycode.appforenp.database.DatabaseHelper;
import com.mycode.appforenp.databinding.ActivityMainBinding;
import com.mycode.appforenp.models.MyModel;

import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int CODE = 187;
    ActivityMainBinding binding;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        setSupportActionBar(binding.aplayout.toolbar);
        binding.aplayout.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(MainActivity.this,CreateRecordActivity.class);
               startActivityForResult(intent,CODE);
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.aplayout.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.aplayout.content.rvList.setLayoutManager(linearLayoutManager);


        getDataFramDatabase();
    }

    private void getDataFramDatabase() {
        ArrayList<MyModel> items = new ArrayList<>();
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        Cursor cursor = databaseHelper.getAllData();
        //iterate through all the rows contained in the database
        if(!cursor.moveToNext()){
            binding.aplayout.content.tvInfo.setVisibility(View.VISIBLE);
            binding.aplayout.content.tvInfo.setText(getString(R.string.no_data));
        }else {
            binding.aplayout.content.tvInfo.setVisibility(View.GONE);
            while(cursor.moveToNext()){
                items.add(new MyModel(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getBlob(3)
                ));
            }

            MyListAdapter adapter = new MyListAdapter(items,this);
            binding.aplayout.content.rvList.setAdapter(adapter);
        }



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }





    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE) {
            if (resultCode == RESULT_OK) {
                String returnedResult = data.getData().toString();
                Toast.makeText(this,returnedResult,Toast.LENGTH_SHORT).show();
                getDataFramDatabase();
            }
        }
    }
}
