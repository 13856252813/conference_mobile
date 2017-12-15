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


public class CustomDeleteUserDialog extends Dialog {
  
    public CustomDeleteUserDialog(Context context) {  
        super(context);  
    }  
  
    public CustomDeleteUserDialog(Context context, int theme) {  
        super(context, theme);  
    }

    public static class Builder {
        private Context context;
        private View contentView;

        private DialogInterface.OnClickListener deleteUserButtonClickListener;
        private DialogInterface.OnClickListener cancelButtonClickListener;

        public Builder(Context context) {  
            this.context = context;  
        }
  
        public Builder setContentView(View v) {  
            this.contentView = v;  
            return this;  
        }

        public Builder setDeleteUserButton(DialogInterface.OnClickListener listener) {
            this.deleteUserButtonClickListener = listener;
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

            if (deleteUserButtonClickListener != null) {
                ((Button) layout.findViewById(R.id.attend_bt_phone_address))
                        .setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                deleteUserButtonClickListener.onClick(dialog,
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