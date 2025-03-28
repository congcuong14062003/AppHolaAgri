package com.example.appholaagri.view;
import com.example.appholaagri.R;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Test extends AppCompatActivity {
    private String lastSelectedItem = ""; // Lưu lại item đã chọn trước đó

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Button button = findViewById(R.id.button);

        button.setOnClickListener(v -> {
            // Khởi tạo PopupMenu
            PopupMenu popupMenu = new PopupMenu(Test.this, button);
            popupMenu.getMenuInflater().inflate(R.menu.menu_items, popupMenu.getMenu());

            // Lắng nghe sự kiện chọn item trong PopupMenu
            popupMenu.setOnMenuItemClickListener(item -> {
                String selectedItem = item.getTitle().toString();

                // Kiểm tra nếu item đã chọn trùng với item cũ
                if (selectedItem.equals(lastSelectedItem)) {
                    Toast.makeText(Test.this, "Bạn chọn lại: " + selectedItem, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Test.this, "Bạn chọn: " + selectedItem, Toast.LENGTH_SHORT).show();
                }

                // Cập nhật lại item đã chọn
                lastSelectedItem = selectedItem;

                return true;
            });

            // Hiển thị menu
            popupMenu.show();
        });
    }
}
