package com.example.appholaagri.view;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.appholaagri.R;
import com.example.appholaagri.utils.LoadingDialog;

import java.io.File;

public class FileWebViewActivity extends AppCompatActivity {
    private LoadingDialog loadingDialog;
    private int retryCount = 0;
    private static final int MAX_RETRIES = 2;
    private static final long RETRY_DELAY_MS = 1000;
    private static final long CONTENT_CHECK_DELAY_MS = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_web_view);
        WebView webView = findViewById(R.id.webview);
        Intent intent = getIntent();
        String fileUrl = intent.getStringExtra("fileUrl");
        String fileName = intent.getStringExtra("fileName");
        Log.d("FileWebViewActivity", "fileUrl: " + fileUrl + ", fileName: " + fileName);

        // Initialize LoadingDialog
        loadingDialog = new LoadingDialog(this);

        // Check if file is Excel and show options immediately
        if (isExcelFile(fileName)) {
            showExcelOptions(fileUrl, fileName);
            return;
        }

        // Configure WebView
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

        // WebViewClient with improved error handling
        webView.setWebViewClient(new WebViewClient() {
            private boolean isErrorReceived = false;

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                isErrorReceived = true;
                loadingDialog.hide();
                Log.e("WebViewError", "Error code: " + errorCode + ", Description: " + description + ", URL: " + failingUrl);

                if ((errorCode == WebViewClient.ERROR_TIMEOUT || errorCode == WebViewClient.ERROR_CONNECT) && retryCount < MAX_RETRIES) {
                    retryCount++;
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        Log.d("FileWebViewActivity", "Retrying load, attempt: " + retryCount);
                        view.loadUrl(failingUrl);
                    }, RETRY_DELAY_MS);
                } else {
                    String errorMessage = getErrorMessage(errorCode, description);
                    Toast.makeText(FileWebViewActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    showOpenOrDownloadOption(fileUrl, fileName);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!isErrorReceived) {
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        if (!isErrorReceived && view.getContentHeight() == 0) {
                            if (retryCount < MAX_RETRIES) {
                                retryCount++;
                                Log.d("FileWebViewActivity", "Content height 0, retrying: " + retryCount);
                                view.loadUrl(url);
                            } else {
                                loadingDialog.hide();
                                Toast.makeText(FileWebViewActivity.this, "File không thể hiển thị sau " + MAX_RETRIES + " lần thử.", Toast.LENGTH_LONG).show();
                                showOpenOrDownloadOption(fileUrl, fileName);
                            }
                        } else {
                            loadingDialog.hide();
                        }
                    }, CONTENT_CHECK_DELAY_MS);
                }
            }
        });

        // Check network and URL validity
        if (fileUrl != null && !fileUrl.isEmpty()) {
            if (!isNetworkAvailable()) {
                loadingDialog.hide();
                Toast.makeText(this, "Không có kết nối mạng. Vui lòng kiểm tra lại.", Toast.LENGTH_SHORT).show();
                showOpenOrDownloadOption(fileUrl, fileName);
                return;
            }
            try {
                Uri.parse(fileUrl);
                loadingDialog.show();
                String loadUrl = fileUrl.startsWith("file://") ? fileUrl : "https://docs.google.com/viewer?url=" + Uri.encode(fileUrl) + "&embedded=true";
                webView.loadUrl(loadUrl);
            } catch (Exception e) {
                loadingDialog.hide();
                Log.e("FileWebViewActivity", "Invalid URL: " + e.getMessage());
                Toast.makeText(this, "URL file không hợp lệ!", Toast.LENGTH_SHORT).show();
                showOpenOrDownloadOption(fileUrl, fileName);
            }
        } else {
            loadingDialog.hide();
            Toast.makeText(this, "Không tìm thấy URL file!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private boolean isExcelFile(String fileName) {
        if (fileName == null) return false;
        String extension = fileName.toLowerCase();
        return extension.endsWith(".xls") || extension.endsWith(".xlsx");
    }

    private void showExcelOptions(String fileUrl, String fileName) {
        new AlertDialog.Builder(this)
                .setTitle("Mở file Excel")
                .setMessage("Bạn muốn mở file bằng ứng dụng khác hoặc tải về thiết bị?")
                .setPositiveButton("Mở bằng ứng dụng", (dialog, which) -> {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        Uri uri = Uri.parse(fileUrl);
                        intent.setDataAndType(uri, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(intent, "Chọn ứng dụng để mở"));
                    } catch (Exception e) {
                        Toast.makeText(this, "Không tìm thấy ứng dụng hỗ trợ: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        downloadAndOpenFile(fileUrl, fileName);
                    }
                })
                .setNeutralButton("Tải về", (dialog, which) -> downloadAndOpenFile(fileUrl, fileName))
                .setNegativeButton("Hủy", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                })
                .setCancelable(true)
                .show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private String getErrorMessage(int errorCode, String description) {
        switch (errorCode) {
            case WebViewClient.ERROR_TIMEOUT:
                return "Hết thời gian tải file. Vui lòng thử lại.";
            case WebViewClient.ERROR_CONNECT:
                return "Không thể kết nối đến server. Vui lòng kiểm tra mạng.";
            case WebViewClient.ERROR_HOST_LOOKUP:
                return "Không tìm thấy server. Vui lòng kiểm tra URL hoặc mạng.";
            case WebViewClient.ERROR_UNSUPPORTED_SCHEME:
                return "Định dạng URL không được hỗ trợ.";
            case WebViewClient.ERROR_BAD_URL:
                return "URL file không hợp lệ.";
            default:
                return "Lỗi tải file: " + description;
        }
    }

    private void showOpenOrDownloadOption(String fileUrl, String fileName) {
        new AlertDialog.Builder(this)
                .setTitle("Không thể xem file")
                .setMessage("File không thể hiển thị trực tiếp. Bạn muốn mở bằng ứng dụng khác hoặc tải về không?")
                .setPositiveButton("Mở bằng ứng dụng", (dialog, which) -> {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(fileUrl));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(Intent.createChooser(intent, "Chọn ứng dụng để mở"));
                    } catch (Exception e) {
                        Toast.makeText(this, "Không tìm thấy ứng dụng hỗ trợ: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        downloadAndOpenFile(fileUrl, fileName);
                    }
                })
                .setNeutralButton("Tải về và mở", (dialog, which) -> downloadAndOpenFile(fileUrl, fileName))
                .setNegativeButton("Hủy", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                })
                .setCancelable(true)
                .show();
    }

    private void downloadAndOpenFile(String fileUrl, String fileName) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileUrl))
                .setTitle(fileName)
                .setDescription("Đang tải file...")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true);

        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        try {
            long downloadId = dm.enqueue(request);
            BroadcastReceiver onComplete = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                    if (id == downloadId) {
                        runOnUiThread(() -> {
                            openDownloadedFile(fileName);
                            Toast.makeText(FileWebViewActivity.this, "Tải file thành công!", Toast.LENGTH_SHORT).show();
                        });
                        unregisterReceiver(this);
                    }
                }
            };
            registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi tải file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void openDownloadedFile(String fileName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
        Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String mimeType = isExcelFile(fileName) ? "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" : "*/*";
        intent.setDataAndType(uri, mimeType);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivity(Intent.createChooser(intent, "Chọn ứng dụng để mở"));
        } catch (Exception e) {
            Toast.makeText(this, "Không tìm thấy ứng dụng hỗ trợ: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loadingDialog != null) {
            loadingDialog.hide();
        }
    }
}