package com.example.appfood_by_tinnguyen2421.Customerr.CustomerFragment.CustomerOrdersFragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfood_by_tinnguyen2421.Account.UserModel;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerAdapter.CustomerCartAdapter;
import com.example.appfood_by_tinnguyen2421.Customerr.CustomerModel.Cart;
import com.example.appfood_by_tinnguyen2421.R;
import com.example.appfood_by_tinnguyen2421.ReusableCodeForAll;
import com.example.appfood_by_tinnguyen2421.SendNotification.APIService;
import com.example.appfood_by_tinnguyen2421.SendNotification.Client;
import com.example.appfood_by_tinnguyen2421.SendNotification.Data;
import com.example.appfood_by_tinnguyen2421.SendNotification.MyResponse;
import com.example.appfood_by_tinnguyen2421.SendNotification.NotificationSender;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.momo.momo_partner.AppMoMoLib;

//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class CustomerCartFragment extends Fragment {

    RecyclerView recyclecart;
    private List<Cart> cartModelList;
    private CustomerCartAdapter adapter;
    private LinearLayout TotalBtns,payment;
    DatabaseReference databaseReference, data, reference, ref, getRef, dataa;
    public static TextView grandt;
    Button remove, placeorder;
    RadioGroup radioGroup;

    RadioButton onlinePayment,cashPayment;
    String DishId, RandomUId, ChefId;
    private ProgressDialog progressDialog;
    private APIService apiService;
    private boolean paymentCompleted = false;
    private String amount;
    private String fee;
    int environment = 0;//developer default
    private String merchantName = "HoangNgoc";
    private String merchantCode = "MOMOC2IC20220510";
    private String merchantNameLabel = "TrongTin";
    private String description = "Thanh toán đơn hàng";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Giỏ hàng của bạn");
        View v = inflater.inflate(R.layout.fragment_customercart, null);
        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT); // AppMoMoLib.ENVIRONMENT.PRODUCTION
        recyclecart = v.findViewById(R.id.recyclecart);
        recyclecart.setHasFixedSize(true);
        recyclecart.setLayoutManager(new LinearLayoutManager(getContext()));
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        cartModelList = new ArrayList<>();
        payment=v.findViewById(R.id.Payment);
        radioGroup = v.findViewById(R.id.radioGroupPayment);
        cashPayment = v.findViewById(R.id.CashPayment);
        onlinePayment = v.findViewById(R.id.OnlinePayment);
        grandt = v.findViewById(R.id.GT);
        remove = v.findViewById(R.id.RM);
        placeorder = v.findViewById(R.id.PO);
        TotalBtns = v.findViewById(R.id.TotalBtns);

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        customercart();
        return v;
    }
    private  void processDataAfterPayment(String address,String Addnote,UserModel userModel,String RandomUID)
    {

        progressDialog.setMessage("Vui lòng đợi ...");
        progressDialog.show();
        reference = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    final Cart cart1 = dataSnapshot1.getValue(Cart.class);
                    DishId = cart1.getDishID();
                    ChefId=cart1.getChefID();
                    final HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("ChefID", ChefId);
                    hashMap.put("DishID", cart1.getDishID());
                    hashMap.put("DishName", cart1.getDishName());
                    hashMap.put("DishQuantity", cart1.getDishQuantity());
                    hashMap.put("DishPrice", cart1.getDishPrice());
                    hashMap.put("RandomUID",RandomUID);
                    hashMap.put("TotalPrice", cart1.getTotalPrice());
                    hashMap.put("UserID",FirebaseAuth.getInstance().getCurrentUser().getUid());
                    hashMap.put("ImageURL", cart1.getImageURL());
                    FirebaseDatabase.getInstance().getReference("CustomerFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUId).child("Dishes").child(DishId).setValue(hashMap);
                    FirebaseDatabase.getInstance().getReference("ChefPendingOrders").child(ChefId).child(RandomUId).child("Dishes").child(DishId).setValue(hashMap);

                }
                ref = FirebaseDatabase.getInstance().getReference("Cart").child("GrandTotal").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("GrandTotal");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        LocalDateTime currentDateTime = LocalDateTime.now();

                        // Định dạng ngày giờ thành chuỗi nếu cần
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm, dd/MM/yyyy");
                        String formattedDateTime = currentDateTime.format(formatter);
                        String grandtotal = dataSnapshot.getValue(String.class);
                        HashMap<String, String> hashMap1 = new HashMap<>();
                        hashMap1.put("Address", address);
                        hashMap1.put("GrandTotalPrice", String.valueOf(grandtotal));
                        hashMap1.put("MobileNumber", userModel.getPhoneNumber());
                        hashMap1.put("RandomUID",RandomUID);
                        hashMap1.put("Name", userModel.getFirstName() + " " + userModel.getLastName());
                        hashMap1.put("Note", Addnote);
                        hashMap1.put("OrderDate", formattedDateTime);
                        hashMap1.put("PaymentMethod", String.valueOf(radioGroup.getCheckedRadioButtonId()));
                        hashMap1.put("OrderStatus","Chờ xác nhận...");
                        FirebaseDatabase.getInstance().getReference("CustomerFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUId).child("OtherInformation").setValue(hashMap1).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                FirebaseDatabase.getInstance().getReference("ChefPendingOrders").child(ChefId).child(RandomUId).child("OtherInformation").setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(FirebaseAuth.getInstance().getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                FirebaseDatabase.getInstance().getReference("Cart").child("GrandTotal").child(FirebaseAuth.getInstance().getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        FirebaseDatabase.getInstance().getReference("AlreadyOrdered").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("isOrdered").setValue("true").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                FirebaseDatabase.getInstance().getReference().child("Tokens").child(ChefId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                        String usertoken = dataSnapshot.getValue(String.class);
                                                                        sendNotifications(usertoken, "Đơn hàng mới", "Bạn có đơn hàng mới", "Order");
                                                                        progressDialog.dismiss();
                                                                        ReusableCodeForAll.ShowAlert(getContext(), "", "Đơn hàng của bạn đang chờ cửa hàng duyệt");
                                                                    }
                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    //Get token through MoMo app
    private void requestPayment(String grandTotal,String RandomUID,String address,String Addnote) {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);
        if (grandTotal != null )
            amount =grandTotal ;

        Map<String, Object> eventValue = new HashMap<>();
        //client Required
        eventValue.put("merchantname", merchantName); //Tên đối tác. được đăng ký tại https://business.momo.vn. VD: Google, Apple, Tiki , CGV Cinemas
        eventValue.put("merchantcode", merchantCode); //Mã đối tác, được cung cấp bởi MoMo tại https://business.momo.vn
        eventValue.put("amount", Integer.parseInt(amount)); //Kiểu integer
        eventValue.put("orderId", RandomUID.substring(Math.max(0,RandomUID.length()-10))); //uniqueue id cho Bill order, giá trị duy nhất cho mỗi đơn hàng
        eventValue.put("orderLabel", "Mã đơn hàng"); //gán nhãn

        //client Optional - bill info
        eventValue.put("merchantnamelabel", "Dịch vụ");//gán nhãn
        eventValue.put("fee",Integer.parseInt(amount)); //Kiểu integer
        eventValue.put("description", description); //mô tả đơn hàng - short description

        //client extra data
        eventValue.put("requestId",  merchantCode+"merchant_billId_"+System.currentTimeMillis());
        eventValue.put("partnerCode", merchantCode);
        //Example extra data
        JSONObject objExtraData = new JSONObject();
        try {
            objExtraData.put("site_code", "008");
            objExtraData.put("site_name", "CGV Cresent Mall");
            objExtraData.put("screen_code", 0);
            objExtraData.put("screen_name", "Special");
            objExtraData.put("movie_name", "Kẻ Trộm Mặt Trăng 3");
            objExtraData.put("movie_format", "2D");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        eventValue.put("extraData", objExtraData.toString());

        eventValue.put("extra", "");
        Activity activity = getActivity(); // Lấy tham chiếu đến Activity chứa Fragment
        if (activity != null) {
            AppMoMoLib.getInstance().requestMoMoCallBack(activity, eventValue); // Gọi phương thức với đối số Activity
        } else {
            // Xử lý trường hợp getActivity() trả về null
        }


    }
    //Get token callback from MoMo app an submit to server side
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
            if(data != null) {
                if(data.getIntExtra("status", -1) == 0) {
                    //TOKEN IS AVAILABLE
                    String token = data.getStringExtra("data"); //Token response
                    String phoneNumber = data.getStringExtra("phonenumber");
                    String env = data.getStringExtra("env");
                    if(env == null){
                        env = "app";
                    }

                    if(token != null && !token.equals("")) {
                    } else {
                        Log.d("TAG", "onActivityResult: "+"Demo");                    }
                } else if(data.getIntExtra("status", -1) == 1) {
                    //TOKEN FAIL
                    String message = data.getStringExtra("message") != null?data.getStringExtra("message"):"Thất bại";
                } else if(data.getIntExtra("status", -1) == 2) {
                    //TOKEN FAIL
                } else {
                    //TOKEN FAIL
                }
            } else {
            }
        } else {
        }
    }
    private void customercart() {

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(userID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cartModelList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Cart cart = snapshot.getValue(Cart.class);
                    cartModelList.add(cart);
                }
                if (cartModelList.size() == 0) {
                    TotalBtns.setVisibility(View.INVISIBLE);
                    payment.setVisibility(View.INVISIBLE);
                    recyclecart.setBackgroundResource(R.drawable.empty_cart);
                } else {
                    TotalBtns.setVisibility(View.VISIBLE);
                    payment.setVisibility(View.VISIBLE);
                    remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("Bạn có chắc chắn muốn xóa món ăn này ?");
                            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                                    FirebaseDatabase.getInstance().getReference("Cart").child("GrandTotal").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                                }
                            });
                            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    });
                    String UserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    data = FirebaseDatabase.getInstance().getReference("Customer").child(UserID);
                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final UserModel userModel = dataSnapshot.getValue(UserModel.class);
                            placeorder.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int selectedId = radioGroup.getCheckedRadioButtonId();
                                    if(selectedId==-1)
                                    {
                                        Toast.makeText(getContext(), "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                            if(cashPayment.isChecked())
                                            {
                                                FirebaseDatabase.getInstance().getReference("AlreadyOrdered").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("isOrdered").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        String ss = "";
                                                        if (dataSnapshot.exists()) {
                                                            ss = dataSnapshot.getValue(String.class);
                                                        }
                                                        if (ss.trim().equalsIgnoreCase("false") || ss.trim().equalsIgnoreCase("")) {
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                            builder.setTitle("Nhập địa chỉ");
                                                            LayoutInflater inflater = getActivity().getLayoutInflater();
                                                            View view = inflater.inflate(R.layout.enter_address, null);
                                                            final EditText localaddress = (EditText) view.findViewById(R.id.LA);
                                                            final EditText addnote = (EditText) view.findViewById(R.id.addnote);

                                                            RadioGroup group = (RadioGroup) view.findViewById(R.id.grp);
                                                            final RadioButton home = (RadioButton) view.findViewById(R.id.HA);
                                                            final RadioButton other = (RadioButton) view.findViewById(R.id.OA);
                                                            builder.setView(view);
                                                            group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                                @Override
                                                                public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                                    if (home.isChecked()) {
                                                                        localaddress.setText(userModel.getAddress() + ", " + userModel.getWard());
                                                                    } else if (other.isChecked()) {
                                                                        localaddress.getText().clear();
                                                                        Toast.makeText(getContext(), "Kiểm tra", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    String address = localaddress.getText().toString().trim();
                                                                    String Addnote = addnote.getText().toString().trim();
                                                                    RandomUId = UUID.randomUUID().toString();
                                                                    processDataAfterPayment(address,Addnote,userModel,RandomUId);

                                                                    dialog.dismiss();
                                                                }
                                                            });
                                                            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    dialog.dismiss();
                                                                }
                                                            });
                                                            AlertDialog aler = builder.create();
                                                            aler.show();
                                                        } else {
                                                            ReusableCodeForAll.ShowAlert(getContext(), "Lỗi", "Oops ! có vẻ bạn đã có một đơn đặt hàng rồi nên bạn không được đặt đơn khác cho đến khi đơn hàng đầu tiên được giao ");
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    }
                                                });
                                            }
                                            else if (onlinePayment.isChecked()){
                                                FirebaseDatabase.getInstance().getReference("AlreadyOrdered").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("isOrdered").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        String ss = "";
                                                        if (dataSnapshot.exists()) {
                                                            ss = dataSnapshot.getValue(String.class);
                                                        }
                                                        if (ss.trim().equalsIgnoreCase("false") || ss.trim().equalsIgnoreCase("")) {
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                            builder.setTitle("Nhập địa chỉ");
                                                            LayoutInflater inflater = getActivity().getLayoutInflater();
                                                            View view = inflater.inflate(R.layout.enter_address, null);
                                                            final EditText localaddress = (EditText) view.findViewById(R.id.LA);
                                                            final EditText addnote = (EditText) view.findViewById(R.id.addnote);
                                                            RadioGroup group = (RadioGroup) view.findViewById(R.id.grp);
                                                            final RadioButton home = (RadioButton) view.findViewById(R.id.HA);
                                                            final RadioButton other = (RadioButton) view.findViewById(R.id.OA);
                                                            builder.setView(view);
                                                            group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                                @Override
                                                                public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                                    if (home.isChecked()) {
                                                                        localaddress.setText(userModel.getAddress() + ", " + userModel.getWard());
                                                                    } else if (other.isChecked()) {
                                                                        localaddress.getText().clear();
                                                                        Toast.makeText(getContext(), "Kiểm tra", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    ref = FirebaseDatabase.getInstance().getReference("Cart").child("GrandTotal").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("GrandTotal");
                                                                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                            String grandtotal = snapshot.getValue(String.class);
                                                                            String address = localaddress.getText().toString().trim();
                                                                            String Addnote = addnote.getText().toString().trim();
                                                                            RandomUId = UUID.randomUUID().toString();
                                                                            requestPayment(grandtotal,RandomUId,address,Addnote);
                                                                            processDataAfterPayment(address,Addnote,userModel,RandomUId);
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                        }
                                                                    });
                                                                    dialog.dismiss();
                                                                }
                                                            });
                                                            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    dialog.dismiss();
                                                                }
                                                            });
                                                            AlertDialog aler = builder.create();
                                                            aler.show();
                                                        } else {
                                                            ReusableCodeForAll.ShowAlert(getContext(), "Lỗi", "Oops ! có vẻ bạn đã có một đơn đặt hàng rồi nên bạn không được đặt đơn khác cho đến khi đơn hàng đầu tiên được giao ");
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    }
                                                });
                                            }

                                        }
                                    }
                            });
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
                adapter = new CustomerCartAdapter(getContext(), cartModelList);
                recyclecart.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void sendNotifications(String usertoken, String title, String message, String order) {

        Data data = new Data(title, message, order);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(getContext(), "Thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }
}
