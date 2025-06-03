package com.example.appholaagri.model.MenuHomeModel;
import java.util.List;
public class MenuHomeResponse {
    private List<MenuItem> data;

    public List<MenuItem> getData() {
        return data;
    }

    public void setData(List<MenuItem> data) {
        this.data = data;
    }

    public static class MenuItem {
        private int id;
        private String code;
        private String title;
        private int type;
        private String url;
        private int position;
        private List<ChildItem> child;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public List<ChildItem> getChild() {
            return child;
        }

        public void setChild(List<ChildItem> child) {
            this.child = child;
        }
    }

    public static class ChildItem {
        private int id;
        private String nextScreenCode;
        private int type;
        private String url;
        private String notification;
        private int position;
        private String title;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNextScreenCode() {
            return nextScreenCode;
        }

        public void setNextScreenCode(String nextScreenCode) {
            this.nextScreenCode = nextScreenCode;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getNotification() {
            return notification;
        }

        public void setNotification(String notification) {
            this.notification = notification;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
