package utils;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {
    private static final Logger LOGGER = Logger.getLogger(CommonUtils.class);

    private CommonUtils() {
    }

    public static void loadProperties(String propertiesPath) {
        String strConfigDirectory = new File(System.getProperty("user.dir")) + propertiesPath;
        File directory = new File(strConfigDirectory);
        File[] fList = directory.listFiles();
        if (fList == null) {
            throw new NullPointerException("Properties file(s) are not found... Please check inputted path.");
        }
        for (File file : fList) {
            InputStream is = null;
            String strFileName = file.getAbsolutePath();

            try {
                is = new FileInputStream(strFileName);
                System.getProperties().load(is);
            } catch (IOException ex) {
                LOGGER.error(ex);
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException ex) {
                        LOGGER.error(ex);
                    }
                }
            }
        }
    }

    public static boolean hasText(String strValue) {
        return strValue != null && !strValue.isEmpty();
    }

    public static String getPathFile() {
        String currentDir = (new File("")).getAbsolutePath();
        String baseDirPath = currentDir.substring(0, currentDir.lastIndexOf(File.separator))
                + File.separator + System.getProperty("apkModuleName") + File.separator + "build";
        return baseDirPath + File.separator + "outputs" + File.separator + "apk";
    }

    public static String getApkFile() {
        String strFilePath = getPathFile();
        String strApkFormatName = System.getProperty("apkFormatName");
        String regex = strApkFormatName + "*";
        Pattern p = Pattern.compile(regex);
        Date date = null;
        File directory = new File(strFilePath);
        File[] fList = directory.listFiles();
        String strFileResult = "";

        try {
            File[] var8 = fList;
            int var9 = fList.length;

            for(int var10 = 0; var10 < var9; ++var10) {
                File file = var8[var10];
                Matcher m = p.matcher(file.toString());
                if(m.find()) {
                    String strDate = file.toString().substring(file.toString().length() - 10, file.toString().length() - 3);
                    Date tmpDate = convertStringToDate(strDate);
                    if(date == null) {
                        date = tmpDate;
                        strFileResult = file.toString();
                    } else if(tmpDate.after(date)) {
                        date = tmpDate;
                        strFileResult = file.toString();
                    }
                }
            }

            LOGGER.info("APK for installation: " + strFileResult);
        } catch (NullPointerException var15) {
            LOGGER.error("Cannot get APK file, please help to check again the APK file!", var15);
            System.exit(0);
        }

        return strFileResult;
    }

    public static Date convertStringToDate(String strDate) {
        Date date = new Date();

        try {
            DateFormat format = new SimpleDateFormat("yymmdd", Locale.ENGLISH);
            date = format.parse(strDate);
        } catch (ParseException var3) {
            LOGGER.error(var3);
        }

        return date;
    }

    public static String executeCommand(String command) {
        String output = null;
        try {
            Scanner scanner = new Scanner(Runtime.getRuntime().exec(command).getInputStream()).useDelimiter("\\A");
            if (scanner.hasNext()) output = scanner.next();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return output;
    }
}
