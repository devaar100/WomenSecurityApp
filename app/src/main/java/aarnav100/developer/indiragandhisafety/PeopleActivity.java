package aarnav100.developer.indiragandhisafety;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class PeopleActivity extends AppCompatActivity {

    private ArrayList<People> peoples = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorMain));

        peoples.add(new People("Aarnav Jindal","9999999999","aarnav@gmail.com"));
        peoples.add(new People("Abhinav Duggal","8888888888","abhinav@gmail.com"));
        peoples.add(new People("Shashank Chauhan","7777777777","shashank@gmail.com"));
        ((ListView)findViewById(R.id.list)).setAdapter(new PeopleAdapter());
    }

    public class PeopleAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return peoples.size();
        }

        @Override
        public People getItem(int i) {
            return peoples.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            People person = getItem(i);
            LayoutInflater li=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=li.inflate(R.layout.layout_person,null);
            ((TextView)view.findViewById(R.id.name)).setText(person.name);
            ((TextView)view.findViewById(R.id.phone)).setText(person.phone);
            ((TextView)view.findViewById(R.id.email)).setText(person.email);
            return view;
        }
    }

    public  class People{
        String name,phone,email;

        public People(String name, String phone, String email) {
            this.name = name;
            this.phone = phone;
            this.email = email;
        }
    }
}
