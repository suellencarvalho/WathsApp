package com.example.wathsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.wathsapp.R;
import com.example.wathsapp.config.ConfiguracaoFirebase;
import com.example.wathsapp.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {

    private TextInputEditText campoNome, campoEmail, campoSenha;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        campoNome = findViewById(R.id.editNome);
        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);
    }

    public void cadastrarUsuario(Usuario usuario){
        autenticacao = ConfiguracaoFirebase.getFirebaseautenticacao();
        autenticacao.createUserWithEmailAndPassword(
                 usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    Toast.makeText(CadastroActivity.this, "Sucesso ao cadastrar usuário!", Toast.LENGTH_SHORT).show();
                    finish();

                }else{
                    String excecao = "";
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        excecao = "Digite uma senha mais forte!";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "Por favor, digite um e-mail válido";
                    }catch (FirebaseAuthUserCollisionException e){
                        excecao = "Esta conta já foi cadastrada";
                    }catch (Exception e){
                        excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroActivity.this, excecao, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    public void validarCadastroUsuario(View view){

        //Recuperar textos dos campos
        String textoNome = campoNome.getText().toString();
        String textEmail = campoEmail.getText().toString();
        String textSenha = campoSenha.getText().toString();

        if (!textoNome.isEmpty()){ //verifica o nome

            if (!textEmail.isEmpty()){//verifica email

                if (!textSenha.isEmpty()){
                        Usuario usuario = new Usuario();
                        usuario.setNome(textoNome);
                        usuario.setEmail(textEmail);
                        usuario.setSenha(textSenha);
                        cadastrarUsuario(usuario);
                }else{
                    Toast.makeText(CadastroActivity.this, "Preencha a senha!.", Toast.LENGTH_SHORT).show();

                }
            }else{
                Toast.makeText(CadastroActivity.this, "Preencha o email!.", Toast.LENGTH_SHORT).show();

            }

        }else {
            Toast.makeText(CadastroActivity.this, "Preencha o nome!.", Toast.LENGTH_SHORT).show();
        }

    }
}
