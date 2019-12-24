package com.example.loginlayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;

public class menu_chats extends Fragment {

    private RecyclerView rvChats;
    private AdapterChats adapter;
    private View view;
    private FirebaseStorage storage;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_menu_chats, container, false);

        rvChats = view.findViewById(R.id.rvChats);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("chat");//Sala de chat
        storage = FirebaseStorage.getInstance();

        adapter = new AdapterChats(view.getContext());
        LinearLayoutManager l = new LinearLayoutManager(view.getContext());
        rvChats.setLayoutManager(l);
        rvChats.setAdapter(adapter);

        /*Query query = FirebaseDatabase.getInstance().getReference().child("chat");

        FirebaseRecyclerOptions<Chat> options = new FirebaseRecyclerOptions.Builder<Chat>().setQuery(query, Chat.class).build();
        System.out.println("options:");
        System.out.println(options.getSnapshots());
        adapter = new FirebaseRecyclerAdapter<Chat, ChatHolder>(options) {
            @Override
            public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_chat, parent, false);
                System.out.println("View:");
                System.out.println(view.toString());
                return new ChatHolder(view);
            }

            @Override
            protected void onBindViewHolder(ChatHolder holder, int position, Chat model) {
                System.out.println("Name:");
                System.out.println(model.getName());
                Glide.with(menu_chats.this).load(R.drawable.soccer).into(holder.getCivFotoChat());
                holder.getTxtNombreChat().setText(model.getName());
            }
        };*/

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollbar();
            }
        });

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //filtrar
                Chat chat = dataSnapshot.getValue(Chat.class);
                adapter.addChat(chat, dataSnapshot.getKey());
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

        return view;
    }

    private void setScrollbar() {
        rvChats.scrollToPosition(adapter.getItemCount()-1);
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

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
                            //mensaje.setKeyEmisor(FirebaseAuth.getInstance().getUid());
                            mensaje.setKeyEmisor(UsuarioDAO.getInstance().getKeyUsuario());
                            databaseReference.push().setValue(mensaje);
                        }
                    }
                }
            });
        }
    }*/

    /*@Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }*/
}
