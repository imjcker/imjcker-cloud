package com.imjcker.api.handler.util;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

public class SignatureUtil {

    private SignatureUtil() {

    }

    private static final char[] bcdLookup = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static final String prikeyvalue = "30820276020100300d06092a864886f70d0101010500048202603082025c020100028181008901c87fc2e67344af11e792f525dbb72e6cf94c138c0cc5518829428bc5e569824db70eb1e07510754108d1c7ad37d0dd5e18c36eb4d8940e09d3e4935dd77652559974e0f857c52420199e3d95523e71d31a5a04dc61597f8ab6b67b35b345912ebd032e0742a363062ee3d88e17928e0199d5f2621dbc5c1ef7981829e55f0203010001028180507ffeb9aadfd2fc994d10ded531d05e5a65e738df075bfd984143d4f8e167414e31b18c6dd16f9722fdfa5ba05253c86239469d95efa68d9f8e00b57e2d164a6fe98612e0d11f2af83c02477d08eb843b71f7fa81222e25342c68e525cc2761fe2b1da76e731448ed8929657a45fab29accdb131c9ee37c4503cf7df5930ce9024100c7212f81757bd7943fb4a49d91755ffa176704cb69419bc58600e21431861077132c0e0a10f2baddbbb6648df156255caaa7caf6f3cee2b9e49dbeaaae814f5b024100b022ab89ebd5da14749ad40a893ef4ce3ab228d1fab709af892c903889b2f3c2933956cb8f5187613a59192520c3068445e4a612570e24018a15f6eb65dcc54d02400cd1b3183772f886dfaf0665ea664efe6fa13f2ac524e71d492a2763bb687108e9600dab5239fe13a630f0ba3e8e182ba604d5491e9af3f799a31a122cd7c03f0240709caa0a1353760bf005719e8ee346877835a22da65b5280fd76157b60669ebcff7e0085434b7fffe4db445d5d95f35bac8824b9c31912623d940f7baf73b85d02410087f3e2945752e34a78996fd611d4c1ca7c6d987bf29624bb02c441e8270069282e01ec9a44a74e8060ce56ae179e7cee0e35a7c070e847a45d875b45c33d40ae";

    public static String signContent(String myinfo) {
        return signContent(prikeyvalue, myinfo);
    }

    public static String signContent(String prikeyvalue, String myinfo) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(hexStrToBytes(prikeyvalue));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey myprikey = keyf.generatePrivate(priPKCS8);
            // 用私钥对信息生成数字签名
            java.security.Signature signet = java.security.Signature.getInstance("MD5withRSA");
            signet.initSign(myprikey);
            signet.update(myinfo.getBytes("UTF-8"));
            byte[] signed = signet.sign();
            return bytesToHexStr(signed);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Transform the specified byte into a Hex String form.
     */
    public static final String bytesToHexStr(byte[] bcd) {
        StringBuilder s = new StringBuilder(bcd.length * 2);

        for (int i = 0; i < bcd.length; i++) {
            s.append(bcdLookup[(bcd[i] >>> 4) & 0x0f]);
            s.append(bcdLookup[bcd[i] & 0x0f]);
        }

        return s.toString();
    }

    /**
     * Transform the specified Hex String into a byte array.
     */
    public static final byte[] hexStrToBytes(String s) {
        byte[] bytes;

        bytes = new byte[s.length() / 2];

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16);
        }

        return bytes;
    }

}
