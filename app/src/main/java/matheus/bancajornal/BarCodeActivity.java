package matheus.bancajornal;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class BarCodeActivity extends Activity{
    /** Called when the activity is first created. */

    //static final String SCAN = "com.google.zxing.client.android.SCAN";
    static final String SCAN = "com.google.zxing.client.android.SCAN";
    Button scanner2;
    String tipo, tipo_usuario = "";
    List<Produtos> lista = new ArrayList<Produtos>();
    List<Integer> qtde = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code);

        scanner2 = (Button)findViewById(R.id.scanner2);
        scanner2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanBar(v);
            }
        });
        Intent intent = getIntent();
        if (intent.hasExtra("tipo")) {
            tipo = intent.getStringExtra("tipo");
        }
        if (intent.hasExtra("lista_prod")){
            lista = (List<Produtos>)intent.getSerializableExtra("lista_prod");
        }
        if (intent.hasExtra("lista_qtde")){
            qtde = (List<Integer>)intent.getSerializableExtra("lista_qtde");
        }
        if (intent.hasExtra("tipo_usuario")){
            tipo_usuario = intent.getStringExtra("tipo_usuario");
        }
    }

    public void ScanBar(View v){
        try{
            Intent in = new Intent (SCAN);
            in.putExtra("SCAN_MODE", "PRODUCT_MODE");
            startActivityForResult(in, 0);
        }catch (ActivityNotFoundException e) {
            // TODO: handle exception
            showDialog(BarCodeActivity.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }

    public void ScanQR(View v){

        try{
            Intent in = new Intent (SCAN);
            in.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(in, 0);
        }catch (ActivityNotFoundException e) {
            // TODO: handle exception
            showDialog(BarCodeActivity.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }

    private Dialog showDialog(final Activity act, CharSequence title,
                              CharSequence message, CharSequence Yes, CharSequence No) {
        // TODO Auto-generated method stub

        AlertDialog.Builder download = new AlertDialog.Builder(act);
        download.setTitle(title);
        download.setMessage(message);
        download.setPositiveButton(Yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i){
                // TODO Auto-generated method stub
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent in = new Intent(Intent.ACTION_VIEW, uri);
                try{
                    act.startActivity(in);
                }catch(ActivityNotFoundException anfe){

                }
            }
        });
        download.setNegativeButton(No, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                // TODO Auto-generated method stub
            }
        });
        return download.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent in) {
        // TODO Auto-generated method stub
        if(requestCode ==0){
            if(resultCode == RESULT_OK){
                String contents = in.getStringExtra("SCAN_RESULT");
                String format =  in.getStringExtra("SCAN_RESULT_FORMAT") ;
                Toast toast = Toast.makeText(this, "Content:" + contents + " Format:" + format, Toast.LENGTH_LONG);
                toast.show();
                if (tipo.equals("compra")) {
                    Intent intent = new Intent(BarCodeActivity.this, CadastroProdutoActivity.class);
                    intent.putExtra("codigo", contents);
                    startActivity(intent);
                }
                if (tipo.equals("venda")) {
                    Intent intent = new Intent(BarCodeActivity.this, VendaActivity.class);
                    intent.putExtra("codigo", contents);
                    intent.putExtra("lista_prod", (Serializable)lista);
                    intent.putExtra("lista_qtde", (Serializable)qtde);
                    intent.putExtra("tipo_usuario", tipo_usuario);
                    startActivity(intent);
                }
            }
        }
    }
}