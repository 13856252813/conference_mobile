package com.txt.conference.utils;
  
import android.app.Dialog;  
import android.content.Context;  
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;  
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;  
import android.widget.TextView;

import com.txt.conference.R;


public class CustomExtendDialog extends Dialog {
  
    public CustomExtendDialog(Context context) {  
        super(context);  
    }  
  
    public CustomExtendDialog(Context context, int theme) {  
        super(context, theme);  
    }

    public static class Builder {
        private Context context;
        private View contentView;

        private DialogInterface.OnClickListener TenMButtonClickListener;
        private DialogInterface.OnClickListener HalfHButtonClickListener;
        private DialogInterface.OnClickListener OneHButtonClickListener;
        private DialogInterface.OnClickListener cancelButtonClickListener;

        public Builder(Context context) {  
            this.context = context;  
        }
  
        public Builder setContentView(View v) {  
            this.contentView = v;  
            return this;  
        }

        public Builder set10mButton(DialogInterface.OnClickListener listener) {
            this.TenMButtonClickListener = listener;
            return this;
        }
        public Builder set30mButton(DialogInterface.OnClickListener listener) {
            this.HalfHButtonClickListener = listener;
            return this;
        }
        public Builder set1HButton(DialogInterface.OnClickListener listener) {
            this.OneHButtonClickListener = listener;
            return this;
        }
        public Builder setCancelButton(DialogInterface.OnClickListener listener) {
            this.cancelButtonClickListener = listener;
            return this;
        }

        public CustomAttendDialog create() {
            LayoutInflater inflater = (LayoutInflater) context  
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
            // instantiate the dialog with the custom Theme  
            final CustomAttendDialog dialog = new CustomAttendDialog(context,R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_other_extend_layout, null);
            dialog.addContentView(layout, new LayoutParams(  
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

            dialog.setContentView(layout);

            if (TenMButtonClickListener != null) {
                ((Button) layout.findViewById(R.id.attend_bt_company_address))
                        .setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                TenMButtonClickListener.onClick(dialog,
                                        DialogInterface.BUTTON_NEUTRAL);
                            }
                        });
            }

            if (HalfHButtonClickListener != null) {
                ((Button) layout.findViewById(R.id.attend_bt_conference_device))
                        .setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                HalfHButtonClickListener.onClick(dialog,
                                        DialogInterface.BUTTON_NEUTRAL);
                            }
                        });
            }

            if (OneHButtonClickListener != null) {
                ((Button) layout.findViewById(R.id.attend_bt_phone_address))
                        .setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                OneHButtonClickListener.onClick(dialog,
                                        DialogInterface.BUTTON_NEUTRAL);
                            }
                        });
            }

            if (cancelButtonClickListener != null) {
                ((Button) layout.findViewById(R.id.attend_bt_cancel))
                        .setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                cancelButtonClickListener.onClick(dialog,
                                        DialogInterface.BUTTON_NEUTRAL);
                            }
                        });
            }

            Window window = dialog.getWindow();

            window.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            return dialog;  
        }  
    }  
}  