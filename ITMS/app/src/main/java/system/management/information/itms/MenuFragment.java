package system.management.information.itms;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "Logout";
    private Boolean checkNotify;
    private Typeface Fonts;
    FirebaseAuth firebaseAuth;


    public MenuFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_menu, container, false);

        Fonts = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Kanit-Light.ttf");

        Button logout = (Button) rootview.findViewById(R.id.btLogout);
        Button notify = (Button) rootview.findViewById(R.id.btNotify);

        TextView txtSwitch = (TextView) rootview.findViewById(R.id.txtNotify);

        final Switch onOffSwitch = (Switch)  rootview.findViewById(R.id.switchNotify);

        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("system.management.information.itms", MODE_PRIVATE);
        onOffSwitch.setChecked(sharedPrefs.getBoolean("NotificationEdited", true));

        notify.setTypeface(Fonts);
        logout.setTypeface(Fonts);
        txtSwitch.setTypeface(Fonts);

        firebaseAuth = FirebaseAuth.getInstance();

        onOffSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onOffSwitch.isChecked())
                {
                    FirebaseMessaging.getInstance().subscribeToTopic("news");
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("system.management.information.itms", MODE_PRIVATE).edit();
                    editor.putBoolean("NotificationEdited", true);
                    editor.commit();
                }
                else
                {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("news");
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("system.management.information.itms", MODE_PRIVATE).edit();
                    editor.putBoolean("NotificationEdited", false);
                    editor.commit();
                }
            }
        });

        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(new android.view.ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom));


                builder.setTitle("LOGOUT");
                builder.setMessage("Are you sure you want to exit?");
                builder.setCancelable(false);
                builder.setIcon(android.R.drawable.ic_dialog_info);


                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //logging out the user
                        firebaseAuth.signOut();
                        //closing activity
                        getActivity().finish();
                        //starting login activity
                        dialog.dismiss();
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("news");
                        Log.d(TAG, "UnfollowToITMS");
                        startActivity(new Intent(getActivity(), LoginActivity.class));

                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

                Snackbar.make(view, "Confirm Logout", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        // Inflate the layout for this fragment
        return rootview;
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_bottombar, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onClick(View view) {
        Fragment fragment = null;
        switch (view.getId()) {
            case R.id.btNotify:
                fragment = new NotificationFragment();
                replaceFragment(fragment);
                break;
        }
    }
}
