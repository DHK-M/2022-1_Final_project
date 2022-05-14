package com.lastproject.used_item_market;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class RecyclerChatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<ChattingRoomInfo> chattingRoomInfoList = new ArrayList<ChattingRoomInfo>();
    private RecyclerChatListAdapter.OnItemClickListener rcListener = null;  //클릭 리스너 변수
    public String mykey;

    //이미지 DB
    private FirebaseStorage storage;            //이미지 저장소
    private StorageReference storageRef;        //정확한 위치에 파일 저장

    //생성자
    public RecyclerChatListAdapter(){}

    public RecyclerChatListAdapter(ArrayList<ChattingRoomInfo> chattingRoomInfoArrayList, String mykey){
        this.chattingRoomInfoList = chattingRoomInfoArrayList;
        this.mykey = mykey;
    }

    public interface OnItemClickListener            //아이템이 눌린
    {
        void onItemClick(View v, int pos);
    }

    public void setOnItemClickListener(RecyclerChatListAdapter.OnItemClickListener rclistener)
    {
        this.rcListener = rclistener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.chatting_home_itme, parent, false);
        RecyclerChatListAdapter.ViewHolderChatRoom viewHolderChatRoom = new RecyclerChatListAdapter.ViewHolderChatRoom(view);
        viewHolderChatRoom.setIsRecyclable(false);       //글라이드 상황에 따라 넣기
        return viewHolderChatRoom;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((RecyclerChatListAdapter.ViewHolderChatRoom)holder).onBind(chattingRoomInfoList.get(position));
    }

    @Override
    public int getItemCount() {
        return chattingRoomInfoList.size();
    }

    @Override
    public int getItemViewType(int position) {      //리사이클뷰에 재활용을 막는다.
        return position;
    }

    //뷰 홀더더
   class ViewHolderChatRoom extends RecyclerView.ViewHolder{

        View itemview;
        ImageView product_img;
        ImageView profile;
        TextView chattingRoom_counts;
        TextView title;
        TextView time;
        TextView last_text;
        LinearLayout back_red_circle;       //알림 뒤에 빨간색
        TextView alramnum;



        public ViewHolderChatRoom(@NonNull View itemView) {
            super(itemView);
            this.itemview = itemView;

            itemView.setOnClickListener(new View.OnClickListener() {            //클릭은 여기다가 한다.
                @Override
                public void onClick(View v) {

                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION)            //눌렸을 경우
                    {
                        // click event
                        rcListener.onItemClick(v, pos);
                    }

                }
            });

            product_img = (ImageView)itemView.findViewById(R.id.chatting_home_product_img);
            profile = (ImageView)itemView.findViewById(R.id.chattimg_home_user_profile);
            title = (TextView)itemView.findViewById(R.id.chattimg_home_title);
            chattingRoom_counts = (TextView)itemView.findViewById(R.id.chatting_home_peoples);
            time = (TextView)itemView.findViewById(R.id.chatting_home_time);
            last_text = (TextView)itemView.findViewById(R.id.chattimg_home_lasttext);
            back_red_circle = (LinearLayout)itemView.findViewById(R.id.chatting_home_alarmimg);
            alramnum = (TextView)itemView.findViewById(R.id.chatting_home_alarmnum);

        }

        public void onBind(ChattingRoomInfo chattingRoomInfo) {

            //상품 이미지 처리
            if (chattingRoomInfo.product_imgkey != null) {       //상품에 사진이 있는 경우

                StorageReference getRef = storageRef.child("images").child(chattingRoomInfo.product_imgkey);
                getRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {     //사진을 가져온 경우
                    @Override
                    public void onSuccess(Uri uri) {

                        try {

                            Glide.with(itemview)
                                    .load(uri)
                                    .override(150, 150)
                                    .into(product_img);

                        } catch (Exception e) {
                            System.out.println("view holder binding 실패");
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("상품 사진 통신 실패");
                    }
                });
            }//상품 이미지 처리 끝

            //프로필 이미지 처리
            StorageReference sellerimgRef = storageRef.child("images")
                    .child(chattingRoomInfo.customer_images.get(0));
            sellerimgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    try {

                        Glide.with(itemview)
                                .load(uri)
                                .override(150, 150)
                                .into(profile);

                    } catch (Exception e) {
                        System.out.println("view holder binding 실패");
                    }

                }
            }); //프로필 이미지 처리 끝

            //타이틀
            title.setText(chattingRoomInfo.title);
            //채팅방 인원수
            int count = 0;
            for(int i = 0; i < chattingRoomInfo.customerList.size(); i++){



            }


        }//onBind 끝
    }
}

/*
chattimg_home_product_img - 상품 이미지
chattimg_home_user_profile - 유저 프로필
chattimg_home_title - 타이틀
chatting_home_peoples - 채팅방 안에 사람수
chatting_home_time - 시간
chattimg_home_lasttext - 마지막 채팅 기록
chatting_home_alarmimg - 빨간 동그라미 이미지 (java 에서 background 변겅 가능 on off 구현 할수 있겠다)
chatting_home_alarmnum - 빨간 동그라미(알림) 안에 숫자
* */
