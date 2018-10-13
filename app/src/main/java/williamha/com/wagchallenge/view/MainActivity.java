package williamha.com.wagchallenge.view;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import williamha.com.wagchallenge.R;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        displayAvatarFragment();
    }

    private void displayAvatarFragment() {
        AvatarFragment avatarFragment = AvatarFragment.newInstance();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.fragment_holder, avatarFragment)
                .replace(R.id.fragment_holder, avatarFragment)
                .addToBackStack(null)
                .commit();
    }
}
