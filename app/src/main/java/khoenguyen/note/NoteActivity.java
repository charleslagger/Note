package khoenguyen.note;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.turkialkhateeb.materialcolorpicker.ColorChooserDialog;
import com.turkialkhateeb.materialcolorpicker.ColorListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

import khoenguyen.note.DateTime.SetDateTime;
import khoenguyen.note.module.Note;

/**
 * Created by Admin on 4/28/2017.
 */

public class NoteActivity extends AppCompatActivity {
    private TextView txtDate, txtTime, timeDefault;
    private EditText edTitle, edNote;
    private int colorChoose;
    ImageButton imageButtonAlarm;
    private ImageView mImageView;
    private SetDateTime setDateTime;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_GALLERY = 2;
    private static final String IMAGE_DIRECTORY = "/note";

    RelativeLayout rel;
    CardView cv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        //chay logo tren action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        getFormWidgets();
        setDateTime.getDefaultInfor();
        setDateTime.addEventFormWidgets();

        onActivityCreated();


    }

    /**
     * Xu ly sau khi view da duoc khoi tao
     */
    private void onActivityCreated() {
        if (getIntent().getSerializableExtra("edit")!=null) {
            Note editNote = (Note) getIntent().getSerializableExtra("edit");
            //in lai ra man hinh du lieu sua
            txtDate.setText(editNote.getDate().toString());
            txtTime.setText(editNote.getTime().toString());
            edTitle.setText(editNote.getTitle().toString());
            edNote.setText(editNote.getContent().toString());

            //colorChoose = editNote.getColor();

        }
    }

    //Tao icons tren action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.note_menu, menu);
        return true;
    }
    //load cac control theo id
    private void getFormWidgets() {
        edTitle = (EditText) findViewById(R.id.title);
        edNote = (EditText)findViewById(R.id.note);
        txtDate = (TextView) findViewById(R.id.txtDate);
        txtTime = (TextView) findViewById(R.id.txtTime);
        timeDefault = (TextView) findViewById(R.id.timeDefault);
        imageButtonAlarm = (ImageButton) findViewById(R.id.alarm_btn);
        mImageView = (ImageView) findViewById(R.id.cameraImport);
        setDateTime = new SetDateTime(this, txtDate, txtTime, timeDefault, imageButtonAlarm);
        rel = (RelativeLayout) findViewById(R.id.relNote);
        //card set color

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
    }

    //click menu: check, camera, color
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            //click check back to main_activitty
            case R.id.check:
                Note newNote= new Note();// tao moi 1 node
                if (edTitle.getText()!= null&& !edTitle.getText().toString().isEmpty()) {
                    newNote.setTitle(edTitle.getText().toString());
                }
                //newNote.setColor(colorChoose);

                if (edNote.getText()!=null&& !edNote.getText().toString().isEmpty()) {
                    newNote.setContent(edNote.getText().toString());

                }
                if (txtDate.getText()!= null&& !txtDate.getText().toString().isEmpty()&&
                        txtTime.getText()!=null&&!txtTime.getText().toString().isEmpty() ) {
                    newNote.setDate(txtDate.getText().toString());
                    newNote.setTime(txtTime.getText().toString());
                    newNote.setCreateDate(txtDate.getText().toString()+" " + txtTime.getText().toString() );
                }
                newNote.setID(UUID.randomUUID().toString());

                //khi click check thi mang du lieu vao main va them card tuong ung
                Intent data = new Intent();
                data.putExtra("note", newNote);
                setResult(MainActivity.RESULT_CODE_NEW, data);
                finish();

                return true;
            //choose color
            case R.id.colors:
                ColorChooserDialog dialog = new ColorChooserDialog(this);
                dialog.setTitle("Choose Color");
                dialog.setColorListener(new ColorListener() {
                    @Override
                    public void OnColorClick(View v, int color) {
                        rel.setBackgroundColor(color);
                        /*Note note = new Note();
                        note.setColor(color);
                        data.putExtra("note", note);
                        finish();*/
                        //cv.setCardBackgroundColor(color);
                    }
                });
                //customize the dialog however you want
                dialog.show();
                return true;
            case R.id.camera:
                showPictureDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showPictureDialog(){
        android.app.AlertDialog.Builder pictureDialog = new android.app.AlertDialog.Builder(this);
        pictureDialog.setTitle("Insert Picture");
        String[] pictureDialogItems = {
                "Take photo",
                "Choose photo" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                dispatchTakePictureIntent();
                                break;
                            case 1:
                                choosePhotoFromGallary();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    //take a photo with camera app
    protected void dispatchTakePictureIntent() {
        mImageView = (ImageView)findViewById(R.id.cameraImport);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    //Get the thumbnail
    //return Intent delivered to onActivityResult() as a small Bitmap in the extras, under the key "data".
    // The following code retrieves this image and displays it in an ImageView.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
            saveImage(imageBitmap);
        }else if(requestCode == REQUEST_GALLERY && resultCode == RESULT_OK){
            if (data != null) {
                //Get data from Uri
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    //String path = saveImage(bitmap);
                    mImageView.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void choosePhotoFromGallary() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(mediaScanIntent, REQUEST_GALLERY);
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

}
