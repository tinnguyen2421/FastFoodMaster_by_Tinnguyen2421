package com.example.appfood_by_tinnguyen2421.Chef.ChefFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.appfood_by_tinnguyen2421.Account.UserModel;
import com.example.appfood_by_tinnguyen2421.Categories;
import com.example.appfood_by_tinnguyen2421.Chef.ChefActivity.ChefPostCate;
import com.example.appfood_by_tinnguyen2421.Chef.ChefAdapter.ChefCateAdapter;

import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.UpdateDishModel;
import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.ChefStatus;
import com.example.appfood_by_tinnguyen2421.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class ChefHomeFragment extends Fragment {
    //Dialog ChangeTime
    private TextView timeStart, btnAceptt, timeEnd, btnCancell, tvTime4, tvTime3, tvTime2, tvTime1,txtOpenn,txtClosedd,tvXem,layoutt;
    //Dialog ChangeTime
    String spinnerReasonn,timeStartt,timeStopp,timeEndd;
    Spinner spinnerReason;
    private ProgressDialog progressDialog;
    RecyclerView RecyclerCate;
    TextView ResName, ResAddress, ResStatus, changeStatus, timeClosed, timeOpen, timeOpenInVaction;
    private List<UpdateDishModel> updateDishModelList;
    private List<Categories> categoriesList;
    private TextView selectedTextView = null;
    private ChefCateAdapter adapterCate;
    DatabaseReference dataaa;
    FloatingActionButton add;
    private String District, City, Ward, Address;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chef_home, null);
        RecyclerCate = v.findViewById(R.id.Recycle_cate);
        ResName = v.findViewById(R.id.tenQuan);
        ResAddress = v.findViewById(R.id.diaChi);
        ResStatus = v.findViewById(R.id.Status);
        RecyclerCate.setLayoutManager(new LinearLayoutManager(getContext()));
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
        updateDishModelList = new ArrayList<>();
        categoriesList = new ArrayList<>();
        add = v.findViewById(R.id.themMoi);
        if (getActivity() != null) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        }
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dataaa = FirebaseDatabase.getInstance().getReference("Chef").child(userid);
        dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel chefc = dataSnapshot.getValue(UserModel.class);
                if (chefc != null) District = chefc.getDistrict();
                City = chefc.getCity();
                Ward = chefc.getWard();
                Address = chefc.getAddress();
                ResName.setText(chefc.getFirstName() + chefc.getLastName());
                ResAddress.setText(Address + "," + Ward + "," + City + "," + District);
                checkStatus();
                chefCate();

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getContext(), ChefPostCate.class));
                    }
                });
                ResStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openStatusDialog(Gravity.CENTER);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return v;
    }

    private void chefCate() {
        String userid = FirebaseAuth.getInstance().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Categories").child(City).child(District).child(Ward).child(userid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoriesList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Categories categories = snapshot.getValue(Categories.class);
                    categoriesList.add(categories);
                }
                adapterCate = new ChefCateAdapter(getContext(), categoriesList);
                RecyclerCate.setAdapter(adapterCate);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void checkStatus()
    {
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ChefStatus").child(userid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Kiểm tra xem có dữ liệu trong snapshot không
                    ChefStatus chefStatus = dataSnapshot.getValue(ChefStatus.class);
                    if (chefStatus != null) {
                        // Lấy giá trị của trường Status từ snapshot
                        String status = chefStatus.getStatus();
                        // Đặt giá trị Status vào TextView (ResStatus)
                        ResStatus.setText(status);
                        if (status.equals("Quán Bận")) {
                            if (isAdded() && getContext() != null) {
                                Drawable drawable = getResources().getDrawable(R.drawable.baseline_circle_24);
                                ResStatus.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                            }
                        }
                        else {
                            if (isAdded() && getContext() != null) {
                                Drawable drawable = getResources().getDrawable(R.drawable.baseline_online_prediction_24);
                                ResStatus.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void openStatusDialog(int center) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.chef_dialog_status);
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams windowAttributes = window.getAttributes();  // Thêm dòng này
            windowAttributes.gravity = windowAttributes.gravity;  // Sửa lại dòng này

            if (Gravity.CENTER == windowAttributes.gravity) {
                dialog.setCancelable(true);
            }
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = windowAttributes.gravity;
        if (Gravity.CENTER == windowAttributes.gravity) {
            dialog.setCancelable(true);
        }
        //dialog.show();
        //ánh xạ dialog
        changeStatus = dialog.findViewById(R.id.ChangeStatus);
        timeOpen = dialog.findViewById(R.id.TimeOpen);
        timeClosed = dialog.findViewById(R.id.TimeClosed);
        layoutt=dialog.findViewById(R.id.layout);
        timeOpenInVaction = dialog.findViewById(R.id.TimeOpenInVacation);
        tvXem=dialog.findViewById(R.id.txt1);
        tvXem.setText(ResStatus.getText().toString().trim());
        if (ResStatus.getText().toString().trim().equals("Quán Bận"))
        {
            Drawable drawable = getResources().getDrawable(R.drawable.baseline_circle_24);
            layoutt.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        }
        else
        {
            Drawable drawable = getResources().getDrawable(R.drawable.baseline_online_prediction_24);
            layoutt.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        }
        changeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChooseDialog(Gravity.CENTER);
            }
        });
        dialog.show();
    }

    private void openChooseDialog(int center) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.chef_dialog_choose_status);
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams windowAttributes = window.getAttributes();  // Thêm dòng này
            windowAttributes.gravity = windowAttributes.gravity;  // Sửa lại dòng này
            if (Gravity.CENTER == windowAttributes.gravity) {
                dialog.setCancelable(true);
            }
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = windowAttributes.gravity;
        //if (Gravity.CENTER == windowAttributes.gravity) {
          //  dialog.setCancelable(true);
        //}
        //dialog.setCancelable(false);
        //ánh xạ
        txtOpenn=dialog.findViewById(R.id.txtOpen);
        txtClosedd=dialog.findViewById(R.id.txtClosed);
        txtOpenn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadStatusFree();
            }
        });
        txtClosedd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openchangeStatusDialog(Gravity.CENTER);
            }
        });



        dialog.show();
    }
    private void openchangeStatusDialog(int center) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.chef_dialog_changestatus);
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams windowAttributes = window.getAttributes();  // Thêm dòng này
            windowAttributes.gravity = windowAttributes.gravity;  // Sửa lại dòng này
            if (Gravity.CENTER == windowAttributes.gravity) {
                dialog.setCancelable(true);
            }
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = windowAttributes.gravity;
        if (Gravity.CENTER == windowAttributes.gravity) {
            dialog.setCancelable(true);
        }
        dialog.setCancelable(false);
        //ánh xạ
        tvTime1 = dialog.findViewById(R.id.tvTime1);
        tvTime2 = dialog.findViewById(R.id.tvTime2);
        tvTime3 = dialog.findViewById(R.id.tvTime3);
        tvTime4 = dialog.findViewById(R.id.tvTime4);
        btnCancell = dialog.findViewById(R.id.btnCancel);
        btnAceptt = dialog.findViewById(R.id.btnAcept);
        timeStart = dialog.findViewById(R.id.tvTimeStart);
        timeEnd = dialog.findViewById(R.id.tvTimeEnd);
        spinnerReason = dialog.findViewById(R.id.SpinnerReason);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTextViewClick((TextView) v);
            }
        };
        tvTime1.setOnClickListener(onClickListener);
        tvTime2.setOnClickListener(onClickListener);
        tvTime3.setOnClickListener(onClickListener);
        tvTime4.setOnClickListener(onClickListener);
        timeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimePickerDialogStart();
            }
        });
        timeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimePickerDialogEnd();
            }
        });
        btnCancell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
            btnAceptt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //if(checkData()) {
                        LoadStatusBusy();
                    //}
                }
            });

        dialog.show();
        }
    private void LoadStatusBusy()
    {
        spinnerReasonn=spinnerReason.getSelectedItem().toString().trim();
        timeStartt=timeStart.getText().toString().trim();
        timeStopp=selectedTextView.getText().toString().trim();
        timeEndd=timeEnd.getText().toString().trim();
        progressDialog.setMessage("Đang cập nhật");
        progressDialog.show();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Status", "Quán Bận");
        hashMap.put("TimeStop", ""+timeStopp);
        hashMap.put("TimeStart", ""+timeStartt);
        hashMap.put("TimeEnd", ""+timeEndd);
        hashMap.put("Reason", ""+spinnerReasonn);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ChefStatus");
        reference.child(FirebaseAuth.getInstance().getUid()).setValue(hashMap).addOnSuccessListener(
                unused -> {
                    progressDialog.dismiss();
                    new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog)
                            .setMessage("Thành công")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    openchangeStatusDialog(Gravity.CENTER);
                                }
                            })
                            .show();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog)
                            .setMessage("Thêm thất bại, lỗi: " + e.getMessage())
                            .setPositiveButton("OK", null)
                            .show();
        });
    }
    private void loadStatusFree()
    {
        progressDialog.setMessage("Đang cập nhật");
        progressDialog.show();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Status", "Mở cửa");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ChefStatus");
        reference.child(FirebaseAuth.getInstance().getUid()).setValue(hashMap).addOnSuccessListener(
                        unused -> {
                            progressDialog.dismiss();
                            new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog)
                                    .setMessage("Thành công")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                            openStatusDialog(Gravity.CENTER);
                                        }
                                    })
                                    .show();
                        })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog)
                            .setMessage("Thêm thất bại, lỗi: " + e.getMessage())
                            .setPositiveButton("OK", null)
                            .show();
                });
    }

    private boolean checkData() {

        if (timeStartt.isEmpty()) {
            timeStart.setError("Không được để trống");
            return false;
        }

        if (timeEndd.isEmpty()) {
            timeEnd.setError("Không được để trống");
            return false;
        }
        if (timeStopp == null) {
            selectedTextView.setText("Chưa xác định");
            return false;
        }
        btnAceptt.setTextColor(Color.RED);
        //GradientDrawable gradientDrawable = new GradientDrawable();
        //gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        //gradientDrawable.setStroke(3, Color.RED);
        return true;
    }
    private void handleTextViewClick(TextView textView) {

        if (selectedTextView == textView) {
            // Nếu người dùng nhấn lại vào TextView đã được chọn, đặt nó về mặc định
            resetTextViewAppearance(textView);
            selectedTextView = null; // Đặt lại TextView được chọn
        } else {
            if (selectedTextView != null) {
                // Nếu đã có TextView được chọn trước đó, đặt lại giao diện của nó
                resetTextViewAppearance(selectedTextView);
            }
            // Cập nhật giao diện của TextView được nhấn vào
            updateTextViewAppearance(textView);
            // Lưu trữ TextView được chọn
            selectedTextView = textView;

        }
    }

    private void updateTextViewAppearance(TextView textView) {
        textView.setTextColor(Color.RED);
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setStroke(3, Color.RED);
        textView.setBackground(gradientDrawable);
    }
    private void resetTextViewAppearance(TextView textView) {
        textView.setTextColor(Color.BLACK);
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(Color.parseColor("#BFBFBF"));
        textView.setBackground(gradientDrawable);
    }
    private void showDateTimePickerDialogStart() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Hiển thị DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Lưu ngày đã chọn
                        final String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;

                        // Hiển thị TimePickerDialog sau khi chọn ngày
                        TimePickerDialog timePickerDialog = new TimePickerDialog(
                                getContext(),
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        // Lưu giờ đã chọn
                                        Calendar selectedCalendar = Calendar.getInstance();
                                        selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                        selectedCalendar.set(Calendar.MINUTE, minute);
                                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                                "dd/MM/yyyy HH:mm", Locale.getDefault());
                                        String selectedDateTime = dateFormat.format(selectedCalendar.getTime());
                                        // Hiển thị thông tin đã chọn trong TextView
                                        timeStart.setText(selectedDateTime);
                                        ;
                                    }
                                },
                                hourOfDay, minute, true);
                        timePickerDialog.show();
                    }
                },
                year, month, day);
        datePickerDialog.show();
    }
    private void showDateTimePickerDialogEnd() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        // Hiển thị DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Lưu ngày đã chọn
                        final String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;

                        // Hiển thị TimePickerDialog sau khi chọn ngày
                        TimePickerDialog timePickerDialog = new TimePickerDialog(
                                getContext(),
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        // Lưu giờ đã chọn
                                        Calendar selectedCalendar = Calendar.getInstance();
                                        selectedCalendar.set(Calendar.YEAR, year);
                                        selectedCalendar.set(Calendar.MONTH, month);
                                        selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                        selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                        selectedCalendar.set(Calendar.MINUTE, minute);

                                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                                "dd/MM/yyyy HH:mm", Locale.getDefault());
                                        String selectedDateTime = dateFormat.format(selectedCalendar.getTime());

                                        // Hiển thị thông tin đã chọn trong TextView
                                        timeEnd.setText(selectedDateTime);
                                    }
                                },
                                hourOfDay, minute, true);
                        timePickerDialog.show();
                    }
                },
                year, month, day);
        datePickerDialog.show();
    }
}
