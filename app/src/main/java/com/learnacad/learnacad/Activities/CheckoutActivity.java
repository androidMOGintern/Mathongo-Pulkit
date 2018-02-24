package com.learnacad.learnacad.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.learnacad.learnacad.R;
import com.payumoney.core.PayUmoneyConstants;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.ContentValues.TAG;

public class CheckoutActivity extends AppCompatActivity {

    private PayUmoneySdkInitializer.PaymentParam mPaymentParams;

    DatabaseReference myRootref;
    private Integer Coins;
    TextInputLayout checkout_textinputlayout;
    Integer amount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        checkout_textinputlayout = findViewById(R.id.checkout_textinputlayout);

        myRootref = FirebaseDatabase.getInstance().getReference("users/" + getIntent().getStringExtra("studentid"));
        myRootref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Coins = dataSnapshot.child("coins").getValue(Integer.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
        checkout_textinputlayout.getEditText().getText().toString();
    }

    public void setupPayUMoney(View view) {

        if (!checkout_textinputlayout.getEditText().getText().toString().isEmpty()) {
            String txnId = System.currentTimeMillis() + "";
            String phone = "9582054664";
            String productName = "Coins";
            String firstName = "Pulkit Aggarwal";
            String email = "aggarwalpulkit596@gmail.com";
            String udf1 = "";
            String udf2 = "";
            String udf3 = "";
            String udf4 = "";
            String udf5 = "";
            String udf6 = "";
            String udf7 = "";
            String udf8 = "";
            String udf9 = "";
            String udf10 = "";
            amount = Integer.valueOf(checkout_textinputlayout.getEditText().getText().toString());
            checkout_textinputlayout.setError(amount + "is equal to " + amount * 5 + " coins");
            PayUmoneySdkInitializer.PaymentParam.Builder builder = new
                    PayUmoneySdkInitializer.PaymentParam.Builder();
            builder.setAmount(amount)                          // Payment amount
                    .setTxnId(txnId)                                             // Transaction ID
                    .setPhone(phone)                                           // User Phone number
                    .setProductName(productName)                   // Product Name or description
                    .setFirstName(firstName)                              // User First name
                    .setEmail(email)                                            // User Email ID
                    .setsUrl("https://www.payumoney.com/mobileapp/payumoney/success.php")                    // Success URL (surl)
                    .setfUrl("https://www.payumoney.com/mobileapp/payumoney/failure.php")                     //Failure URL (furl)
                    .setUdf1("")
                    .setUdf2("")
                    .setUdf3("")
                    .setUdf4("")
                    .setUdf5("")
                    .setUdf6("")
                    .setUdf7("")
                    .setUdf8("")
                    .setUdf9("")
                    .setUdf10("")
                    .setIsDebug(false)                              // Integration environment - true (Debug)/ false(Production)
                    .setKey("j0LyWpPK")                        // Merchant key
                    .setMerchantId("6089738");             // Merchant ID

            mPaymentParams = builder.build();

            mPaymentParams = calculateServerSideHashAndInitiatePayment1(mPaymentParams);

            PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, this, R.style.AppTheme_default, false);
        } else {
            checkout_textinputlayout.setError("Cant Be Empty");
        }


    }

    private PayUmoneySdkInitializer.PaymentParam calculateServerSideHashAndInitiatePayment1(PayUmoneySdkInitializer.PaymentParam mPaymentParams) {
        StringBuilder stringBuilder = new StringBuilder();
        HashMap<String, String> params = mPaymentParams.getParams();
        stringBuilder.append(params.get(PayUmoneyConstants.KEY) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.TXNID) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.AMOUNT) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.PRODUCT_INFO) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.FIRSTNAME) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.EMAIL) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF1) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF2) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF3) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF4) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF5) + "||||||");

        stringBuilder.append("PX4MSYglPw");

        String hash = hashCal(stringBuilder.toString());
        mPaymentParams.setMerchantHash(hash);

        return mPaymentParams;

    }

    public static String hashCal(String str) {
        byte[] hashseq = str.getBytes();
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();
            for (byte aMessageDigest : messageDigest) {
                String hex = Integer.toHexString(0xFF & aMessageDigest);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException ignored) {
        }
        return hexString.toString();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result Code is -1 send from Payumoney activity
        Log.d("MainActivity", "request code " + requestCode + " resultcode " + resultCode);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data !=
                null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager
                    .INTENT_EXTRA_TRANSACTION_RESPONSE);

            ResultModel resultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT);

            // Check which object is non-null
            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                    myRootref.child("coins").setValue(Coins + amount*5);
                } else {
                    //Failure Transaction
                }

                // Response from Payumoney
                String payuResponse = transactionResponse.getPayuResponse();

                // Response from SURl and FURL
                String merchantResponse = transactionResponse.getTransactionDetails();
                new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Transaction Succesfull")
                        .setContentText("5 Coins Have Been Added To Your Account")
                        .show();
            } else if (resultModel != null && resultModel.getError() != null) {
                Log.d(TAG, "Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                Log.d(TAG, "Both objects are null!");
            }
        }
    }

}
