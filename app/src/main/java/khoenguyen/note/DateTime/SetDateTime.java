package khoenguyen.note.DateTime;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Admin on 4/28/2017.
 */

public class SetDateTime {
    private Context context;
    private TextView txtDate, txtTime, timeDefault;
    private ImageButton imageButtonAlarm;
    public SetDateTime(Context _context, TextView date, TextView time, TextView timeDe, ImageButton imageButton) {
        context = _context;
        txtDate = date;
        txtTime = time;
        timeDefault = timeDe;
        imageButtonAlarm = imageButton;
    }
    //bat cac su kien cho cac control button
    public void addEventFormWidgets()
    {
        imageButtonAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
                showTimePickerDialog();
            }
        });
    }
    private void showDatePickerDialog() {
        DatePickerDialog.OnDateSetListener callback=new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear,
                                  int dayOfMonth) {
                //Mỗi lần thay đổi ngày tháng năm thì cập nhật lại TextView Date
                txtDate.setText(
                        (dayOfMonth) +"/"+(monthOfYear+1)+"/"+year);
            }
        };
        //các lệnh dưới này xử lý ngày giờ trong DatePickerDialog
        //sẽ giống với trên TextView khi mở nó lên
        String s=txtDate.getText()+"";
        String strArrtmp[]=s.split("/");
        int ngay = Integer.parseInt(strArrtmp[0]);
        int thang = Integer.parseInt(strArrtmp[1])-1;
        int nam = Integer.parseInt(strArrtmp[2]);
        DatePickerDialog dialog = new DatePickerDialog(
                context,
                callback, nam, thang, ngay);
        dialog.setTitle("Set Date");
        dialog.show();
    }

    public void showTimePickerDialog()
    {
        TimePickerDialog.OnTimeSetListener callback=new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view,
                                  int hourOfDay, int minute) {
                //Xử lý lưu giờ và AM,PM
                String s=hourOfDay +":"+minute;
                int hourTam=hourOfDay;
                if(hourTam>12)
                    hourTam=hourTam-12;
                txtTime.setText
                        (hourTam +":"+minute +(hourOfDay>12?" PM":" AM"));
                //lưu giờ thực vào tag
                txtTime.setTag(s);
                //lưu viết lại giờ vào hourFinish
                //cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                //cal.set(Calendar.MINUTE, minute);
                //hourFinish=cal.getTime();
            }
        };
        //các lệnh dưới này xử lý ngày giờ trong TimePickerDialog
        //sẽ giống với trên TextView khi mở nó lên
        String s=txtTime.getTag()+"";
        Log.v("NoteActivity", "chuoi s " + s);
        String strArr[]=s.split(":");
        int gio=Integer.parseInt(strArr[0]);
        int phut=Integer.parseInt(strArr[1]);

        TimePickerDialog time=new TimePickerDialog(
                context,
                callback, gio, phut, true);
        time.setTitle("Chọn giờ hoàn thành");
        time.show();
    }

    /**
     * Hàm lấy các thông số mặc định khi lần đầu tiền chạy ứng dụng
     */
    public void getDefaultInfor()
    {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dfm = null;
        dfm=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String strDate=dfm.format(c.getTime());
        txtDate.setText(strDate);
        dfm=new SimpleDateFormat("hh:mm a",Locale.getDefault());
        String strTime=dfm.format(c.getTime());
        txtTime.setText(strTime);
        timeDefault.setText(strDate + " " + strTime);
        //lấy giờ theo 24 để lập trình theo Tag
        dfm=new SimpleDateFormat("HH:mm",Locale.getDefault());
        txtTime.setTag(dfm.format(c.getTime()));
    }

}
