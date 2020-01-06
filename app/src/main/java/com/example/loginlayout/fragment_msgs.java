package com.example.loginlayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class fragment_msgs extends AppCompatActivity {

    private CircleImageView fotoPerfil;
    private TextView nombre;
    private RecyclerView rvMensaje;
    private EditText txtMensaje;
    private Button btnEnviar;
    private ImageButton btnEnviarFoto;

    private AdapterMensajes adapter;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private static final int PHOTO_SEND = 1;
    private static final int PHOTO_PERFIL = 2;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private View view;
    private String fotoPerfilCadena;
    private String nombre_usuario;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);

        nombre_usuario = UsuariSingleton.getInstance().getNom_usuari();

        fotoPerfil = findViewById(R.id.fotoPerfil);
        nombre = findViewById(R.id.nombre);
        rvMensaje = findViewById(R.id.rvMensajes);
        txtMensaje = findViewById(R.id.txtMensaje);
        btnEnviar = findViewById(R.id.btnEnviar);
        btnEnviarFoto = findViewById(R.id.btnEnviarFoto);
        fotoPerfilCadena = "";


        //Picasso.get().load(UsuariSingleton.getInstance().getFotoPerfil()).into(fotoPerfil);
        nombre.setText(getIntent().getStringExtra("nombre_grupo"));

        String nombre_chat = getIntent().getStringExtra("id_chat");
        String tema = getIntent().getStringExtra("deporte");
        switch (tema){
            case "Futbol":
                fotoPerfil.setImageResource(R.drawable.futbol);
                break;
            case "Baloncesto":
                fotoPerfil.setImageResource(R.drawable.baloncesto);
                break;
            case "Tenis":
                fotoPerfil.setImageResource(R.drawable.tennis);
                break;
            case "PingPong":
                fotoPerfil.setImageResource(R.drawable.pingpong);
                break;
            case "Hockey":
                fotoPerfil.setImageResource(R.drawable.hockey);
                break;
            case "Golf":
                fotoPerfil.setImageResource(R.drawable.golf);
                break;
            case "Rugby":
                fotoPerfil.setImageResource(R.drawable.rugby);
                break;
            case "Running":
                fotoPerfil.setImageResource(R.drawable.running);
                break;
            case "Ciclismo":
                fotoPerfil.setImageResource(R.drawable.ciclismo);
                break;
            default:
                fotoPerfil.setImageResource(R.drawable.futbol);
                break;
        }

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("chat").child(nombre_chat);//Sala de chat
        storage = FirebaseStorage.getInstance();

        adapter = new AdapterMensajes(this);
        LinearLayoutManager l = new LinearLayoutManager(this);
        rvMensaje.setLayoutManager(l);
        rvMensaje.setAdapter(adapter);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mensajeaenviar = txtMensaje.getText().toString();
                if (!mensajeaenviar.isEmpty()) {
                    Mensaje mensaje = new Mensaje();
                    mensaje.setMensaje(mensajeaenviar);
                    mensaje.setContieneFoto(false);
                    mensaje.setKeyEmisor(UsuariSingleton.getInstance().getId());
                    //mensaje.setKeyEmisor(UsuarioDAO.getInstance().getKeyUsuario());
                    databaseReference.push().setValue(mensaje);
                    txtMensaje.setText("");
                }
            }
        });

        btnEnviarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/jpeg");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(i, "Selecciona una foto"), PHOTO_SEND);
            }
        });

        fotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/jpeg");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(i, "Selecciona una foto"), PHOTO_PERFIL);
            }
        });


        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.hasChildren()) {
                    Mensaje mensaje = dataSnapshot.getValue(Mensaje.class);
                    final LMensaje lMensaje = new LMensaje(mensaje, dataSnapshot.getKey());
                    DatabaseReference aux = FirebaseDatabase.getInstance().getReference("Usuarios");
                    aux.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot2, @Nullable String s) {
                            if (dataSnapshot2.getKey().equals(lMensaje.getMensaje().getKeyEmisor())) {
                                Usuari usuari = dataSnapshot2.getValue(Usuari.class);
                                LUsuari lUsuari = new LUsuari(usuari, dataSnapshot2.getKey());
                                lMensaje.setlUsuari(lUsuari);
                                adapter.addMensaje(lMensaje);
                                if (lMensaje.getlUsuari() == null) System.out.println("ME CAGO EN MIS PUTOS MUERTOS");
                                else System.out.println("HA IDO BIEN");
                            }
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    //if (lMensaje.getlUsuari() == null) System.out.println("ME CAGO EN MIS PUTOS MUERTOS");
                    //adapter.addMensaje(lMensaje);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setScrollbar() {
        rvMensaje.scrollToPosition(adapter.getItemCount()-1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_SEND && resultCode == RESULT_OK) {
            System.out.println("ESTOY ENVIANDO UNA FOTO");
            Uri u = data.getData();
            storageReference = storage.getReference("fotos_chat"); //imagenes del chat
            final StorageReference fotoReferencia = storageReference.child(u.getLastPathSegment());
            UploadTask uploadTask = fotoReferencia.putFile(u);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return fotoReferencia.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        if (downloadUri != null) {
                            String photoStringLink = downloadUri.toString();
                            Mensaje mensaje = new Mensaje();
                            mensaje.setMensaje("te ha enviado una foto");
                            mensaje.setUrlFoto(photoStringLink);
                            mensaje.setContieneFoto(true);
                            mensaje.setKeyEmisor(UsuariSingleton.getInstance().getId());
                            databaseReference.push().setValue(mensaje);
                        }
                    }
                }
            });
        }
        /*else if (requestCode == PHOTO_PERFIL && resultCode == RESULT_OK) {
            Uri u = data.getData();
            storageReference = storage.getReference("fotos_perfiles"); //imagenes del chat
            final StorageReference fotoReferencia = storageReference.child(u.getLastPathSegment());
            UploadTask uploadTask = fotoReferencia.putFile(u);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return fotoReferencia.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        if (downloadUri != null) {
                            String photoStringLink = downloadUri.toString(); //YOU WILL GET THE DOWNLOAD URL HERE !!!!
                            fotoPerfilCadena = photoStringLink;
                            databaseReference.push().setValue(new MensajeEnviar("kevin ha actualizado su foto de perfil", nombre_usuario, fotoPerfilCadena, "2", photoStringLink, ServerValue.TIMESTAMP));
                            Glide.with(view.getContext()).load(photoStringLink).into(fotoPerfil);
                        }
                    }
                }
            });
        }*/
    }
}