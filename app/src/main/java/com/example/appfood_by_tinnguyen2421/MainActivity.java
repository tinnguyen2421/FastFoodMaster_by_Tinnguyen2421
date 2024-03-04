package com.example.appfood_by_tinnguyen2421;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appfood_by_tinnguyen2421.BottomNavigation.ChefBottomNavigation;
import com.example.appfood_by_tinnguyen2421.BottomNavigation.CustomerBottomNavigation;
import com.example.appfood_by_tinnguyen2421.BottomNavigation.DeliveryBottomNavigation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth Fauth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fauth = FirebaseAuth.getInstance();

        if (Fauth.getCurrentUser() != null) {
            if (Fauth.getCurrentUser().isEmailVerified()) {
                checkUserRole();
            } else {
                showEmailVerificationDialog();
            }
        } else {
            navigateToMainMenu();
        }
    }

    private void checkUserRole() {
        databaseReference = FirebaseDatabase.getInstance().getReference("User")
                .child(FirebaseAuth.getInstance().getUid() + "/Role");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String role = dataSnapshot.getValue(String.class);
                if (role != null) {
                    switch (role) {
                        case "UserModel":
                            startActivity(new Intent(MainActivity.this, CustomerBottomNavigation.class));
                            finish();
                            break;
                        case "Chef":
                            startActivity(new Intent(MainActivity.this, ChefBottomNavigation.class));
                            finish();
                            break;
                        case "DeliveryPerson":
                            startActivity(new Intent(MainActivity.this, DeliveryBottomNavigation.class));
                            finish();
                            break;
                        default:
                            showRoleErrorDialog();
                    }
                } else {
                    showRoleErrorDialog();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showEmailVerificationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Kiểm tra xem bạn đã xác minh thông tin của mình chưa, nếu không vui lòng xác minh");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                navigateToMainMenu();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        Fauth.signOut();
    }

    private void showRoleErrorDialog() {
        Toast.makeText(MainActivity.this, "Lỗi xác định vai trò người dùng", Toast.LENGTH_LONG).show();
        navigateToMainMenu();
    }

    private void navigateToMainMenu() {
        startActivity(new Intent(MainActivity.this, MainMenu.class));
        finish();
    }
}
