# 錯誤修正：4-1 使用照相機與麥克風

這一章加入的照相與錄音功能，把照片與錄音檔案名稱儲存在同一個欄位，因此造成衝突。依照下列的步驟修正錯誤：

1. 開啟「Item.java」，加入下列的欄位與方法宣告：

        // 錄音檔案名稱
        private String recFileName;

        public String getRecFileName() {
            return recFileName;
        }
    
        public void setRecFileName(String recFileName) {
            this.recFileName = recFileName;
        }
        
2. 同樣在「Item.java」，為建構子加入錄音檔案名稱參數：

        // 錄音檔案名稱參數：String recFileName
        public Item(long id, long datetime, Colors color, String title,
                    String content, String fileName, String recFileName,
                    double latitude, double longitude, long lastModify) {
            this.id = id;
            this.datetime = datetime;
            this.color = color;
            this.title = title;
            this.content = content;
            this.fileName = fileName;
            // 錄音檔案名稱
            this.recFileName = recFileName;
            this.latitude = latitude;
            this.longitude = longitude;
            this.lastModify = lastModify;
        }

3. 開啟「ItemDAO.java」，加入與修改下列的欄位宣告：

        ...
        // 錄音檔案名稱
        public static final String RECFILENAME_COLUMN = "recfilename";
        ...    
        // 在「FILENAME_COLUMN」下方加入錄音檔案名稱欄位
        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        DATETIME_COLUMN + " INTEGER NOT NULL, " +
                        COLOR_COLUMN + " INTEGER NOT NULL, " +
                        TITLE_COLUMN + " TEXT NOT NULL, " +
                        CONTENT_COLUMN + " TEXT NOT NULL, " +
                        FILENAME_COLUMN + " TEXT, " +
                        RECFILENAME_COLUMN + " TEXT, " +    // 增加錄音檔案名稱
                        LATITUDE_COLUMN + " REAL, " +
                        LONGITUDE_COLUMN + " REAL, " +
                        LASTMODIFY_COLUMN + " INTEGER, " +
                        ALARMDATETIME_COLUMN + " INTEGER)";

4. 同樣在「ItemDAO.java」，修改「insert」方法：

        public Item insert(Item item) {
            ContentValues cv = new ContentValues();
    
            cv.put(DATETIME_COLUMN, item.getDatetime());
            cv.put(COLOR_COLUMN, item.getColor().parseColor());
            cv.put(TITLE_COLUMN, item.getTitle());
            cv.put(CONTENT_COLUMN, item.getContent());
            cv.put(FILENAME_COLUMN, item.getFileName());
            // 錄音檔案名稱
            cv.put(RECFILENAME_COLUMN, item.getRecFileName());
            cv.put(LATITUDE_COLUMN, item.getLatitude());
            cv.put(LONGITUDE_COLUMN, item.getLongitude());
            cv.put(LASTMODIFY_COLUMN, item.getLastModify());
            cv.put(ALARMDATETIME_COLUMN, item.getAlarmDatetime());
            long id = db.insert(TABLE_NAME, null, cv);
            item.setId(id);
        
            return item;
        }

5. 同樣在「ItemDAO.java」，修改「update」方法：

        public boolean update(Item item) {
            ContentValues cv = new ContentValues();

            cv.put(DATETIME_COLUMN, item.getDatetime());
            cv.put(COLOR_COLUMN, item.getColor().parseColor());
            cv.put(TITLE_COLUMN, item.getTitle());
            cv.put(CONTENT_COLUMN, item.getContent());
            cv.put(FILENAME_COLUMN, item.getFileName());
            // 錄音檔案名稱
            cv.put(RECFILENAME_COLUMN, item.getRecFileName());
            cv.put(LATITUDE_COLUMN, item.getLatitude());
            cv.put(LONGITUDE_COLUMN, item.getLongitude());
            cv.put(LASTMODIFY_COLUMN, item.getLastModify());
            cv.put(ALARMDATETIME_COLUMN, item.getAlarmDatetime());
            String where = KEY_ID + "=" + item.getId();

            return db.update(TABLE_NAME, cv, where, null) > 0;
        }

6. 同樣在「ItemDAO.java」，修改「getRecord」方法：

        public Item getRecord(Cursor cursor) {
            Item result = new Item();
    
            result.setId(cursor.getLong(0));
            result.setDatetime(cursor.getLong(1));
            result.setColor(ItemActivity.getColors(cursor.getInt(2)));
            result.setTitle(cursor.getString(3));
            result.setContent(cursor.getString(4));
            result.setFileName(cursor.getString(5));
            // 錄音檔案名稱
            result.setRecFileName(cursor.getString(6));
            // 後續的編號都要修改
            result.setLatitude(cursor.getDouble(7));
            result.setLongitude(cursor.getDouble(8));
            result.setLastModify(cursor.getLong(9));
            result.setAlarmDatetime(cursor.getLong(9));
    
            return result;
        }

7. 同樣在「ItemDAO.java」，修改「sample」方法：

        public void sample() {
            // 增加錄音檔案名稱參數「""」
            Item item = new Item(0, new Date().getTime(), Colors.RED, "關於Android Tutorial的事情.", "Hello content", "", "", 0, 0, 0);
            Item item2 = new Item(0, new Date().getTime(), Colors.BLUE, "一隻非常可愛的小狗狗!", "她的名字叫「大熱狗」，又叫\n作「奶嘴」，是一隻非常可愛\n的小狗。", "", "", 25.04719, 121.516981, 0);
            Item item3 = new Item(0, new Date().getTime(), Colors.GREEN, "一首非常好聽的音樂！", "Hello content", "", "", 0, 0, 0);
            Item item4 = new Item(0, new Date().getTime(), Colors.ORANGE, "儲存在資料庫的資料", "Hello content", "", "", 0, 0, 0);
    
            insert(item);
            insert(item2);
            insert(item3);
            insert(item4);
        }

8. 開啟「MyDBHelper.java」，增加資料庫版本編號：

        // 資料庫版本，資料結構改變的時候要更改這個數字，通常是加一
        public static final int VERSION = 3;

9. 開啟「ItemActivity.java」，增加錄音檔案名稱欄位變數：

        // 錄音檔案名稱
        private String recFileName;

10. 同樣在「ItemActivity.java」，增加取得錄音檔案名稱的方法：

        private File configRecFileName(String prefix, String extension) {
            // 如果記事資料已經有檔案名稱
            if (item.getRecFileName() != null && item.getRecFileName().length() > 0) {
                recFileName = item.getRecFileName();
            }
            // 產生檔案名稱
            else {
                recFileName = FileUtil.getUniqueFileName();
            }
    
            return new File(FileUtil.getExternalStorageDir(FileUtil.APP_DIR),
                    prefix + recFileName + extension);
        }

11. 同樣在「ItemActivity.java」，修改啟動錄音元件的方法：

        public void clickFunction(View view) {
            int id = view.getId();
    
            switch (id) {
                ...
                case R.id.record_sound:
                    // 修改呼叫方法的名稱為「configRecFileName」
                    final File recordFile = configRecFileName("R", ".mp3");
    
                    if (recordFile.exists()) {
                        ...
                    }
                    // 如果沒有錄音檔，啟動錄音元件
                    else {
                        goToRecord(recordFile);
                    }
    
                    break;
                ...
            }
    
        }

完成全部的修改以後執行應用程式，測試同一個記事資料照相與錄音的功能。