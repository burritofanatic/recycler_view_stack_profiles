package williamha.com.wagchallenge.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import java.util.List;
import java.util.Locale;

import williamha.com.wagchallenge.R;
import williamha.com.wagchallenge.model.Badges;
import williamha.com.wagchallenge.model.StackProfile;

public class AvatarAdapter extends RecyclerView.Adapter<AvatarAdapter.AvatarViewHolder> {

    private List<StackProfile> profiles;
    private Context context;

    public AvatarAdapter(List<StackProfile> profiles) {
        this.profiles = profiles;
    }


    public void setProfiles(List<StackProfile> profiles, Context context) {
        this.profiles = profiles;
        this.context = context;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AvatarViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_holder_avatar, viewGroup, false);

        return new AvatarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AvatarViewHolder avatarViewHolder, int i) {
        StackProfile profile = profiles.get(i);
        avatarViewHolder.userNameTextView.setText(profile.getDisplayName());

        Badges badges = profile.getBadges();
        if (badges != null) {
            Integer goldCount = badges.getGold();
            Integer silverCount = badges.getSilver();
            Integer bronzeCount = badges.getBronze();

            avatarViewHolder.goldCountTextView.setText(String.format(Locale.US, "%d", goldCount));
            avatarViewHolder.silverCountTextView.setText(String.format(Locale.US, "%d", silverCount));
            avatarViewHolder.bronzeCountTextView.setText(String.format(Locale.US, "%d", bronzeCount));
        }

        Picasso picasso = Picasso.get();
        picasso.load(profile.getGravatar())
                .placeholder(getCircularProgress(avatarViewHolder.profileImageView))
                .error(R.drawable.person_placeholder)
                .into(avatarViewHolder.profileImageView);

        // To see whether the image was loaded from memory (green), disk (blue),
        // or from the result of a network (red) fetch, uncomment the following line.
        // picasso.setIndicatorsEnabled(true);
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    private CircularProgressDrawable getCircularProgress(ImageView imageView) {

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(this.context);
        circularProgressDrawable.setStrokeWidth(20f);
        circularProgressDrawable.setCenterRadius(imageView.getMeasuredHeight() / 2);
        circularProgressDrawable.setColorSchemeColors(R.color.colorPrimary);
        circularProgressDrawable.start();

        return circularProgressDrawable;
    }

    class AvatarViewHolder extends RecyclerView.ViewHolder {

        private TextView userNameTextView;
        private TextView goldCountTextView;
        private TextView silverCountTextView;
        private TextView bronzeCountTextView;
        private ImageView profileImageView;


        public AvatarViewHolder(@NonNull View itemView) {
            super(itemView);

            this.userNameTextView = itemView.findViewById(R.id.user_name);
            this.goldCountTextView = itemView.findViewById(R.id.gold_count);
            this.silverCountTextView = itemView.findViewById(R.id.silver_count);
            this.bronzeCountTextView = itemView.findViewById(R.id.bronze_count);
            this.profileImageView = itemView.findViewById(R.id.imageView);
        }
    }
}
