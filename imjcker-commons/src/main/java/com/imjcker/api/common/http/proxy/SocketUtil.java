package com.imjcker.api.common.http.proxy;

import com.imjcker.api.common.model.SourceLogInfo;
import com.imjcker.api.common.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.SocketFactory;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Socket utils
 */
public class SocketUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketUtil.class);
    private static final int HEADER_LENGTH = 6;

    private static final String ENCODING = "GBK"; // UTF-8 会出现中文乱码

    public static Result send2TcpServer(String msg, String host, int port, int readTimeout) {
        Result result =new Result();
        long start = System.currentTimeMillis();
        String strResult;
        try {
            byte[] requestData = msg.getBytes(ENCODING);
            byte[] responseDate = send2TcpServer(requestData, host, port, readTimeout);
            if (responseDate != null) {
                strResult=new String(responseDate, ENCODING);
                result.setResult(strResult);
                SourceLogInfo info = SourceLogInfo.builder()
                        .sourceCreateTime(start)
                        .sourceResponseCode(200)
                        .sourceResult(strResult)
                        .sourceTimeout(readTimeout)
                        .sourceSpendTime(System.currentTimeMillis()-start).build();
                result.setSourceLogInfo(info);
            }
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("send2TcpServer exception:{}", e.getMessage());
            result.setResult(null);
            SourceLogInfo info = SourceLogInfo.builder()
                    .sourceCreateTime(start)
                    .sourceResponseCode(9999)
                    .sourceResult(null)
                    .sourceSpendTime(System.currentTimeMillis()-start).build();
            result.setSourceLogInfo(info);
        }
        return result;
    }

    private static byte[] send2TcpServer(byte[] data, String host, int port, int readTimeout) {
        Socket socket = null;
        InputStream inStream = null;
        try {
            SocketFactory socketFactory = SocketFactory.getDefault();
            socket = socketFactory.createSocket(host, port);
            socket.setReceiveBufferSize(4096);
            socket.setSendBufferSize(4096);
            socket.setSoTimeout(readTimeout);
            socket.setKeepAlive(false);
            socket.setTcpNoDelay(false);
            socket.getOutputStream().write(data);
            socket.getOutputStream().flush();
            inStream = socket.getInputStream();
            byte[] response = readInputStream(inStream, HEADER_LENGTH, true);
            String responseBody = new String(response, StandardCharsets.UTF_8);
            int bodyLength = Integer.parseInt(responseBody);
            byte[] result = readInputStream(inStream, bodyLength, true);
            LOGGER.debug("原始报文长[{}],内容:{}", bodyLength, new String(result, ENCODING));
            return result;
        } catch (Exception e) {
            LOGGER.error("send2TcpServer exception:{}", e.getMessage());
        } finally {
            if (null != inStream) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    LOGGER.error("send2TcpServer exception:{}", e.getMessage());
                }
            }
            if (null != socket) {
                try {
                    socket.close();
                } catch (IOException e) {
                    LOGGER.error("send2TcpServer socket close exception:{}", e.getMessage());
                }
            }
        }
        return null;
    }

    private static byte[] getHeader(int headerLen, int bodyLen) {
        StringBuilder sb = new StringBuilder();
        String head = String.valueOf(bodyLen);
        for (int i = head.length(); i < headerLen; i++) {
            sb.append("0");
        }
        sb.append(head);
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    /***
     * 从输入流中读取字节数组
     * @param inputStream 输入流
     * @param length 预期长度,调用方自行检查长度的合法性
     * @param checkLen 是否检查长度
     * @return 读取的字节数组
     * @throws IOException io
     */
    private static byte[] readInputStream(InputStream inputStream, int length, boolean checkLen) throws IOException {
        byte[] bytes = new byte[length];
        int readLen = 0;
        int currentLen = -1;
        while (readLen < length) {
            currentLen = inputStream.read(bytes, readLen, length - readLen);
            if (currentLen == -1) {// 读取返回结果时，未读到预期的长度即结束
                if (checkLen) {
                    throw new IOException("输入流异常结束");
                }
                return copyBytes(bytes, readLen);
            }
            readLen += currentLen;
        }
        return bytes;
    }

    /***
     * 从目标字节数组的指定开始位置开始copy指定长度的子字节数组
     * @param copyLen 目标长度
     * @return copy bytes
     */
    private static byte[] copyBytes(byte[] bytes, int copyLen) {
        if (null == bytes || copyLen <= 0) {
            return new byte[0];
        }

        if (copyLen > bytes.length) {
            copyLen = bytes.length;
        }
        byte[] res = new byte[copyLen];
        System.arraycopy(bytes, 0, res, 0, copyLen);
        return res;
    }

    private static byte[] readData(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        BufferedReader bReader = null;
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            bReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), ENCODING));
            String oneLine = "";
            while (bReader.ready()) {
                oneLine = bReader.readLine();
                out.write(oneLine.trim().getBytes(ENCODING));
            }
            bReader.close();
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != bReader) {
                try {
                    bReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
