/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package multichat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.event.EventListenerList;

/**
 *
 * @author vbmacher
 */
public class MultiChat {
    private MulticastSocket soc;
    private DatagramPacket hello;
    private InetAddress group;
    private final int port = 2345;
    private Timer onlineSender;
    private Hashtable online;
    private boolean running;
    private Thread onlineRequest;
    private Timer onlineGarbageCollector;
    private EventListenerList ell; 
    private EventObject evt;
    private String nick;
    
    public interface MessageListener extends EventListener {
        public void gotMessage(String from, String data, long time, EventObject e);
        public void onlineChanged(boolean add);
    }
    
    
    public MultiChat(String nick) throws IOException, UnknownHostException  {
        online = new Hashtable();
        this.nick = nick;
        evt = new EventObject(this);
        soc = new MulticastSocket(port);
        group = InetAddress.getByName("224.0.0.1");
        soc.joinGroup(group);
        soc.setTimeToLive(64);
        
        byte[]hel = (">HELLO!<" + this.nick).getBytes();
        hello = new DatagramPacket(hel, hel.length, group, port);

        ell = new EventListenerList();
        
        onlineSender = new Timer();
        onlineSender.scheduleAtFixedRate(new OnlineSender(), 0, 3000);
        
        running = true;
        onlineRequest = new Thread(new OnlineRequest());
        onlineRequest.start();
        
        onlineGarbageCollector = new Timer();
        onlineGarbageCollector.scheduleAtFixedRate(new OnlineGarbageCollector(),
                0, 5000);
    }
    
    public void addML(MessageListener l) {
        ell.add(MessageListener.class, l);
    }
    
    public void removeML(MessageListener l) {
        ell.remove(MessageListener.class, l);
    }
    
    private class OnlineSender extends TimerTask {
        public void run() {
            try { soc.send(hello); } catch (IOException ex) {}
        }
    }
    
    private class OnlineRequest implements Runnable {
        public void run() {
            try { soc.setSoTimeout(1000); } catch (SocketException ex) {}
            while (running) {
                try {
                    byte[] buffer = new byte[1100];
                    DatagramPacket p = new DatagramPacket(buffer, buffer.length);
                    try {
                        soc.receive(p);
                        byte[] dataB = p.getData();
                        String data = (new String(dataB)).substring(0,p.getLength());
                        if (data.startsWith(">HELLO!<")) {
                            // I AM ALIVE
                            synchronized (online) {
                                online.put(data.substring(8),
                                        System.currentTimeMillis());
                            }
                            fireOnline(true);
                        } else if (data.startsWith("TO:"+nick+":FROM:")
                                || data.startsWith("TO:ALL:FROM:")) {
                            if (data.startsWith("TO:ALL:FROM:"))
                                data = data.substring(12);
                            else
                                data = data.substring(9+nick.length());
                            String from = data.substring(0,data.indexOf(":DATA:"));
                            data = data.substring(data.indexOf(":DATA:")+6);
                            // DATA (max 1100 chars) -> fire events
                            Object[] li = ell.getListenerList();
                            for (int i=0; i<li.length; i+=2) {
                                if (li[i] == MessageListener.class)
                                    ((MessageListener)li[i+1])
                                            .gotMessage(from, data, 
                                            System.currentTimeMillis(),evt);
                            }
                        }
                    } catch(SocketTimeoutException e) {}
                    Thread.yield();
                } catch (IOException ex) {}
            }
        }
    }
    
    private void fireOnline(boolean add) {
        Object[] li = ell.getListenerList();
        for (int i=0; i<li.length; i+=2) {
            if (li[i] == MessageListener.class)
                ((MessageListener)li[i+1])
                        .onlineChanged(add);
        }
    }
    
    private class OnlineGarbageCollector extends TimerTask {
        public void run() {
            long currentTime = System.currentTimeMillis();
            synchronized (online) {
                // remove all "online" users that have time < 15 s
                for (Enumeration e = online.keys(); e.hasMoreElements(); ) {
                    String key = (String)e.nextElement();
                    if ((currentTime - (Long)online.get(key)) >= 15000) {
                        online.remove(key);
                        fireOnline(false);
                    }
                }
            }
        }
    }
    
    public Object[] getOnlineList() {
        synchronized (online) {
            return online.keySet().toArray();
        }
    }

    public void stop() {
        running = false;
        onlineSender.cancel();
        onlineGarbageCollector.cancel();
        try { soc.leaveGroup(group); }
        catch(IOException e) {}
    }
    
    public void send(String to_who, String data, boolean to_all) {
        if (!to_all && !online.containsKey(to_who)) {
            return;
        }
        if (to_all) to_who = "ALL";
        data = "TO:"+to_who+":FROM:"+nick+":DATA:"+data;
        byte[] buffer = data.getBytes();
        DatagramPacket p = new DatagramPacket(buffer, buffer.length,group,port);
        try { soc.send(p); } catch(IOException e) {}
    }
    
    public void change_nick(String new_nick) {
        this.nick = new_nick;
        byte[]hel = (">HELLO!<" + this.nick).getBytes();
        hello = new DatagramPacket(hel, hel.length, group, port);
    }
}
