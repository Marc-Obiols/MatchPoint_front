package com.example.loginlayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.widget.Toast.LENGTH_SHORT;

public class activity_profile_completo_modificable extends AppCompatActivity implements fragment_delete_profile_dialog.DialogListener{

    private EditText nombreApellidos;
    private EditText username;
    private CircleImageView imageProfile;
    private EditText opcionGenero;
    private EditText fechaNacimiento;
    private Button buttonModificar;
    private Button eraseButton;

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private View view;

    private RequestQueue queue; //cola de las solicitudes
    private String id;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_modificable);

        nombreApellidos = findViewById(R.id.nombreApellidos);

        imageProfile = findViewById(R.id.imageProfile);

        //username = findViewById(R.id.Username);
        opcionGenero = findViewById(R.id.opcionGenero);
        fechaNacimiento = findViewById(R.id.fechaNacimiento);
        buttonModificar = findViewById(R.id.buttonModificar);
        eraseButton = findViewById(R.id.buttonerase);

        Intent i = getIntent();
        id = i.getStringExtra("id");

        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Usuarios").child(id);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String u = (String) dataSnapshot.getValue();
                if (u.charAt(0) == 'h' && u.charAt(1) == 't' && u.charAt(2) == 't' && u.charAt(3) == 'p')
                    Picasso.get().load(u).into(imageProfile);
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

        eraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/jpeg");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(i, "Selecciona una foto"), 1);
            }
        });

        url = "http://10.4.41.144:3000/profile/modify/"+ id;

        queue = Volley.newRequestQueue(this); //inicializar el requestqueue
        Request(id);
    }

    public void modificar(View v){

            String nombreApellidosModif;
            String opcionGeneroModif;
            String fechaNacimientoModif;
            //String usernameModif;

            nombreApellidosModif = nombreApellidos.getText().toString();
            opcionGeneroModif = opcionGenero.getText().toString();
            fechaNacimientoModif = fechaNacimiento.getText().toString();
            //usernameModif = username.getText().toString();

            JSONObject req = new JSONObject();
            try {
                req.put("real_name",nombreApellidosModif);
                req.put("sex",opcionGeneroModif);
                req.put("birth_date",fechaNacimientoModif);
                //req.put("username",usernameModif);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, req, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    String responses = response.toString();
                    //Toast.makeText(activity_profile_completo_modificable.this, responses, LENGTH_SHORT).show();
                    System.out.println(responses);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(activity_profile_completo_modificable.this, error.toString(), LENGTH_SHORT).show();
                    System.out.println(error.toString());
                }
            });
            queue.add(request);
            Toast.makeText(activity_profile_completo_modificable.this,"Se ha modificado tu perfil", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, activity_main.class);
            startActivity(i);
            /*Intent i = new Intent(getBaseContext(),fragment_profile.class);
            startActivity(i);*/
    }

   public void Request(String id) {
       String url = "http://10.4.41.144:3000/profile/" + id;

       JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
           //@Override
           public void onResponse(JSONObject response) {
               try {
                   nombreApellidos.setText(response.getString("real_name"));
                   //username.setText(response.getString("username"));
                   opcionGenero.setText(response.getString("sex"));
                   fechaNacimiento.setText(response.getString("birth_date").substring(0,10));
                   Integer result = response.getInt("phone_number");
                   NumberFormat nm =  NumberFormat.getNumberInstance();
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               Toast.makeText(activity_profile_completo_modificable.this, error.toString(), LENGTH_SHORT).show();
               System.out.println(error.toString());
           }
       });
       queue.add(request);
   }

   public void openDialog(){
        fragment_delete_profile_dialog deleteAccount = new fragment_delete_profile_dialog();
        deleteAccount.show(getSupportFragmentManager(), "DeleteAccount");
   }

   @Override
   public void DeleteAccount(String mail, String passwd){
       String url = "http://10.4.41.144:3000/user/delete";
       JSONObject req = new JSONObject();
       try {
           req.put("email",mail);
           req.put("password",passwd);
       } catch (JSONException e) {
           e.printStackTrace();
       }
       final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, req, new Response.Listener<JSONObject>() {
           @Override
           public void onResponse(JSONObject response) {
               String responses = response.toString();
               Toast.makeText(activity_profile_completo_modificable.this, responses, LENGTH_SHORT).show();
               System.out.println(responses);
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               Toast.makeText(activity_profile_completo_modificable.this, error.toString(), LENGTH_SHORT).show();
               System.out.println(error.toString());
           }
       });
       queue.add(request);
       Intent i = new Intent(this, activity_main.class);
       UsuariSingleton.getInstance().user_LogOut();
       startActivity(i);
   }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri u = data.getData();
            storageReference = storage.getReference("Usuarios"); //imagenes del chat
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
                            Usuari usuari = new Usuari();
                            usuari.setMail(UsuariSingleton.getInstance().getMail());
                            usuari.setFotoPerfil(photoStringLink);
                            usuari.setNom_usuari(UsuariSingleton.getInstance().getNom_usuari());
                            databaseReference.setValue(usuari);
                            Picasso.get().load(photoStringLink).into(imageProfile);
                        }
                    }
                }
            });
        }
    }
}