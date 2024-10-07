package com.example.splash_screen;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Added_issues_adapter extends
        RecyclerView.Adapter<Added_issues_adapter.ExampleViewHolder> {

    private Context cartcontext;
    public ArrayList<AddedIssuesDataModel> notificationArraylists;
    private Added_issues_adapter.OnItemClickListener onItemClickListener;
    SimpleDateFormat sdfo;
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public interface OnItemClickListener {
        void onclick(int position);
    }

    public void setOnItemClickListener(Added_issues_adapter.OnItemClickListener listenerdas) {
        onItemClickListener = listenerdas;
    }

    public Added_issues_adapter(Context context, ArrayList<AddedIssuesDataModel> addresslist) {
        cartcontext = context;
        notificationArraylists = addresslist;
    }

    @Override
    public Added_issues_adapter.ExampleViewHolder onCreateViewHolder(ViewGroup parent,
                                                                     int viewType) {
        View vw = LayoutInflater.from(cartcontext).inflate(R.layout.added_issues_list,
                parent, false);
        return new Added_issues_adapter.ExampleViewHolder(vw);

    }

    @Override
    public void onBindViewHolder(final Added_issues_adapter.ExampleViewHolder holder, int position) {
        AddedIssuesDataModel currentItem = notificationArraylists.get(position);
        String type = currentItem.getFetched_location();
        String content = currentItem.getLandmark();
        String created_at = currentItem.getDate_time();

        holder.Title.setText(type);
        holder.descritpion.setText(content);
        holder.image_view.setImageBitmap(currentItem.getImage());

        if (created_at.contains("&&")) {
            created_at = created_at.replace("&&", " ");
        }
        sdfo = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        try {
            Date d1 = sdfo.parse(created_at);
            holder.hours.setText(getTimeAgo(d1));

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Date currentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    public static String getTimeAgo(Date date) {
        Log.d("OMG","date = " + date);

        long time = date.getTime();
        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = currentDate().getTime();
        if (time > now || time <= 0) {
            return "in the future";
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "moments ago";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 60 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 2 * HOUR_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }

    @Override
    public int getItemCount() {
        return notificationArraylists.size();
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder {

        TextView Title, descritpion, hours,editing;
        ImageView image_view;

        public ExampleViewHolder(View itemView) {
            super(itemView);

            image_view = itemView.findViewById(R.id.image_view);
            Title = itemView.findViewById(R.id.Title);
            descritpion = itemView.findViewById(R.id.descritpion);
            hours = itemView.findViewById(R.id.hours);
            editing = itemView.findViewById(R.id.editing);

            if (Main_prefence.getInstance().user_login_type == "Service Worker Login") {
                editing.setVisibility(View.VISIBLE);
            }else {
                editing.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onItemClickListener.onclick(position);
                        }
                    }
                }
            });
        }
    }
}

/*
 itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (successfullstorylisteneras != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            successfullstorylisteneras.onItemClick(position);
                        }
                    }
                }
            });
 */

//        Picasso.get().load(full_image_url).into(holder.image_view);

//        if (created_at.contains("&&")) {
//            created_at = created_at.replace("&&", " ");
//        }
//        String Day = created_at;
//
//        sdfo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//        try {
//            Date d1 = sdfo.parse(Day);
//
//            Date c = Calendar.getInstance().getTime();
//            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//            String CurrentDate = df.format(c);
//            sdfo = new SimpleDateFormat("yyyy-MM-dd");
//            Date Current = sdfo.parse(CurrentDate);
//            DecimalFormat crunchifyFormatter = new DecimalFormat("###,###");
//
//            long diff = Current.getTime() - d1.getTime();
//
//            int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
//            Log.e("OMG", "difference between days: " + diffDays);
//
//            int diffhours = (int) (diff / (60 * 60 * 1000));
//            Log.e("OMG", "difference between hours: " + crunchifyFormatter.format(diffhours));
//
//            int diffmin = (int) (diff / (60 * 1000));
//            Log.e("OMG", "difference between minutues: " + crunchifyFormatter.format(diffmin));
//
//            if (2 < crunchifyFormatter.format(diffmin).length()) {
//
//                if (2 < crunchifyFormatter.format(diffhours).length()) {
//
//                } else {
//                    holder.hours.setText(crunchifyFormatter.format(diffhours) + " hours ago");
//                }
//            } else {
//                holder.hours.setText(crunchifyFormatter.format(diffmin) + " mins ago");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

