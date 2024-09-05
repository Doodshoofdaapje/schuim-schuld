package com.boris.schuimschuld.util;

import android.content.Context;
import android.text.TextUtils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class JsonFileHandler {

    private Context context;
    public JsonFileHandler(Context context) {
        this.context = context;
    }

    public boolean createFile(String fileName) {
        File file = new File(context.getFilesDir(), fileName);

        if (!file.exists()) {
            try {
                file.createNewFile();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void writeFile(String fileName, JSONObject input) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);

            fileOutputStream.write(input.toJSONString().getBytes(Charset.forName("UTF-8")));
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void copyFromRaw(String fileName, int id, String jsonKey) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            JSONObject objectToCopy = (JSONObject) readFileFromRaw(id).get(jsonKey);

            if (objectToCopy != null) {
                fileOutputStream.write(objectToCopy.toJSONString().getBytes(Charset.forName("UTF-8")));
                fileOutputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONObject readFileFromInternal(String fileName) {
        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, Charset.forName("UTF-8"));

            return readStreamer(inputStreamReader);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public JSONObject readFileFromRaw(int id) {
        try {
            InputStream fileInputStream =  context.getResources().openRawResource(id);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, Charset.forName("UTF-8"));

            return readStreamer(inputStreamReader);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public JSONObject readStreamer(InputStreamReader inputStreamReader) {
        try {
            JSONParser jsonParser = new JSONParser();
            List<String> lines = new ArrayList<String>();
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();

            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }

            JSONObject outputObject = (JSONObject) jsonParser.parse(TextUtils.join("\n", lines));
            reader.close();
            inputStreamReader.close();
            return outputObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
