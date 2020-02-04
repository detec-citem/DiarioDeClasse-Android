package br.gov.sp.educacao.sed.mobile.Carteirinha;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import br.gov.sp.educacao.sed.mobile.util.Banco;
import br.gov.sp.educacao.sed.mobile.util.CriarAcessoBanco;

public class CarteirinhaActivity extends AppCompatActivity implements CarteirinhaViewMvc.Listener {
    //Variáveis
    private int telaPequena;
    private DadosCarteirinha dadosCarteirinha;
    private CarteirinhaViewMvcImpl carteirinhaViewMvcImpl;

    //Lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        if (savedInstanceState == null) {
            Banco banco = CriarAcessoBanco.gerarBanco(this);
            CarteirinhaDBgetters carteirinhaDBgetters = new CarteirinhaDBgetters(banco);
            String codigoCargo = getIntent().getStringExtra("codigoCargo");
            dadosCarteirinha = carteirinhaDBgetters.getCarteirinha(codigoCargo);
            int height = Resources.getSystem().getDisplayMetrics().heightPixels;
            WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            Point outPoint = new Point();
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
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
        }
        else {
            telaPequena = savedInstanceState.getInt("telaPequena");
            dadosCarteirinha = savedInstanceState.getParcelable("dadosCarteirinha");
        }
        carteirinhaViewMvcImpl = new CarteirinhaViewMvcImpl(LayoutInflater.from(this), telaPequena, null, getResources(), dadosCarteirinha);
        setContentView(carteirinhaViewMvcImpl.getRootView());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("telaPequena", telaPequena);
        outState.putParcelable("dadosCarteirinha", dadosCarteirinha);
        super.onSaveInstanceState(outState);
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

    @Override
    public void onBackPressed() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }
        else {
            finish();
        }
    }
}