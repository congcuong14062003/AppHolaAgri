    package com.example.appholaagri.adapter;

    import android.content.Context;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageView;
    import com.example.appholaagri.R;
    import com.squareup.picasso.Picasso;

    import androidx.recyclerview.widget.RecyclerView;

    import java.util.List;

    public class ImageHorizontalAdapter extends RecyclerView.Adapter<ImageHorizontalAdapter.ImageViewHolder> {

        private final List<String> imageList;
        private final Context context;

        public ImageHorizontalAdapter(Context context, List<String> imageList) {
            this.context = context;
            this.imageList = imageList;
        }

        public static class ImageViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            public ImageViewHolder(View view) {
                super(view);
                imageView = view.findViewById(R.id.image_item);
            }
        }

        @Override
        public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_image_horizontal, parent, false);
            return new ImageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ImageViewHolder holder, int position) {
            String imagePath = imageList.get(position);

            // Log URL ra logcat
            android.util.Log.d("ImageHorizontalAdapter", "Image URL: " + imagePath);

            // Load ảnh với Picasso
            Picasso.get()
                    .load(imagePath)
                    .placeholder(R.drawable.avatar)
                    .error(R.drawable.avatar)
                    .into(holder.imageView);
        }


        @Override
        public int getItemCount() {
            return imageList.size();
        }
    }

