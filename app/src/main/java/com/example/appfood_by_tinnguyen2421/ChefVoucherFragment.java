package com.example.appfood_by_tinnguyen2421;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.appfood_by_tinnguyen2421.Chef.ChefActivity.Chef_PostDish;
import com.example.appfood_by_tinnguyen2421.Chef.ChefAdapter.ChefCateAdapter;
import com.example.appfood_by_tinnguyen2421.Chef.ChefModel.Chef;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class ChefVoucherFragment extends Fragment {
    private TextView timeStart,timeEnd,nameVoucher,btnCancell,btnAceptt;
    Spinner dishName;
    RadioButton yess;
    RadioButton noo;
    RadioGroup voucherAuth;
    ChefVoucherAdapter chefVoucherAdapter;
    List<ChefVoucher> chefVoucherList;
    ArrayList<String> categoryList;
    ProgressDialog progressDialog;
    TextInputLayout voucherID,voucherValue;
    String RandomUID;
    RecyclerView recyclerViewVoucher,rcvInviteEventt;
    LinearLayout linearLayoutt5;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference dataaa;
    FirebaseAuth FAuth;
    StorageReference ref;
    String State,City,Sub;
    private String timeStartt,timeEndd,voucherAuthh,nameVoucherr,voucherIDD,voucherValuee,dishNamee;
    public ChefVoucherFragment() {
        // Required empty public constructor
    }
    TextView createVoucher;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chef_voucher, container, false);
        createVoucher=view.findViewById(R.id.CreateVoucher);
        rcvInviteEventt=view.findViewById(R.id.rcvInviteEvent);
        recyclerViewVoucher=view.findViewById(R.id.rcvVoucherDetail);
        categoryList = new ArrayList<>();
        chefVoucherList=new ArrayList<>();
        recyclerViewVoucher.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewVoucher.setLayoutManager(layoutManager);
        chefVoucher();
        createVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCreateVoucherDialog();
            }
        });
        return view;
    }
    public void chefVoucher()
    {
        databaseReference=FirebaseDatabase.getInstance().getReference("ChefVoucher").child(FirebaseAuth.getInstance().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chefVoucherList.clear();
                HashSet<String> uniqueVoucherIds = new HashSet<>();
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    ChefVoucher chefVoucher = dataSnapshot.getValue(ChefVoucher.class);
                    chefVoucherList.add(chefVoucher);
                }
                chefVoucherAdapter = new ChefVoucherAdapter(getContext(), chefVoucherList);
                recyclerViewVoucher.setAdapter(chefVoucherAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void openCreateVoucherDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.chef_dialog_create_voucher);
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
        timeStart=dialog.findViewById(R.id.tvTimeStartt);
        timeEnd=dialog.findViewById(R.id.tvTimeEndd);
        nameVoucher=dialog.findViewById(R.id.tvTime2);
        voucherID=dialog.findViewById(R.id.edtVoucherID);
        voucherAuth=dialog.findViewById(R.id.radioGroup);
        voucherValue=dialog.findViewById(R.id.edtGiamGia);
        dishName=dialog.findViewById(R.id.SpinnerReason);
        btnCancell=dialog.findViewById(R.id.btnCancel);
        btnAceptt=dialog.findViewById(R.id.btnAcept);
        linearLayoutt5=dialog.findViewById(R.id.linear5);
         yess=dialog.findViewById(R.id.Yes);
         noo=dialog.findViewById(R.id.No);
        showdataspinner();
        dialog.setCancelable(false);
        linearLayoutt5.setVisibility(View.GONE);
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

        voucherAuth.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.Yes) {
                    linearLayoutt5.setVisibility(View.GONE);
                } else if (checkedId == R.id.No) {
                    linearLayoutt5.setVisibility(View.VISIBLE);
                }
                else
                {
                    Toast.makeText(getContext(), "Vui lòng chọn Yes hoặc No", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnAceptt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isvalid()) {
                    uploadVoucher();
                }
            }
        });
        dialog.show();
    }
    public void showdataspinner()
    {
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dataaa = firebaseDatabase.getInstance().getReference("Chef").child(userid);
        dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Chef chefc = dataSnapshot.getValue(Chef.class);
                State = chefc.getState();
                City = chefc.getCity();
                Sub = chefc.getSuburban();
                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("FoodSupplyDetails").child(State).child(City).child(Sub);
                databaseReference1.child(userid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (categoryList != null) {
                            categoryList.clear();
                            for (DataSnapshot item : snapshot.getChildren()) {
                                categoryList.add(item.child("Dishes").getValue(String.class));
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), com.hbb20.R.layout.support_simple_spinner_dropdown_item, categoryList);
                            dishName.setAdapter(arrayAdapter);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void uploadVoucher()
    {
        RandomUID= UUID.randomUUID().toString();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
        timeStartt=timeStart.getText().toString().trim();
        timeEndd=timeEnd.getText().toString().trim();
        voucherIDD=voucherID.getEditText().getText().toString().trim();
        nameVoucherr=nameVoucher.getText().toString().trim();
        voucherValuee=voucherValue.getEditText().getText().toString().trim();
        dishNamee=dishName.getSelectedItem().toString().trim();
        progressDialog.setMessage("Đang tạo");
        progressDialog.show();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("StartDate","" +timeStartt);
        hashMap.put("EndDate", ""+timeEndd);
        hashMap.put("VoucherID", ""+voucherIDD);
        hashMap.put("VoucherName", ""+nameVoucherr);
        if (yess.isChecked()) {
            hashMap.put("VoucherApply", ""+yess.getText().toString().trim());
        } else if (noo.isChecked()) {
            hashMap.put("VoucherApply", ""+noo.getText().toString().trim());
        }
        hashMap.put("VoucherValue", ""+voucherValuee);
        hashMap.put("DishUseVoucher", ""+dishNamee);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ChefVoucher");
        reference.child(FirebaseAuth.getInstance().getUid()).child(RandomUID).setValue(hashMap).addOnSuccessListener(
                        unused -> {
                            progressDialog.dismiss();
                            new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog)
                                    .setMessage("Thành công")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
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
    public boolean isvalid() {
        timeStartt=timeStart.getText().toString().trim();
        timeEndd=timeEnd.getText().toString().trim();
        voucherIDD=voucherID.getEditText().getText().toString().trim();
        nameVoucherr=nameVoucher.getText().toString().trim();
        voucherValuee=voucherValue.getEditText().getText().toString().trim();
        dishNamee=dishName.getSelectedItem().toString().trim();
        voucherID.setErrorEnabled(false);
        voucherID.setError("");
        voucherValue.setErrorEnabled(false);
        voucherValue.setError("");
        boolean isValidVoucherID = false,isValidVoucherValue = false,isValidDate=false,isvalid = false;
        if (TextUtils.isEmpty(voucherIDD)) {
            voucherID.setErrorEnabled(true);
            voucherID.setError("Mã voucher không được để trống");
        }
        else {
            if(voucherIDD.length()>12)
            {
                voucherID.setErrorEnabled(true);
                voucherID.setError("voucher không được quá 12 kí tự");
            }
            else if (voucherIDD.length()<5) {
                voucherID.setErrorEnabled(true);
                voucherID.setError("voucher không được ít hơn 5 kí tự");
            }
            else {
                isValidVoucherID = true;
            }
        }
        if (TextUtils.isEmpty(voucherValuee)) {
            voucherValue.setErrorEnabled(true);
            voucherValue.setError("Mã voucher không được để trống");
        }
        else {
            double voucherValueDouble=Double.parseDouble(voucherValuee);
            if(voucherValueDouble>1000000)
            {
                voucherValue.setErrorEnabled(true);
                voucherValue.setError("voucher không quá 1000000 đ");
            }
            else if (voucherValueDouble<5000) {
                voucherValue.setErrorEnabled(true);
                voucherValue.setError("voucher không được ít hơn 5000 đ");
            }
            else
                isValidVoucherValue=true;
        }
        timeStart.setError(null);
        timeEnd.setError(null);
        if (TextUtils.isEmpty(timeStartt)) {
            timeStart.setError("Vui lòng chọn ngày và giờ bắt đầu");
        } else if (TextUtils.isEmpty(timeEndd)) {
            timeEnd.setError("Vui lòng chọn ngày và giờ kết thúc");
        } else {
            isValidDate = true;
        }
        isvalid = (isValidVoucherID&&isValidVoucherValue&&isValidDate) ? true : false;
        return isvalid;
    }
}