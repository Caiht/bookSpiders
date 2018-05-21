package transform;

public class test {

    public static byte[] getUTF8BytesFromGBKString(String gbkStr) {
        int n = gbkStr.length();
        byte[] utfBytes = new byte[3 * n];
        int k = 0;
        for (int i = 0; i < n; i++) {
            int m = gbkStr.charAt(i);
            if (m < 128 && m >= 0) {
                utfBytes[k++] = (byte) m;
                continue;
            }
            utfBytes[k++] = (byte) (0xe0 | (m >> 12));
            utfBytes[k++] = (byte) (0x80 | ((m >> 6) & 0x3f));
            utfBytes[k++] = (byte) (0x80 | (m & 0x3f));
        }
        if (k < utfBytes.length) {
            byte[] tmp = new byte[k];
            System.arraycopy(utfBytes, 0, tmp, 0, k);
            return tmp;
        }
        return utfBytes;
    }
    public static void main(String[] args) throws Exception {
        StringBuilder strBuilder = new StringBuilder();
        for(int i =101700001;i<=105999999;i++){
            strBuilder.append("http://cnki.scstl.org/kcms/detail/detail.aspx?QueryID=1&CurRec=1&dbcode=SCPD&dbname=SCPD2016&filename=CN");
        }
    }

}
