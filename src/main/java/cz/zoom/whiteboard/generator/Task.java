package cz.zoom.whiteboard.generator;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Task {
    
    private final Map<String, Object> taskData = new HashMap<String, Object>();
    
    public Task(Map<String, Object> task) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        taskData.putAll(task);
        taskData.put("hash", generateHash());
    }
    
    public String getSummary() {
        return (String)taskData.get("summary");
    }
    
    public String getHash() {
        return (String)taskData.get("hash");
    }
    
    public String getId() {
        return (String)taskData.get("id");
    }
    
    public Map<String, Object> getData() {
        return Collections.unmodifiableMap(taskData);
    }
    
    private String generateHash() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] sha1hash;
        md.update(bytes, 0, bytes.length);
        sha1hash = md.digest();
        return byteArrayToHexString(sha1hash).substring(0, 6);
    }

    private static String byteArrayToHexString(byte[] b) {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }
    
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        
        if (!(other instanceof Task)) {
            return false;
        }
        
        return getHash().equals(((Task)other).getHash());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + getHash().hashCode();
        return hash;
    }
    
}
