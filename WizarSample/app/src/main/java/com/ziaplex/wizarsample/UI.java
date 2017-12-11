package com.ziaplex.wizarsample;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UI {

    public interface CustomButtonViewListener {

        void onButtonClick(View view, String text);
    }

    public static View createCustomButtonView(final Context context, int iconResourceId, final String text, final CustomButtonViewListener listener) {
        FrameLayout p = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.view_custom_button, null);
        if (p != null) {
            {
                final LinearLayout v = p.findViewById(R.id.container);
                if (v != null){
                    v.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            v.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGrey));
                            if (listener != null)
                                listener.onButtonClick(v, text);
                        }
                    });
                }
            }
            {
                ImageView v = p.findViewById(R.id.img_icon);
                if (v != null)
                    v.setImageResource(iconResourceId);
            }
            {
                TextView v = p.findViewById(R.id.txt_text);
                if (v != null)
                    v.setText(text);
            }
        }
        return p;
    }

    public static View createCustomView(Context context, int width, int height) {
        View v = new View(context);
        v.setLayoutParams(getLinearLayoutParams(width, height));
        return v;
    }

    public static LinearLayout.LayoutParams getLinearLayoutParams(int width, int height) {
        return new LinearLayout.LayoutParams(width, height);
    }

    public static LinearLayout.LayoutParams getLinearLayoutParams(int w, int h, int wt) {
        LinearLayout.LayoutParams v = getLinearLayoutParams(w, h);
        v.weight = wt;
        return v;
    }

    public static View createCardDetailsView(Context context, CardDetails cardDetails) {
        LinearLayout p = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.view_card_details, null);
        if (p != null && cardDetails != null) {
            p.setLayoutParams(getLinearLayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) context.getResources().getDimension(R.dimen._205sdp)));
            {
                TextView v = p.findViewById(R.id.txt_pan);
                if (v != null)
                    v.setText(cardDetails.getPan());
            }
            {
                TextView v = p.findViewById(R.id.txt_card_holder_name);
                if (v != null)
                    v.setText(cardDetails.getCardHolderName());
            }
            {
                TextView v = p.findViewById(R.id.txt_expiry_date);
                if (v != null)
                    v.setText(cardDetails.getExpiryDate());
            }
            {
                TextView v = p.findViewById(R.id.txt_service_code);
                if (v != null)
                    v.setText(cardDetails.getServiceCode());
            }
        }
        return p;
    }

    public static View createMessageView(Context context, int iconResourceId, final String message, final CustomButtonViewListener listener) {
        final LinearLayout p = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.view_custom_message, null);
        if (p != null) {
            {
                ImageView v = p.findViewById(R.id.img_icon);
                if (v != null)
                    v.setImageResource(iconResourceId);
            }
            {
                TextView v = p.findViewById(R.id.txt_message);
                if (v != null) {
                    v.setText(message);
                    if (listener != null) {
                        v.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                        v.setBackground(context.getResources().getDrawable(R.drawable.border_button));
                        v.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                listener.onButtonClick(p, message);
                            }
                        });
                    }
                }
            }
        }
        return p;
    }

    public static void showToastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
