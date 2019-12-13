package br.gov.sp.educacao.sed.mobile.Carteirinha;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.CriarAcessoBanco;

public class CarteirinhaActivity
        extends AppCompatActivity
         implements CarteirinhaViewMvc.Listener {

    public Banco banco;

    private CarteirinhaViewMvcImpl carteirinhaViewMvcImpl;

    private String TAG = CarteirinhaActivity.class.getSimpleName();

    private CarteirinhaDBgetters carteirinhaDBgetters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        String codigoCargo = getIntent().getStringExtra("codigoCargo");

        inicializarAcessoDB();

        DadosCarteirinha dadosCarteirinha = carteirinhaDBgetters.getCarteirinha(codigoCargo);

        carteirinhaViewMvcImpl = new CarteirinhaViewMvcImpl(LayoutInflater.from(this), checarTamanhoTela(), null, getResources(), dadosCarteirinha);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){

            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }

        setContentView(carteirinhaViewMvcImpl.getRootView());
    }

    private int checarTamanhoTela() {

        int width  = Resources.getSystem().getDisplayMetrics().widthPixels;
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;
        int dpi    = Resources.getSystem().getDisplayMetrics().densityDpi;

        int telaPequena = 0;

        WindowManager windowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);

        final Display display = windowManager.getDefaultDisplay();

        Point outPoint = new Point();

        if(Build.VERSION.SDK_INT >= 19) {

            display.getRealSize(outPoint);
        }
        else {

            display.getSize(outPoint);
        }

        if(height >= 880 && height < 1000) {

            telaPequena = 1; //Tela maior que 4.3 e menor que 5
        }
        else if(height < 880) {

            telaPequena = 2; //Tela menor que 4.3
        }

        Log.e(TAG, "Medidas - " + "Width: " + width + " Height: " + height + " Dpi: " + dpi);

        Log.e(TAG, "Medidas - " + "Height: " + outPoint.y);

        return telaPequena;
    }

    private void inicializarAcessoDB() {

        CriarAcessoBanco criarAcessoBanco = new CriarAcessoBanco();

        banco = criarAcessoBanco.gerarBanco(getApplicationContext());

        carteirinhaDBgetters = new CarteirinhaDBgetters(banco);
    }

    @Override
    public void onBackPressed() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            finishAfterTransition();
        }
        else {

            finish();
        }
    }

    @Override
    protected void onStart() {

        super.onStart();

        carteirinhaViewMvcImpl.registerListener(this);
    }

    @Override
    protected void onStop() {

        super.onStop();

        carteirinhaViewMvcImpl.unregisterListener();
    }
}
