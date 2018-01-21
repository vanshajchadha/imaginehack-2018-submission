package nfctutorials.tutorial04;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;


public class MainActivity extends ActionBarActivity {

    NfcAdapter nfcAdapter;
    ToggleButton tglReadWrite;
    EditText txtTagContent;
    EditText ed0;
    EditText ed1;
    EditText ed2;
    EditText ed3;
    EditText ed4;
    EditText ed5;
    EditText ed6;
    EditText ed7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(nfctutorials.tutorial04.R.layout.activity_main);
        txtTagContent=(EditText) findViewById(R.id.xyz);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        tglReadWrite = (ToggleButton)findViewById(R.id.tglReadWrite);
        ed0=(EditText) findViewById(R.id.edit0);
        ed1=(EditText) findViewById(R.id.edit1);
        ed2=(EditText) findViewById(R.id.edit2);
        ed3=(EditText) findViewById(R.id.edit3);
        ed4=(EditText) findViewById(R.id.edit4);
        ed5=(EditText) findViewById(R.id.edit5);
        ed6=(EditText) findViewById(R.id.edit6);
        ed7=(EditText) findViewById(R.id.edit7);

    }

    @Override
    protected void onResume() {
        super.onResume();

        enableForegroundDispatchSystem();
    }

    @Override
    protected void onPause() {
        super.onPause();

        disableForegroundDispatchSystem();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
            Toast.makeText(this, "NfcIntent!", Toast.LENGTH_SHORT).show();

            if(tglReadWrite.isChecked())
            {
                //Toast.makeText(this,"isChecked",Toast.LENGTH_LONG).show();
                Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

                if(parcelables != null && parcelables.length > 0)
                {
                    readTextFromMessage((NdefMessage) parcelables[0]);
                }else{
                    Toast.makeText(this, "No NDEF messages found!", Toast.LENGTH_SHORT).show();
                }

            }else{
               // Toast.makeText(this,"NotisChecked",Toast.LENGTH_LONG).show();
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                NdefMessage ndefMessage = createNdefMessage("{"+
                        "\"Name\""+ ":"+ "\""+ ed0.getText()+"\","+
                        "\"Company\""+ ":"+ "\""+ ed1.getText()+ "\","+
                        "\"Title\""+":"+ "\""+ ed2.getText()+ "\","+
                        "\"Email\""+ ":"+"\""+ ed3.getText()+ "\","+
                        "\"Address\""+ ":"+ "\""+ ed4.getText()+ "\","+
                        "\"Phone_Number\""+ ":"+ "\""+ ed5.getText()+ "\","+
                        "\"Website\""+ ":"+ "\""+ ed6.getText()+ "\","+
                        "\"LinkedIn\""+ ":"+ "\""+ ed7.getText()+ "\""+
                        "}"+"");
                writeNdefMessage(tag, ndefMessage);
            }

        }
    }

    private void readTextFromMessage(NdefMessage ndefMessage) {

        NdefRecord[] ndefRecords = ndefMessage.getRecords();

        if(ndefRecords != null && ndefRecords.length>0){

            NdefRecord ndefRecord = ndefRecords[0];

            String tagContent = getTextFromNdefRecord(ndefRecord);

            tagContent=tagContent.replaceAll(",",":");
            tagContent=tagContent.replaceAll("\"","");
            String d[]=tagContent.split(":");

            d[d.length-1]=d[d.length-1].substring(0,d[d.length-1].length()-1);

            for(int i=1;i<d.length;i+=2) {
                switch (i) {
                    case 1:
                        ed0.setText(d[i]);
                        break;
                    case 3:
                        ed1.setText(d[i]);
                        break;
                    case 5:
                        ed2.setText(d[i]);
                        break;
                    case 7:
                        ed3.setText(d[i]);
                        break;
                    case 9:
                        ed4.setText(d[i]);
                        break;
                    case 11:
                        ed5.setText(d[i]);
                        break;
                    case 13:
                        ed6.setText(d[i]);
                        break;
                    case 15:
                        ed7.setText(d[i]);
                        break;
                }
            }

        }else
        {
            Toast.makeText(this, "No NDEF records found!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(nfctutorials.tutorial04.R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == nfctutorials.tutorial04.R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void enableForegroundDispatchSystem() {

        Intent intent = new Intent(this, MainActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        IntentFilter[] intentFilters = new IntentFilter[]{};

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
    }

    private void disableForegroundDispatchSystem() {
        nfcAdapter.disableForegroundDispatch(this);
    }

    private void formatTag(Tag tag, NdefMessage ndefMessage) {
        try {

            NdefFormatable ndefFormatable = NdefFormatable.get(tag);

            if (ndefFormatable == null) {
                Toast.makeText(this, "Tag is not ndef formatable!", Toast.LENGTH_SHORT).show();
                return;
            }


            ndefFormatable.connect();
            ndefFormatable.format(ndefMessage);
            ndefFormatable.close();

            Toast.makeText(this, "Tag written!", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.e("formatTag", e.getMessage());
        }

    }

    private void writeNdefMessage(Tag tag, NdefMessage ndefMessage) {

        try {

            if (tag == null) {
                Toast.makeText(this, "Tag object cannot be null", Toast.LENGTH_SHORT).show();
                return;
            }

            Ndef ndef = Ndef.get(tag);

            if (ndef == null) {
                // format tag with the ndef format and writes the message.
                formatTag(tag, ndefMessage);
            } else {
                ndef.connect();

                if (!ndef.isWritable()) {
                    Toast.makeText(this, "Tag is not writable!", Toast.LENGTH_SHORT).show();

                    ndef.close();
                    return;
                }

                ndef.writeNdefMessage(ndefMessage);
                ndef.close();

                Toast.makeText(this, "Tag written!", Toast.LENGTH_SHORT).show();

            }

        } catch (Exception e) {
            Log.e("writeNdefMessage", e.getMessage());
        }

    }

    private NdefRecord createTextRecord(String content) {
        try {
            byte[] language;
            language = Locale.getDefault().getLanguage().getBytes("UTF-8");

            final byte[] text = content.getBytes("UTF-8");
            final int languageSize = language.length;
            final int textLength = text.length;
            final ByteArrayOutputStream payload = new ByteArrayOutputStream(1 + languageSize + textLength);

            payload.write((byte) (languageSize & 0x1F));
            payload.write(language, 0, languageSize);
            payload.write(text, 0, textLength);

            return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload.toByteArray());

        } catch (UnsupportedEncodingException e) {
            Log.e("createTextRecord", e.getMessage());
        }
        return null;
    }


    private NdefMessage createNdefMessage(String content) {

        NdefRecord ndefRecord = createTextRecord(content);

        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{ndefRecord});

        return ndefMessage;
    }


    public void tglReadWriteOnClick(View view){
        txtTagContent.setText("");
    }

    public String getTextFromNdefRecord(NdefRecord ndefRecord)
    {
        String tagContent = null;
        try {
            byte[] payload = ndefRecord.getPayload();
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            int languageSize = payload[0] & 0063;
            tagContent = new String(payload, languageSize + 1,
                    payload.length - languageSize - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {
            Log.e("getTextFromNdefRecord", e.getMessage(), e);
        }
        return tagContent;
    }

}
