package khoenguyen.note;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import khoenguyen.note.Adapter.NoteAdapter;
import khoenguyen.note.module.Note;
import khoenguyen.note.util.GridSpacingItemDecoration;

public class MainActivity extends AppCompatActivity {
    RecyclerView listNote;
    ArrayList<Note> dsNote;
    NoteAdapter adapter;
    public static final int RESULT_CODE_NEW = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //chay logo tren action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        listNote = (RecyclerView) findViewById(R.id.cardList);
        setupListView();
        initListener();
    }

    /**
     * Ham lang nghe su kien
     */
    private void initListener() {
    }

    /**
     * Ham khoi tao listview chia lam 2 cot
     */
    private void setupListView() {
        dsNote = new ArrayList<>() ;
        SharedPreferences pre=getSharedPreferences (Constants.Data,MODE_PRIVATE);
        String lsNote = pre.getString(Constants.Note,null) ;
        if (lsNote!=null) {
            Type listType = new TypeToken<ArrayList<Note>>(){}.getType();

            List<Note> lsNoteTemp = new Gson().fromJson(lsNote, listType);
            dsNote.addAll(lsNoteTemp) ;
        }
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        listNote.setLayoutManager(mLayoutManager);
        listNote.addItemDecoration(new GridSpacingItemDecoration(2,10, true));
        adapter = new NoteAdapter(dsNote,this);
        listNote.setAdapter(adapter);
    }

    //Tao icon + tren action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    //chuyen sang activity_note khi click '+' menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.addNote:
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                startActivityForResult(intent,1);
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        Gson gson = new Gson();
        if (dsNote!=null&& !dsNote.isEmpty()) {
            String lsNoteString = gson.toJson(dsNote);
            SharedPreferences pre=getSharedPreferences(Constants.Data, MODE_PRIVATE);
            SharedPreferences.Editor edit=pre.edit();
            edit.putString(Constants.Note,lsNoteString) ;
            edit.commit();
        }

        super.onDestroy();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1) {
            if (resultCode==2) {
                Note newNode = (Note) data.getSerializableExtra("note");
                dsNote.add(newNode);
                adapter.notifyItemInserted(dsNote.size());
            }
        }
        /*if (requestCode == 3){
            if(resultCode == 2){
                adapter.notifyDataSetChanged();
            }
        }*/

    }
}
