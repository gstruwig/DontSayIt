package za.co.datamatix.dontsayit;

        import android.app.Activity;
import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends Activity implements View.OnClickListener {
    // Declare Variables
    ListView listview;
    List<ParseObject> ob;
    ProgressDialog mProgressDialog;
    ArrayAdapter<String> adapter;
    final ArrayList al = new ArrayList(5);
    long maxFieldNo;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from listview_main.xml
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
      //  Parse.enableLocalDatastore(this);

        Parse.initialize(this, "m2oyaaVCZTqrJaBL5ieduKr286Pzzj9BLrcFDKhK", "dQySBPL6OjR1gy6uHEecRNNv2JygRQUG3FTAzid3");
        recordcount();


    }

  //  int reccnt = recordcount();
// me testing github
    final ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);

    int twoMin = 2 * 60 * 1000; // 2 minutes in milli seconds

    final  CountDownTimer Count = new CountDownTimer(30000, 1000) {
        public void onTick(long millisUntilFinished) {
            int seconds = (int) ((millisUntilFinished / 1000));

            //  textic.setText(seconds + "seconds " + millisUntilFinished / 1000);

        }

        public void onFinish() {
           // adapter.clear();
            adapter.add("Time's up!");
            adapter.notifyDataSetChanged();
            toneGen1.startTone(ToneGenerator.TONE_CDMA_ALERT_INCALL_LITE,1000);
        }
    };


    private void recordcount() {

        ParseQuery<ParseObject> cntquery = new ParseQuery("WordCount");
        // Find objects where the array in arrayKey contains all of the elements 2, 3, and 4.
        cntquery.whereContainedIn("CountIdx", Arrays.asList("1"));


        cntquery.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> posts, ParseException e) {
                String mytext = "";
                if (e == null) {
                    List<String> wordcount = new ArrayList<String>();
                    for (ParseObject post : posts) {
                     //   wordcount.add(post.getString("WordCount"));
                        System.out.println(post.getString("WordCount"));
                        hasValueDoRest(post.getString("WordCount"));
                    }

                  //   Toast.makeText(MainActivity.this, mytext.toString(), Toast.LENGTH_LONG).show();
                } else {

                 //   Toast.makeText(MainActivity.this, "query error: " + e, Toast.LENGTH_LONG).show();
                    Log.d("Don't Say It", "Error: " + e.getMessage());
                }

            }
        });




// I Need Help Here.
//return 7143;

    }

    private void hasValueDoRest(String val) {
        maxFieldNo = new Long(val).longValue();
    }
//test

    @Override
    public void onClick(View v) {


        Count.cancel();

        // Create a progressdialog
        mProgressDialog = new ProgressDialog(MainActivity.this);
        // Set progressdialog title
        mProgressDialog.setTitle("Don't Say It!");
        // Set progressdialog message
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setIndeterminate(false);
        // Show progressdialog
        mProgressDialog.show();



                for (int i = 0; i < 5; i++) {
          int number = getRandomNumberInRange(1, (int)maxFieldNo);
            String value = String.valueOf(number);

// list contains element 10
            boolean retval = al.contains(value);

            if (retval == true) {
                i--;
            }
            else {
                al.add(value);
            }


           Log.v("randnumber", value);
        }


        // Locate the class table named "WordList" in Parse.com
        ParseQuery<ParseObject> query = new ParseQuery("WordList");
        // Find objects where the array in arrayKey contains all of the elements 2, 3, and 4.
        query.whereContainedIn("Idx", al);

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> ob, ParseException e) {
               if (e == null) {
                                     // Locate the listview in listview_main.xml
                   listview = (ListView) findViewById(R.id.listview);
                   // Pass the results into an ArrayAdapter
                   adapter = new ArrayAdapter<String>(MainActivity.this,
                           R.layout.listview_item);
                   // Retrieve object "name" from Parse.com database

                   for (ParseObject phrase : ob) {
                       adapter.add((String) phrase.get("Phrase"));

                   }
                   // Binds the Adapter to the ListView
                   listview.setAdapter(adapter);
                   al.clear();
                   // Close the progressdialog
                   mProgressDialog.dismiss();

                                     Count.start();
               } else {
                   al.clear();
                   mProgressDialog.dismiss();
                   Count.cancel();
                   Log.d("Don't Say It", "Error: " + e.getMessage());
               }
           }
       });


    }

    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }



}