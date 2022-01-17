package org.tensorflow.lite.examples.classification;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class Menu extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        String name = getIntent().getStringExtra("Extra_name");
        bottomNavigationView = findViewById(R.id.bottom_navigasi_menu);
        bottomNavigationView.setOnItemReselectedListener(navigation);

    }
    private NavigationBarView.OnItemReselectedListener navigation = new BottomNavigationView.OnNavigationItemReselectedListener() {

        @Override
        public void onNavigationItemReselected(@NonNull MenuItem item) {

            String name = getIntent().getStringExtra("Extra_name");
            Fragment f = null;
            switch (item.getItemId()){
                case R.id.menu_chatbot:
                    f = new FragmentChatbot();
                    break;
                case R.id.menu_scan:
                    Intent it= new Intent((Context) Menu.this, ClassifierActivity.class);
                    it.putExtra("Extra_name", name);
                    Menu.this.startActivity(it);
                    Menu.this.finish();
                    break;
                case R.id.menu_wiki:
                    f = new FragmentWiki();
                    break;
                case R.id.menu_user:
                    String nameuser = getIntent().getStringExtra("Extra_name");
                    f = new FragmentUser();
                    break;
                case R.id.menu_history:
                    f = new FragmentHistory();
                    Bundle bundle = new Bundle();
                    bundle.putString("Extra_name", name);
                    f.setArguments(bundle);
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,f).commit();
        }
    };

}