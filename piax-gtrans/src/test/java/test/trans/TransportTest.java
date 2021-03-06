package test.trans;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.piax.common.ComparableKey;
import org.piax.common.Destination;
import org.piax.common.PeerId;
import org.piax.common.PeerLocator;
import org.piax.common.TransportId;
import org.piax.common.dcl.DCLTranslator;
import org.piax.common.subspace.KeyRange;
import org.piax.common.wrapper.DoubleKey;
import org.piax.gtrans.Channel;
import org.piax.gtrans.ChannelListener;
import org.piax.gtrans.ChannelTransport;
import org.piax.gtrans.FutureQueue;
import org.piax.gtrans.Peer;
import org.piax.gtrans.ReceivedMessage;
import org.piax.gtrans.TransOptions;
import org.piax.gtrans.TransOptions.RetransMode;
import org.piax.gtrans.Transport;
import org.piax.gtrans.TransportListener;
import org.piax.gtrans.ov.Overlay;
import org.piax.gtrans.ov.OverlayListener;
import org.piax.gtrans.ov.OverlayReceivedMessage;
import org.piax.gtrans.ov.sg.MSkipGraph;
import org.piax.gtrans.ov.szk.Suzaku;
import org.piax.gtrans.raw.emu.EmuLocator;
import org.piax.gtrans.raw.tcp.TcpLocator;
import org.piax.gtrans.raw.udp.UdpLocator;
import org.piax.gtrans.util.FailureSimulationChannelTransport;
import org.piax.gtrans.util.ThroughTransport;

public class TransportTest {
	
    boolean udp_received1, udp_received2;

    @Test
    public void UDPTransportTest() throws Exception {
        // get peers
        Peer p1 = Peer.getInstance(new PeerId("p1"));
        Peer p2 = Peer.getInstance(new PeerId("p2"));

        try {
        // top level
        Transport<UdpLocator> tr1 = p1.newBaseTransport(
        				new UdpLocator(
        						new InetSocketAddress("localhost", 12367)));
        
        Transport<UdpLocator> tr2 = p2.newBaseTransport(
        				new UdpLocator(
        						new InetSocketAddress("localhost", 12368)));

        udp_received1 = false;
        udp_received2 = false;
                
        tr1.setListener(new TransportListener<UdpLocator>() {
            public void onReceive(Transport<UdpLocator> trans, ReceivedMessage msg) {
                udp_received1 = "654321".equals(msg.getMessage());
            }
        });
        
        tr2.setListener(new TransportListener<UdpLocator>() {
            public void onReceive(Transport<UdpLocator> trans, ReceivedMessage msg) {
                udp_received2 = "123456".equals(msg.getMessage());
            }
        });

        tr1.send((UdpLocator) tr2.getEndpoint(), "123456");
        tr2.send((UdpLocator) tr1.getEndpoint(), "654321");

        // Channel ch = tr1.newChannel(tr2.getMyEndpoint());
        // ch.send("abcdefg".getBytes());

        // ReceivedMessage b = ch.receive(1000);
        // ch.close();
        Thread.sleep(1000);
        }
        finally {
            p1.fin();
            p2.fin();
        }
        assertTrue("UDP1 receive failed", udp_received1);
        assertTrue("UDP2 receive failed", udp_received2);
        // assertTrue(ByteUtil.equals(b.getMessage(), "abcdefg".getBytes()));
    }

    @Test
    public void UDPTransportChannelTest() throws Exception {
        // get peers
        Peer p1 = Peer.getInstance(new PeerId("p1"));
        Peer p2 = Peer.getInstance(new PeerId("p2"));
        try {
        // top level
        ChannelTransport<UdpLocator> tr1 = p1.newBaseChannelTransport(
                        new UdpLocator(
                                new InetSocketAddress("localhost", 12367)));
        ChannelTransport<UdpLocator> tr2 = p2.newBaseChannelTransport(
                        new UdpLocator(
                                new InetSocketAddress("localhost", 12368)));

        tr1.setChannelListener(new ChannelListener<UdpLocator>() {
            public boolean onAccepting(Channel<UdpLocator> channel) {
                return true;
            }

            public void onClosed(Channel<UdpLocator> channel) {
            }

            public void onFailure(Channel<UdpLocator> channel, Exception cause) {
            }

            public void onReceive(Channel<UdpLocator> ch) {
                if (ch.isCreatorSide())
                    return;
                Object msg = ch.receive();
                try {
                    ch.send(msg);
                } catch (IOException e) {
                    fail("IOException occured");
                }
            }
        });
        
        Channel<UdpLocator> c = tr2.newChannel(tr1.getEndpoint());
        c.send("654321");
        String mes = (String) c.receive(1000);
        assertTrue(mes.equals("654321"));
        c.close();
        } finally {
        p1.fin();
        p2.fin();
        }
    }
    
    boolean sg_received1, sg_received2, sg_received3;

    @Test
    public void SGOnUdpTest() throws Exception {
        // get peers
        Peer p1 = Peer.getInstance(new PeerId("p1"));
        Peer p2 = Peer.getInstance(new PeerId("p2"));
        try {
        // base transport
        PeerLocator loc;
        ChannelTransport<?> bt1 = p1.newBaseChannelTransport(
                loc = new UdpLocator(new InetSocketAddress("localhost", 12367)));
        ChannelTransport<?> bt2 = p2.newBaseChannelTransport(
                new UdpLocator(new InetSocketAddress("localhost", 12368)));

        // top level
        //Overlay<ComparableKey<?>, ComparableKey<?>> ov1, ov2;

        Overlay<ComparableKey<?>, ComparableKey<?>> tr1 = 
        		new MSkipGraph<ComparableKey<?>, ComparableKey<?>>(bt1);
        Overlay<ComparableKey<?>, ComparableKey<?>> tr2 =
        		new MSkipGraph<ComparableKey<?>, ComparableKey<?>>(bt2);
  
    /*    
        TopLevelTransport<ComparableKey<?>> tr1 = new TopLevelTransport<ComparableKey<?>>(
        		TopLevelTransport.DEFAULT_APP_ID, ov1 = new MSkipGraph<ComparableKey<?>, ComparableKey<?>>(bt1));
        TopLevelTransport<ComparableKey<?>> tr2 = new TopLevelTransport<ComparableKey<?>>(
        		TopLevelTransport.DEFAULT_APP_ID, ov2 = new MSkipGraph<ComparableKey<?>, ComparableKey<?>>(bt2));
  */      
        sg_received1 = false;
        sg_received2 = false;

        tr1.setListener(new TransportListener<ComparableKey<?>>() {
            public void onReceive(Transport<ComparableKey<?>> trans,
                    ReceivedMessage rmsg) {
                System.out.println("udp recv1:" + rmsg.getMessage());
                sg_received1 = rmsg.getMessage().equals("recv");
            }
        });

        tr2.setListener(new TransportListener<ComparableKey<?>>() {
            public void onReceive(Transport<ComparableKey<?>> trans,
                    ReceivedMessage rmsg) {
                try {
                    System.out.println("udp recv2:" + rmsg.getMessage());
                    sg_received2 = true;
                    System.out.println(trans.getTransportIdPath());
                    trans.send(new DoubleKey(Double.parseDouble(
                    		(String) rmsg.getMessage())), "recv");
                } catch (IOException e) {
                    fail("IOException occured");
                }
            }
        });

        boolean succ1 = tr1.join(loc);
        boolean succ2 = tr2.join(loc);
        Thread.sleep(500);
        boolean succ3 = tr1.addKey(new DoubleKey(1.0));
        boolean succ4 = tr2.addKey(new DoubleKey(2.0));

        assertTrue("SG1 join failed", succ1);
        assertTrue("SG2 join failed", succ2);
        assertTrue("SG1 addKey failed", succ3);
        assertTrue("SG2 addKey failed", succ4);
        Thread.sleep(500);

        DoubleKey key = null;
        for (ComparableKey<?> obj : tr1.getKeys()) {
            if (obj instanceof DoubleKey) {
                key = (DoubleKey) obj;
            }
        }
        System.out.println(tr2.getTransportIdPath());
        tr2.send(new DoubleKey(1.0), "recv");
        tr1.send(new DoubleKey(2.0), key.getKey().toString());
        //tr1.send(new DoubleKey(1.0), "recv");
//        tr2.send(new Ranges<Double>(new Range<Double>(1.0)), "recv");
//      tr1.send(new DestinationDescription("[2.0..2.0]"));
//        tr2.send(new Ranges<Integer>(new Range<Integer>(1)),
//                "654321".getBytes());
        
        Thread.sleep(100);
        assertTrue("SG2 receive failed", sg_received2);
        assertTrue("SG1 receive failed", sg_received1);
        sg_received1 = false;
        sg_received2 = false;
        tr1.send("[2.0..2.0]", "1.0");
        Thread.sleep(1000);
        assertTrue("SG2 receive failed", sg_received2);
        assertTrue("SG1 receive failed", sg_received1);
        sg_received1 = false;
        sg_received2 = false;
        } finally {
        p1.fin();
        p2.fin();
        }
    }

    @Test
    public void SGOnTcpTest() throws Exception {
        // get peers
        Peer p1 = Peer.getInstance(new PeerId("p1"));
        Peer p2 = Peer.getInstance(new PeerId("p2"));
        try {
        // base transport (TCP)
        PeerLocator loc;
        ChannelTransport<?> bt1 = p1.newBaseChannelTransport(
                loc = new TcpLocator(new InetSocketAddress("localhost", 12367)));
        ChannelTransport<?> bt2 = p2.newBaseChannelTransport(
                new TcpLocator(new InetSocketAddress("localhost", 12368)));

        // top level
        Overlay<ComparableKey<?>, ComparableKey<?>> tr1 = 
                	new MSkipGraph<ComparableKey<?>, ComparableKey<?>>(bt1);
        Overlay<ComparableKey<?>, ComparableKey<?>> tr2 = 
                	new MSkipGraph<ComparableKey<?>, ComparableKey<?>>(bt2);

        sg_received1 = false;
        sg_received2 = false;

        tr1.setListener(new TransportListener<ComparableKey<?>>() {
            public void onReceive(Transport<ComparableKey<?>> trans,
                    ReceivedMessage rmsg) {
                System.out.println("tcp recv1:" + rmsg.getMessage());
                sg_received1 = rmsg.getMessage().equals("recv");
            }
        });

        tr2.setListener(new TransportListener<ComparableKey<?>>() {
            public void onReceive(Transport<ComparableKey<?>> trans,
                    ReceivedMessage rmsg) {
                try {
                    System.out.println("tcp recv2:" + rmsg.getMessage());
                    sg_received2 = true;
                    trans.send(new DoubleKey(Double.parseDouble(
                            (String) rmsg.getMessage())), "recv");
                } catch (IOException e) {
                    fail("IOException occured");
                }
            }
        });

        boolean succ1 = tr1.join(loc);
        boolean succ2 = tr2.join(loc);
        Thread.sleep(500);
        boolean succ3 = tr1.addKey(new DoubleKey(1.0));
        boolean succ4 = tr2.addKey(new DoubleKey(2.0));

        assertTrue("SG1 join failed", succ1);
        assertTrue("SG2 join failed", succ2);
        assertTrue("SG1 addKey failed", succ3);
        assertTrue("SG2 addKey failed", succ4);
        Thread.sleep(500);

        DoubleKey key = null;
        for (ComparableKey<?> obj : tr1.getKeys()) {
            if (obj instanceof DoubleKey) {
                key = (DoubleKey) obj;
            }
        }
        tr1.send(new DoubleKey(2.0), key.getKey().toString());

        Thread.sleep(1000);
        assertTrue("SG2 receive failed", sg_received2);
        assertTrue("SG1 receive failed", sg_received1);
        } finally {
        p1.fin();
        p2.fin();
        }
    }

    @Test
    public void SGOnEmuTest() throws Exception {
        // get peers
        Peer p1 = Peer.getInstance(new PeerId("p1"));
        Peer p2 = Peer.getInstance(new PeerId("p2"));
        try {
        // base transport (Emu)
        PeerLocator loc;
        ChannelTransport<?> bt1 = p1.newBaseChannelTransport(
                loc = new EmuLocator(12367));
        ChannelTransport<?> bt2 = p2.newBaseChannelTransport(
                new EmuLocator(12368));

        // top level
        
        Overlay<ComparableKey<?>, ComparableKey<?>> tr1 = new MSkipGraph<ComparableKey<?>, ComparableKey<?>>(bt1);
        Overlay<ComparableKey<?>, ComparableKey<?>> tr2 = new MSkipGraph<ComparableKey<?>, ComparableKey<?>>(bt2);

        sg_received1 = false;
        sg_received2 = false;

        tr1.setListener(new TransportListener<ComparableKey<?>>() {
            public void onReceive(Transport<ComparableKey<?>> trans,
                    ReceivedMessage rmsg) {
                System.out.println("emu recv1:" + rmsg.getMessage());
                sg_received1 = rmsg.getMessage().equals("recv");
            }
        });

        tr2.setListener(new TransportListener<ComparableKey<?>>() {
            public void onReceive(Transport<ComparableKey<?>> trans,
                    ReceivedMessage rmsg) {
                try {
                    System.out.println("emu recv2:" + rmsg.getMessage());
                    sg_received2 = true;
                    trans.send(new DoubleKey(Double.parseDouble(
                            (String) rmsg.getMessage())), "recv");
                } catch (IOException e) {
                    fail("IOException occured");
                }
            }
        });

        boolean succ1 = tr1.join(loc);
        boolean succ2 = tr2.join(loc);
        Thread.sleep(500);
        boolean succ3 = tr1.addKey(new DoubleKey(1.0));
        boolean succ4 = tr2.addKey(new DoubleKey(2.0));

        assertTrue("SG1 join failed", succ1);
        assertTrue("SG2 join failed", succ2);
        assertTrue("SG1 addKey failed", succ3);
        assertTrue("SG2 addKey failed", succ4);
        Thread.sleep(500);

        DoubleKey key = null;
        for (ComparableKey<?> obj : tr1.getKeys()) {
            if (obj instanceof DoubleKey) {
                key = (DoubleKey) obj;
            }
        }
        tr1.send(new DoubleKey(2.0), key.getKey().toString());

        Thread.sleep(1000);
        assertTrue("SG2 receive failed", sg_received2);
        assertTrue("SG1 receive failed", sg_received1);
        } finally {
        p1.fin();
        p2.fin();
        }
    }
    
    @Test
    public void SGTransportTestDirectReply() throws Exception {
        // get peers
        Peer p1 = Peer.getInstance(new PeerId("p1"));
        Peer p2 = Peer.getInstance(new PeerId("p2"));
        try {
        // base transport
        PeerLocator loc;
        ChannelTransport<?> bt1 = p1.newBaseChannelTransport(
                loc = new UdpLocator(new InetSocketAddress("localhost", 12367)));
        ChannelTransport<?> bt2 = p2.newBaseChannelTransport(
                new UdpLocator(new InetSocketAddress("localhost", 12368)));

        // top level
        Overlay<ComparableKey<?>, ComparableKey<?>> ov1, ov2;
        ov1 = new MSkipGraph<ComparableKey<?>, ComparableKey<?>>(
                        new TransportId("mskip"), bt1);
        
        ov2 = new MSkipGraph<ComparableKey<?>, ComparableKey<?>>(
                        new TransportId("mskip"), bt2);

        sg_received1 = false;
        sg_received2 = false;

        ov1.setListener(new TransportListener<ComparableKey<?>>() {
            public void onReceive(Transport<ComparableKey<?>> trans,
                    ReceivedMessage rmsg) {
                System.out.println("udp recv1:" + rmsg.getMessage());
                sg_received1 = rmsg.getMessage().equals("recv");
            }
        });

        ov2.setListener(new TransportListener<ComparableKey<?>>() {
            public void onReceive(Transport<ComparableKey<?>> trans,
                    ReceivedMessage rmsg) {
                try {
                    System.out.println("udp recv2:" + rmsg.getMessage());
                    sg_received2 = rmsg.getMessage().equals("12345");
                    trans.send((PeerId) rmsg.getSource(), "recv");
                } catch (IOException e) {
                    fail("IOException occured");
                }
            }
        });

        boolean succ1 = ov1.join(loc);
        boolean succ2 = ov2.join(loc);
        Thread.sleep(500);
        boolean succ3 = ov1.addKey(new DoubleKey(1.0));
        boolean succ4 = ov2.addKey(new DoubleKey(2.0));

        assertTrue("SG1 join failed", succ1);
        assertTrue("SG2 join failed", succ2);
        assertTrue("SG1 addKey failed", succ3);
        assertTrue("SG2 addKey failed", succ4);
        Thread.sleep(500);

        ov1.send(new DoubleKey(2.0), "12345");

        Thread.sleep(1000);
        assertTrue("SG2 receive failed", sg_received2);
        assertTrue("SG1 receive failed", sg_received1);
        } finally {
        p1.fin();
        p2.fin();
        }
    }

    int response_count = 0;
    
    @Test
    public void SGOverlayRangecastTest() throws Exception {
        // get peers
        Peer p1 = Peer.getInstance(new PeerId("p1"));
        Peer p2 = Peer.getInstance(new PeerId("p2"));
        Peer p3 = Peer.getInstance(new PeerId("p3"));
        try {
        // base transport
        PeerLocator loc;
        ChannelTransport<?> bt1 = p1.newBaseChannelTransport(
                loc = new UdpLocator(new InetSocketAddress("localhost", 12367)));
        ChannelTransport<?> bt2 = p2.newBaseChannelTransport(
                new UdpLocator(new InetSocketAddress("localhost", 12368)));
        ChannelTransport<?> bt3 = p3.newBaseChannelTransport(
                new UdpLocator(new InetSocketAddress("localhost", 12369)));

        // top level
        Overlay<Destination, ComparableKey<?>> tr1, tr2, tr3;
        tr1 = new MSkipGraph<Destination, ComparableKey<?>>(bt1);
        tr2 = new MSkipGraph<Destination, ComparableKey<?>>(bt2);
        tr3 = new MSkipGraph<Destination, ComparableKey<?>>(bt3);

        sg_received1 = false;
        sg_received2 = false;
        sg_received3 = false;
        response_count = 0;

        tr1.setListener(new OverlayListener<Destination, ComparableKey<?>>() {
            public void onReceive(Overlay<Destination, ComparableKey<?>> overlay,
                    OverlayReceivedMessage<ComparableKey<?>> rmsg) {
                if (rmsg.getMessage().equals("recv")) {
                    response_count++;
                }
                sg_received1 = (response_count == 2);
            }

            public FutureQueue<?> onReceiveRequest(Overlay<Destination, ComparableKey<?>> overlay,
                    OverlayReceivedMessage<ComparableKey<?>> rmsg) {
                return null;
            }

            // unused
            public void onReceive(Transport<Destination> trans, ReceivedMessage rmsg) {
            }
        });
        
        tr2.setListener(new OverlayListener<Destination, ComparableKey<?>>() {
            public void onReceive(Overlay<Destination, ComparableKey<?>> overlay,
                    OverlayReceivedMessage<ComparableKey<?>> rmsg) {
                try {
                    sg_received2 = true;
                    overlay.send(new DoubleKey(Double.parseDouble(
                            (String) rmsg.getMessage())), "recv");
                } catch (IOException e) {
                    fail("IOException occured");
                }
            }

            public FutureQueue<?> onReceiveRequest(Overlay<Destination, ComparableKey<?>> overlay,
                    OverlayReceivedMessage<ComparableKey<?>> rmsg) {
                return null;
            }

            // unused
            public void onReceive(Transport<Destination> trans, ReceivedMessage rmsg) {
            }
        });
        
        tr3.setListener(new OverlayListener<Destination, ComparableKey<?>>() {
            public void onReceive(Overlay<Destination, ComparableKey<?>> overlay,
                    OverlayReceivedMessage<ComparableKey<?>> rmsg) {
                try {
                    sg_received3 = true;
                    overlay.send(new DoubleKey(Double.parseDouble(
                            (String) rmsg.getMessage())), "recv");
                } catch (IOException e) {
                    fail("IOException occured");
                }
            }

            public FutureQueue<?> onReceiveRequest(Overlay<Destination, ComparableKey<?>> overlay,
                    OverlayReceivedMessage<ComparableKey<?>> rmsg) {
                return null;
            }

            // unused
            public void onReceive(Transport<Destination> trans, ReceivedMessage rmsg) {
            }
        });

        tr1.join(loc);
        tr2.join(loc);
        tr3.join(loc);
        Thread.sleep(500);
        tr1.addKey(new DoubleKey(1.0));
        tr2.addKey(new DoubleKey(2.0));
        tr3.addKey(new DoubleKey(3.0));
        Thread.sleep(500);
        
        DoubleKey key = null;
        for (Object obj : tr1.getKeys()) {
            if (obj instanceof DoubleKey) {
                key = (DoubleKey) obj;
            }
        }
        tr1.send(new KeyRange<DoubleKey>(new DoubleKey(2.0), new DoubleKey(3.0)),
                key.getKey().toString());

        Thread.sleep(2000);
        assertTrue("SG2 receive failed", sg_received2);
        assertTrue("SG3 receive failed", sg_received3);
        assertTrue("SG1 receive failed", sg_received1);
        } finally {
        p1.fin();
        p2.fin();
        p3.fin();
        }
    }
    
    @Test
    public void SGOverlayRangeRequestTest() throws Exception {
        // get peers
        Peer p1 = Peer.getInstance(new PeerId("p1"));
        Peer p2 = Peer.getInstance(new PeerId("p2"));
        Peer p3 = Peer.getInstance(new PeerId("p3"));
        try {
        // base transport
        PeerLocator loc;
        ChannelTransport<?> bt1 = p1.newBaseChannelTransport(
                loc = new UdpLocator(new InetSocketAddress("localhost", 12367)));
        ChannelTransport<?> bt2 = p2.newBaseChannelTransport(
                new UdpLocator(new InetSocketAddress("localhost", 12368)));
        ChannelTransport<?> bt3 = p3.newBaseChannelTransport(
                new UdpLocator(new InetSocketAddress("localhost", 12369)));

        // top level
        Overlay<Destination, ComparableKey<?>> tr1, tr2, tr3;
        tr1 = new MSkipGraph<Destination, ComparableKey<?>>(bt1);
        tr2 = new MSkipGraph<Destination, ComparableKey<?>>(bt2);
        tr3 = new MSkipGraph<Destination, ComparableKey<?>>(bt3);

        sg_received2 = false;
        sg_received3 = false;
        
        tr2.setListener(new OverlayListener<Destination, ComparableKey<?>>() {
            public void onReceive(
                    Overlay<Destination, ComparableKey<?>> overlay,
                    OverlayReceivedMessage<ComparableKey<?>> rmsg) {
            }

            public FutureQueue<?> onReceiveRequest(
                    Overlay<Destination, ComparableKey<?>> overlay,
                    OverlayReceivedMessage<ComparableKey<?>> rmsg) {
                return overlay.singletonFutureQueue("recv2");
            }

            // unused
            public void onReceive(Transport<Destination> trans,
                    ReceivedMessage rmsg) {
            }
        });
        
        tr3.setListener(new OverlayListener<Destination, ComparableKey<?>>() {
            public void onReceive(
                    Overlay<Destination, ComparableKey<?>> overlay,
                    OverlayReceivedMessage<ComparableKey<?>> rmsg) {
            }

            public FutureQueue<?> onReceiveRequest(
                    Overlay<Destination, ComparableKey<?>> overlay,
                    OverlayReceivedMessage<ComparableKey<?>> rmsg) {
                return overlay.singletonFutureQueue("recv3");
            }

            // unused
            public void onReceive(Transport<Destination> trans,
                    ReceivedMessage rmsg) {
            }
        });

        tr1.join(loc);
        tr2.join(loc);
        tr3.join(loc);
        Thread.sleep(500);
        tr1.addKey(new DoubleKey(1.0));
        tr2.addKey(new DoubleKey(2.0));
        tr3.addKey(new DoubleKey(3.0));
        Thread.sleep(500);
        
        DoubleKey key = null;
        for (Object obj : tr1.getKeys()) {
            if (obj instanceof DoubleKey) {
                key = (DoubleKey) obj;
            }
        }
        List<Object> l = Arrays.asList(tr1.request(
                new KeyRange<DoubleKey>(new DoubleKey(2.0), new DoubleKey(3.0)),
                key.getKey().toString(), 2000).getAllValues());
        sg_received2 = l.contains("recv2");
        sg_received3 = l.contains("recv3");
        
        assertTrue("SG2 receive failed", sg_received2);
        assertTrue("SG3 receive failed", sg_received3);
        } finally {
        p1.fin();
        p2.fin();
        p3.fin();
        }
    }
    
    @Test
    public void CSOnUdpTest() throws Exception {
        // get peers
        Peer p1 = Peer.getInstance(new PeerId("p1"));
        Peer p2 = Peer.getInstance(new PeerId("p2"));
        try {
        // base transport
        PeerLocator loc;
        ChannelTransport<?> bt1 = p1.newBaseChannelTransport(
                loc = new UdpLocator(new InetSocketAddress("localhost", 12367)));
        ChannelTransport<?> bt2 = p2.newBaseChannelTransport(
                new UdpLocator(new InetSocketAddress("localhost", 12368)));

        // top level
        Overlay<ComparableKey<?>, ComparableKey<?>> tr1 = 
                new Suzaku<ComparableKey<?>, ComparableKey<?>>(
                        bt1);
        Overlay<ComparableKey<?>, ComparableKey<?>> tr2 = 
                new Suzaku<ComparableKey<?>, ComparableKey<?>>(
                        bt2);

        sg_received1 = false;
        sg_received2 = false;

        tr1.setListener(new TransportListener<ComparableKey<?>>() {
            public void onReceive(Transport<ComparableKey<?>> trans,
                    ReceivedMessage rmsg) {
                System.out.println("udp recv1:" + rmsg.getMessage());
                sg_received1 = rmsg.getMessage().equals("recv");
            }
        });

        tr2.setListener(new TransportListener<ComparableKey<?>>() {
            public void onReceive(Transport<ComparableKey<?>> trans,
                    ReceivedMessage rmsg) {
                try {
                    System.out.println("udp recv2:" + rmsg.getMessage());
                    sg_received2 = true;
                    trans.send(new DoubleKey(Double.parseDouble(
                            (String) rmsg.getMessage())), "recv");
                } catch (IOException e) {
                    fail("IOException occured");
                }
            }
        });

        boolean succ1 = tr1.join(loc);
        boolean succ2 = tr2.join(loc);
        Thread.sleep(500);
        boolean succ3 = tr1.addKey(new DoubleKey(1.0));
        boolean succ4 = tr2.addKey(new DoubleKey(2.0));

        assertTrue("SG1 join failed", succ1);
        assertTrue("SG2 join failed", succ2);
        assertTrue("SG1 addKey failed", succ3);
        assertTrue("SG2 addKey failed", succ4);
        Thread.sleep(500);

        DoubleKey key = null;
        for (ComparableKey<?> obj : tr1.getKeys()) {
            if (obj instanceof DoubleKey) {
                key = (DoubleKey) obj;
            }
        }
        long start = System.currentTimeMillis();
        tr1.send(new DoubleKey(2.0), key.getKey().toString());
        System.out.println("send took:" + (System.currentTimeMillis() - start) + "(ms)");
        Thread.sleep(1000);
        assertTrue("SG2 receive failed", sg_received2);
        assertTrue("SG1 receive failed", sg_received1);
        start = System.currentTimeMillis();
        tr1.send(new DoubleKey(2.0), key.getKey().toString(), new TransOptions(RetransMode.NONE));
        System.out.println("send/no-retrans took:" + (System.currentTimeMillis() - start) + "(ms)");
        Thread.sleep(1000);
        assertTrue("SG2 receive failed", sg_received2);
        assertTrue("SG1 receive failed", sg_received1);
        
        start = System.currentTimeMillis();
        tr1.send(new DoubleKey(2.0), key.getKey().toString(), new TransOptions(RetransMode.FAST));
        System.out.println("send/fast-retrans took:" + (System.currentTimeMillis() - start) + "(ms)");
        Thread.sleep(1000);
        assertTrue("SG2 receive failed", sg_received2);
        assertTrue("SG1 receive failed", sg_received1);
        
        start = System.currentTimeMillis();
        tr1.request(new DoubleKey(2.0), key.getKey().toString());
        System.out.println("send-sync (request) took:" + (System.currentTimeMillis() - start) + "(ms)");
//        tr2.send(new Ranges<Double>(new Range<Double>(1.0)), "recv");
//        tr1.send(new DestinationDescription("[2.0..2.0]"));
//        tr2.send(new Ranges<Integer>(new Range<Integer>(1)),
//                "654321".getBytes());
        Thread.sleep(1000);
        assertTrue("SG2 receive failed", sg_received2);
        assertTrue("SG1 receive failed", sg_received1);
        } finally {
        p1.fin();
        p2.fin();
        }
    }

    @Test
    public void CSOnTcpTest() throws Exception {
        // get peers
        Peer p1 = Peer.getInstance(new PeerId("p1"));
        Peer p2 = Peer.getInstance(new PeerId("p2"));
        try {
        // base transport (TCP)
        PeerLocator loc;
        ChannelTransport<?> bt1 = p1.newBaseChannelTransport(
                loc = new TcpLocator(new InetSocketAddress("localhost", 12367)));
        ChannelTransport<?> bt2 = p2.newBaseChannelTransport(
                new TcpLocator(new InetSocketAddress("localhost", 12368)));

        // top level
        Overlay<ComparableKey<?>, ComparableKey<?>> tr1 = 
                new Suzaku<ComparableKey<?>, ComparableKey<?>>(bt1);
        Overlay<ComparableKey<?>, ComparableKey<?>> tr2 = 
                new Suzaku<ComparableKey<?>, ComparableKey<?>>(bt2);

        sg_received1 = false;
        sg_received2 = false;

        tr1.setListener(new TransportListener<ComparableKey<?>>() {
            public void onReceive(Transport<ComparableKey<?>> trans,
                    ReceivedMessage rmsg) {
                System.out.println("tcp recv1:" + rmsg.getMessage());
                sg_received1 = rmsg.getMessage().equals("recv");
            }
        });

        tr2.setListener(new TransportListener<ComparableKey<?>>() {
            public void onReceive(Transport<ComparableKey<?>> trans,
                    ReceivedMessage rmsg) {
                try {
                    System.out.println("tcp recv2:" + rmsg.getMessage());
                    sg_received2 = true;
                    trans.send(new DoubleKey(Double.parseDouble(
                            (String) rmsg.getMessage())), "recv");
                } catch (IOException e) {
                    fail("IOException occured");
                }
            }
        });

        boolean succ1 = tr1.join(loc);
        boolean succ2 = tr2.join(loc);
        Thread.sleep(500);
        boolean succ3 = tr1.addKey(new DoubleKey(1.0));
        boolean succ4 = tr2.addKey(new DoubleKey(2.0));

        assertTrue("SG1 join failed", succ1);
        assertTrue("SG2 join failed", succ2);
        assertTrue("SG1 addKey failed", succ3);
        assertTrue("SG2 addKey failed", succ4);
        Thread.sleep(500);

        DoubleKey key = null;
        for (ComparableKey<?> obj : tr1.getKeys()) {
            if (obj instanceof DoubleKey) {
                key = (DoubleKey) obj;
            }
        }
        tr1.send(new DoubleKey(2.0), key.getKey().toString());

        Thread.sleep(1000);
        assertTrue("SG2 receive failed", sg_received2);
        assertTrue("SG1 receive failed", sg_received1);
        } finally {
        p1.fin();
        p2.fin();
        }
    }

    @Test
    public void CSOnEmuTest() throws Exception {
        // get peers
        Peer p1 = Peer.getInstance(new PeerId("p1"));
        Peer p2 = Peer.getInstance(new PeerId("p2"));
        try {
        // base transport (Emu)
        PeerLocator loc;
        ChannelTransport<?> bt1 = p1.newBaseChannelTransport(
                loc = new EmuLocator(12367));
        ChannelTransport<?> bt2 = p2.newBaseChannelTransport(
                new EmuLocator(12368));

        // top level
        Overlay<ComparableKey<?>, ComparableKey<?>> ov1, ov2;
        ov1 = new Suzaku<ComparableKey<?>, ComparableKey<?>>(
                        new TransportId("mskip"), bt1);
        ov2 = new Suzaku<ComparableKey<?>, ComparableKey<?>>(
                        new TransportId("mskip"), bt2);

        sg_received1 = false;
        sg_received2 = false;

        ov1.setListener(new TransportListener<ComparableKey<?>>() {
            public void onReceive(Transport<ComparableKey<?>> trans,
                    ReceivedMessage rmsg) {
                System.out.println("emu recv1:" + rmsg.getMessage());
                sg_received1 = rmsg.getMessage().equals("recv");
            }
        });

        ov2.setListener(new TransportListener<ComparableKey<?>>() {
            public void onReceive(Transport<ComparableKey<?>> trans,
                    ReceivedMessage rmsg) {
                try {
                    System.out.println("emu recv2:" + rmsg.getMessage());
                    sg_received2 = true;
                    trans.send(new DoubleKey(Double.parseDouble(
                            (String) rmsg.getMessage())), "recv");
                } catch (IOException e) {
                    fail("IOException occured");
                }
            }
        });

        boolean succ1 = ov1.join(loc);
        boolean succ2 = ov2.join(loc);
        Thread.sleep(500);
        boolean succ3 = ov1.addKey(new DoubleKey(1.0));
        boolean succ4 = ov2.addKey(new DoubleKey(2.0));

        assertTrue("SG1 join failed", succ1);
        assertTrue("SG2 join failed", succ2);
        assertTrue("SG1 addKey failed", succ3);
        assertTrue("SG2 addKey failed", succ4);
        Thread.sleep(500);

        DoubleKey key = null;
        for (ComparableKey<?> obj : ov1.getKeys()) {
            if (obj instanceof DoubleKey) {
                key = (DoubleKey) obj;
            }
        }
        ov1.send(new DoubleKey(2.0), key.getKey().toString());

        Thread.sleep(1000);
        assertTrue("SG2 receive failed", sg_received2);
        assertTrue("SG1 receive failed", sg_received1);
        } finally {
        p1.fin();
        p2.fin();
        }
    }
    
    @Test
    public void CSTransportTestDirectReply() throws Exception {
        // get peers
        Peer p1 = Peer.getInstance(new PeerId("p1"));
        Peer p2 = Peer.getInstance(new PeerId("p2"));
        try {
        // base transport
        PeerLocator loc;
        ChannelTransport<?> bt1 = p1.newBaseChannelTransport(
                loc = new UdpLocator(new InetSocketAddress("localhost", 12367)));
        ChannelTransport<?> bt2 = p2.newBaseChannelTransport(
                new UdpLocator(new InetSocketAddress("localhost", 12368)));

        // top level
        Overlay<ComparableKey<?>, DoubleKey> tr1 = 
                new Suzaku<ComparableKey<?>, DoubleKey>(bt1);
        Overlay<ComparableKey<?>, DoubleKey> tr2 = 
                new Suzaku<ComparableKey<?>, DoubleKey>(bt2);

        sg_received1 = false;
        sg_received2 = false;

        tr1.setListener(new TransportListener<ComparableKey<?>>() {
            public void onReceive(Transport<ComparableKey<?>> trans,
                    ReceivedMessage rmsg) {
                System.out.println("udp recv1:" + rmsg.getMessage());
                sg_received1 = rmsg.getMessage().equals("recv");
            }
        });

        tr2.setListener(new TransportListener<ComparableKey<?>>() {
            public void onReceive(Transport<ComparableKey<?>> trans,
                    ReceivedMessage rmsg) {
                try {
                    System.out.println("udp recv2:" + rmsg.getMessage());
                    sg_received2 = rmsg.getMessage().equals("12345");
                    trans.send((PeerId) rmsg.getSource(), "recv");
                } catch (IOException e) {
                    fail("IOException occured");
                }
            }
        });

        boolean succ1 = tr1.join(loc);
        boolean succ2 = tr2.join(loc);
        Thread.sleep(500);
        boolean succ3 = tr1.addKey(new DoubleKey(1.0));
        boolean succ4 = tr2.addKey(new DoubleKey(2.0));

        assertTrue("SG1 join failed", succ1);
        assertTrue("SG2 join failed", succ2);
        assertTrue("SG1 addKey failed", succ3);
        assertTrue("SG2 addKey failed", succ4);
        Thread.sleep(500);

        tr1.send(new DoubleKey(2.0), "12345");

        Thread.sleep(1000);
        assertTrue("SG2 receive failed", sg_received2);
        assertTrue("SG1 receive failed", sg_received1);
        } finally {
        p1.fin();
        p2.fin();
        }
    }

    @Test
    public void CSOverlayRangecastTest() throws Exception {
        // get peers
        Peer p1 = Peer.getInstance(new PeerId("p1"));
        Peer p2 = Peer.getInstance(new PeerId("p2"));
        Peer p3 = Peer.getInstance(new PeerId("p3"));
        try {
        // base transport
        PeerLocator loc;
        ChannelTransport<?> bt1 = p1.newBaseChannelTransport(
                loc = new UdpLocator(new InetSocketAddress("localhost", 12367)));
        ChannelTransport<?> bt2 = p2.newBaseChannelTransport(
                new UdpLocator(new InetSocketAddress("localhost", 12368)));
        ChannelTransport<?> bt3 = p3.newBaseChannelTransport(
                new UdpLocator(new InetSocketAddress("localhost", 12369)));

        // top level
        Overlay<Destination, ComparableKey<?>> tr1, tr2, tr3;
        tr1 = new Suzaku<Destination, ComparableKey<?>>(
                        new TransportId("mskip"), bt1);
        tr2 = new Suzaku<Destination, ComparableKey<?>>(
                        new TransportId("mskip"), bt2);
        tr3 = new Suzaku<Destination, ComparableKey<?>>(
                        new TransportId("mskip"), bt3);

        sg_received1 = false;
        sg_received2 = false;
        sg_received3 = false;
        response_count = 0;

        tr1.setListener(new OverlayListener<Destination, ComparableKey<?>>() {
            public void onReceive(
                    Overlay<Destination, ComparableKey<?>> overlay,
                    OverlayReceivedMessage<ComparableKey<?>> rmsg) {
                System.out.println("udp recv1:" + rmsg.getMessage());
                if (rmsg.getMessage().equals("recv")) {
                    response_count++;
                }
                sg_received1 = (response_count == 2);
            }

            public FutureQueue<?> onReceiveRequest(
                    Overlay<Destination, ComparableKey<?>> overlay,
                    OverlayReceivedMessage<ComparableKey<?>> rmsg) {
                return null;
            }

            /* unused
            public void onReceive(Transport<Destination> trans,
                    ReceivedMessage rmsg) {
            }*/
        });
        
        tr2.setListener(new OverlayListener<Destination, ComparableKey<?>>() {
            public void onReceive(
                    Overlay<Destination, ComparableKey<?>> overlay,
                    OverlayReceivedMessage<ComparableKey<?>> rmsg) {
                try {
                    sg_received2 = true;
                    overlay.send(new DoubleKey(Double.parseDouble(
                            (String) rmsg.getMessage())), "recv");
                } catch (IOException e) {
                    fail("IOException occured");
                }
            }

            public FutureQueue<?> onReceiveRequest(
                    Overlay<Destination, ComparableKey<?>> overlay,
                    OverlayReceivedMessage<ComparableKey<?>> rmsg) {
                return null;
            }

            /*
            public void onReceive(Transport<Destination> trans,
                    ReceivedMessage rmsg) {
            }*/
        });
        
        tr3.setListener(new OverlayListener<Destination, ComparableKey<?>>() {
            public void onReceive(
                    Overlay<Destination, ComparableKey<?>> overlay,
                    OverlayReceivedMessage<ComparableKey<?>> rmsg) {
                try {
                    sg_received3 = true;
                    overlay.send(new DoubleKey(Double.parseDouble(
                            (String) rmsg.getMessage())), "recv");
                } catch (IOException e) {
                    fail("IOException occured");
                }
            }

            public FutureQueue<?> onReceiveRequest(
                    Overlay<Destination, ComparableKey<?>> overlay,
                    OverlayReceivedMessage<ComparableKey<?>> rmsg) {
                return null;
            }

            /*
            public void onReceive(Transport<Destination> trans,
                    ReceivedMessage rmsg) {
            }*/
        });

        tr1.join(loc);
        tr2.join(loc);
        tr3.join(loc);
        Thread.sleep(500);
        tr1.addKey(new DoubleKey(1.0));
        tr2.addKey(new DoubleKey(2.0));
        tr3.addKey(new DoubleKey(3.0));
        Thread.sleep(500);
        
        DoubleKey key = null;
        for (Object obj : tr1.getKeys()) {
            if (obj instanceof DoubleKey) {
                key = (DoubleKey) obj;
            }
        }
        tr1.send(new DCLTranslator().parseDestination("[2.0..3.0]"), key.getKey().toString());
        //tr1.send(new KeyRange<DoubleKey>(new DoubleKey(2.0), new DoubleKey(3.0)),
        //                key.getKey().toString());

        Thread.sleep(2000);
        assertTrue("SG2 receive failed", sg_received2);
        assertTrue("SG3 receive failed", sg_received3);
        assertTrue("SG1 receive failed", sg_received1);
        } finally {
        p1.fin();
        p2.fin();
        p3.fin();
        }
    }
    
    @Test
    public void CSOverlayRangeRequestTest() throws Exception {
        // get peers
        Peer p1 = Peer.getInstance(new PeerId("p1"));
        Peer p2 = Peer.getInstance(new PeerId("p2"));
        Peer p3 = Peer.getInstance(new PeerId("p3"));
        try {
        // base transport
        PeerLocator loc;
        ChannelTransport<?> bt1 = p1.newBaseChannelTransport(
                loc = new UdpLocator(new InetSocketAddress("localhost", 12367)));
        ChannelTransport<?> bt2 = p2.newBaseChannelTransport(
                new UdpLocator(new InetSocketAddress("localhost", 12368)));
        ChannelTransport<?> bt3 = p3.newBaseChannelTransport(
                new UdpLocator(new InetSocketAddress("localhost", 12369)));

        // top level
        Overlay<Destination, ComparableKey<?>> tr1, tr2, tr3;
        tr1 = new Suzaku<Destination, ComparableKey<?>>(
                        bt1);
        tr2 = new Suzaku<Destination, ComparableKey<?>>(
                        bt2);
        tr3 = new Suzaku<Destination, ComparableKey<?>>(
                        bt3);

        sg_received2 = false;
        sg_received3 = false;
        
        tr2.setListener(new OverlayListener<Destination, ComparableKey<?>>() {
            public void onReceive(
                    Overlay<Destination, ComparableKey<?>> overlay,
                    OverlayReceivedMessage<ComparableKey<?>> rmsg) {
            }

            public FutureQueue<?> onReceiveRequest(
                    Overlay<Destination, ComparableKey<?>> overlay,
                    OverlayReceivedMessage<ComparableKey<?>> rmsg) {
                return overlay.singletonFutureQueue("recv2");
            }

            /*
            public void onReceive(Transport<Destination> trans,
                    ReceivedMessage rmsg) {
            }*/
        });
        
        tr3.setListener(new OverlayListener<Destination, ComparableKey<?>>() {
            public void onReceive(
                    Overlay<Destination, ComparableKey<?>> overlay,
                    OverlayReceivedMessage<ComparableKey<?>> rmsg) {
            }

            public FutureQueue<?> onReceiveRequest(
                    Overlay<Destination, ComparableKey<?>> overlay,
                    OverlayReceivedMessage<ComparableKey<?>> rmsg) {
                return overlay.singletonFutureQueue("recv3");
            }

            /*
            public void onReceive(Transport<Destination> trans,
                    ReceivedMessage rmsg) {
            }*/
        });

        tr1.join(loc);
        tr2.join(loc);
        tr3.join(loc);
        Thread.sleep(500);
        tr1.addKey(new DoubleKey(1.0));
        tr2.addKey(new DoubleKey(2.0));
        tr3.addKey(new DoubleKey(3.0));
        Thread.sleep(500);
        
        DoubleKey key = null;
        for (Object obj : tr1.getKeys()) {
            if (obj instanceof DoubleKey) {
                key = (DoubleKey) obj;
            }
        }
        List<Object> l = Arrays.asList(tr1.request(
                new KeyRange<DoubleKey>(new DoubleKey(2.0), new DoubleKey(3.0)),
                key.getKey().toString(), 2000).getAllValues());
        sg_received2 = l.contains("recv2");
        sg_received3 = l.contains("recv3");
        
        assertTrue("SG2 receive failed", sg_received2);
        assertTrue("SG3 receive failed", sg_received3);
        } finally {
        p1.fin();
        p2.fin();
        p3.fin();
        }
    }
    
    void MSGAddKeyOnRequestTest(boolean tcp) throws Exception {
    		int numOfPeers = 50;
        // get peers
    		PeerLocator loc = null;
    		MSkipGraph<Destination, ComparableKey<?>> trs[] = new MSkipGraph[numOfPeers];
    		Peer peers[] = new Peer[numOfPeers];
    		try {
    		for (int i = 0; i < numOfPeers; i++) {
                    PeerLocator l = tcp ? new TcpLocator(new InetSocketAddress("localhost", 12367 + i)) :
                        new UdpLocator(new InetSocketAddress("localhost", 12367 + i));
    			if (loc == null) {
    				loc = l; // remember for seed; 
    			}
    			trs[i] = new MSkipGraph<Destination, ComparableKey<?>>((peers[i] = Peer.getInstance(new PeerId("p" + i))).newBaseChannelTransport(l));
    			int x = i;
    			trs[i].setListener(new OverlayListener<Destination, ComparableKey<?>>() {
    				public void onReceive(
    						Overlay<Destination, ComparableKey<?>> overlay,
    						OverlayReceivedMessage<ComparableKey<?>> rmsg) {
    				}

    				public FutureQueue<?> onReceiveRequest(
    						Overlay<Destination, ComparableKey<?>> overlay,
    						OverlayReceivedMessage<ComparableKey<?>> rmsg) {
    					try {
    						DoubleKey key = new DoubleKey((double)x + numOfPeers);
    						if(!overlay.getKeys().contains(key)) {
    							System.out.println("adding key" + key);
    							overlay.addKey(key);
    						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    					return overlay.singletonFutureQueue("recv" + x);
    				}
    			});
    		}
    		for (int i = 0; i < numOfPeers; i++) {
    			trs[i].join(loc);
    			trs[i].addKey(new DoubleKey((double)i));
    		}

        List<Object> l = Arrays.asList(trs[0].request(
                new KeyRange<DoubleKey>(new DoubleKey(0.0), new DoubleKey((double)numOfPeers)),
                "req", 2000).getAllValues());
        System.out.println("size=" + l.size());
        assertTrue("receive failed", l.size() == numOfPeers);
    		} finally {
        for (int i = 0; i < numOfPeers; i++) {
        		peers[i].fin();
        }
    		}
    }

    @Test
    public void MSGAddKeyOnRequestOnTCPTest() throws Exception {
        MSGAddKeyOnRequestTest(true);
    }

    @Test
    public void MSGAddKeyOnRequestOnUDPTest() throws Exception {
        MSGAddKeyOnRequestTest(false);
    }
    
    void SuzakuAddKeyOnRequestTest(boolean tcp) throws Exception {
        int numOfPeers = 50;
        // get peers
        PeerLocator loc = null;
        Suzaku<Destination, ComparableKey<?>> trs[] = new Suzaku[numOfPeers];
        Peer peers[] = new Peer[numOfPeers];
        for (int i = 0; i < numOfPeers; i++) {
            PeerLocator l = tcp ? new TcpLocator(new InetSocketAddress(
                    "localhost", 12367 + i)) : new UdpLocator(
                    new InetSocketAddress("localhost", 12367 + i));
            if (loc == null) {
                loc = l; // remember for seed;
            }
            trs[i] = new Suzaku<Destination, ComparableKey<?>>((peers[i] = Peer
                    .getInstance(new PeerId("p" + i)))
                    .newBaseChannelTransport(l));
            int x = i;
            trs[i].setListener(new OverlayListener<Destination, ComparableKey<?>>() {
                public void onReceive(
                        Overlay<Destination, ComparableKey<?>> overlay,
                        OverlayReceivedMessage<ComparableKey<?>> rmsg) {
                }

                public FutureQueue<?> onReceiveRequest(
                        Overlay<Destination, ComparableKey<?>> overlay,
                        OverlayReceivedMessage<ComparableKey<?>> rmsg) {
                    try {
                        DoubleKey key = new DoubleKey((double) x + numOfPeers);
                        if (!overlay.getKeys().contains(key)) {
                            System.out.println("adding key" + key);
                            overlay.addKey(key);
                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    return overlay.singletonFutureQueue("recv" + x);
                }
            });
        }
        try {
        for (int i = 0; i < numOfPeers; i++) {
            trs[i].join(loc);
            trs[i].addKey(new DoubleKey((double) i));
        }
        System.out.println("sleeping 5 sec...");
        Thread.sleep(5000);

        List<Object> l = Arrays.asList(trs[0]
                .request(
                        new KeyRange<DoubleKey>(new DoubleKey(0.0), true,
                                new DoubleKey((double) numOfPeers), false),
                        "req", 2000).getAllValues());
        System.out.println("size=" + l.size());
        System.out.println("received: " + l);
        assertTrue("receive failed", l.size() == numOfPeers);
        } finally {
        for (int i = 0; i < numOfPeers; i++) {
            peers[i].fin();
        }
        }
    }
    
    @Test
    public void SuzakuAddKeyOnRequestOnTCPTest() throws Exception {
        SuzakuAddKeyOnRequestTest(true);
    }
    
    @Test
    public void SuzakuAddKeyOnRequestOnUDPTest() throws Exception {
        SuzakuAddKeyOnRequestTest(false);
    }
    

	@Test
    public void UDPTransportFailureChannelTest() throws Exception {
        // get peers
        Peer p1 = Peer.getInstance(new PeerId("p1"));
        Peer p2 = Peer.getInstance(new PeerId("p2"));
        try {
        FailureSimulationChannelTransport<UdpLocator> ft;
        
        Transport<UdpLocator> bt1 = p1.newBaseTransport(
                new UdpLocator(
                        new InetSocketAddress("localhost", 12367)));
        Transport<UdpLocator> bt2 = p2.newBaseTransport(
				new UdpLocator(
						new InetSocketAddress("localhost", 12368)));
        // top level
        ChannelTransport<UdpLocator> tr1 = 
                ft = new FailureSimulationChannelTransport<UdpLocator>(bt1);
        
        ChannelTransport<UdpLocator> tr2 =
                new FailureSimulationChannelTransport<UdpLocator>(bt2);

        tr1.setChannelListener(new ChannelListener<UdpLocator>() {
            public boolean onAccepting(Channel<UdpLocator> channel) {
                return true;
            }

            public void onClosed(Channel<UdpLocator> channel) {
            }

            public void onFailure(Channel<UdpLocator> channel, Exception cause) {
            }

            public void onReceive(Channel<UdpLocator> ch) {
                if (ch.isCreatorSide())
                    return;
                Object msg = ch.receive();
                try {
                    ch.send(msg);
                } catch (IOException e) {
                    fail("IOException occured");
                }
            }
        });
        ft.setErrorRate(0);
        ft.upsetTransport();
        
        Channel<UdpLocator> c = tr2.newChannel(tr1.getEndpoint());
        c.send("654321");
        String mes = (String) c.receive(1000);
        assertTrue(mes.equals("654321"));
        c.close();
        
        ft.setErrorRate(100);
        try {
        	c = tr2.newChannel(tr1.getEndpoint());
        	c.send("654321");
        	mes = (String) c.receive(1000);
        }
        catch (Exception e) {
        	mes = null;
        }
        assertTrue(mes == null);
        c.close();
        } finally {
        p1.fin();
        p2.fin();
        }
    }
	
	@Test
    public void CSOnFailureSimulationChannelTest() throws Exception {
    	// get peers
        Peer p1 = Peer.getInstance(new PeerId("p1"));
        Peer p2 = Peer.getInstance(new PeerId("p2"));
        Peer p3 = Peer.getInstance(new PeerId("p3"));
        try {
        PeerLocator loc;
        
        FailureSimulationChannelTransport<UdpLocator> ft;
        
        // top level
        Overlay<Destination, ComparableKey<?>> tr1, tr2, tr3;
        tr1 = new Suzaku<Destination, ComparableKey<?>>(
        				ft = new FailureSimulationChannelTransport<UdpLocator>(
        						p1.newBaseChannelTransport(loc = new UdpLocator(new InetSocketAddress("localhost", 12367)))));
        
        tr2 = new Suzaku<Destination, ComparableKey<?>>(
                        new FailureSimulationChannelTransport<UdpLocator>(
                        		p2.newBaseTransport(new UdpLocator(new InetSocketAddress("localhost", 12368)))));
        
        tr3 = new Suzaku<Destination, ComparableKey<?>>(
                        new FailureSimulationChannelTransport<UdpLocator>(
                        		p3.newBaseChannelTransport(new UdpLocator(new InetSocketAddress("localhost", 12369)))));

        sg_received2 = false;
        sg_received3 = false;
        
        tr2.setListener(new OverlayListener<Destination, ComparableKey<?>>() {
            public void onReceive(
                    Overlay<Destination, ComparableKey<?>> overlay,
                    OverlayReceivedMessage<ComparableKey<?>> rmsg) {
            }

            public FutureQueue<?> onReceiveRequest(
                    Overlay<Destination, ComparableKey<?>> overlay,
                    OverlayReceivedMessage<ComparableKey<?>> rmsg) {
                return overlay.singletonFutureQueue("recv2");
            }

            /*
            public void onReceive(Transport<Destination> trans,
                    ReceivedMessage rmsg) {
            }*/
        });
        
        tr3.setListener(new OverlayListener<Destination, ComparableKey<?>>() {
            public void onReceive(
                    Overlay<Destination, ComparableKey<?>> overlay,
                    OverlayReceivedMessage<ComparableKey<?>> rmsg) {
            }

            public FutureQueue<?> onReceiveRequest(
                    Overlay<Destination, ComparableKey<?>> overlay,
                    OverlayReceivedMessage<ComparableKey<?>> rmsg) {
                return overlay.singletonFutureQueue("recv3");
            }

            /*
            public void onReceive(Transport<Destination> trans,
                    ReceivedMessage rmsg) {
            }*/
        });

        tr1.join(loc);
        tr2.join(loc);
        tr3.join(loc);
        Thread.sleep(500);
        tr1.addKey(new DoubleKey(1.0));
        tr2.addKey(new DoubleKey(2.0));
        tr3.addKey(new DoubleKey(3.0));
        Thread.sleep(500);
        
        DoubleKey key = null;
        for (Object obj : tr1.getKeys()) {
            if (obj instanceof DoubleKey) {
                key = (DoubleKey) obj;
            }
        }
        
        ft.setErrorRate(100);
        ft.upsetTransport();
        
        List<Object> l = Arrays.asList(tr1.request(
                new KeyRange<DoubleKey>(new DoubleKey(2.0), new DoubleKey(3.0)),
                key.getKey().toString(), 2000).getAllValues());
        sg_received2 = l.contains("recv2");
        sg_received3 = l.contains("recv3");
        assertTrue("SG2 received falsely", !sg_received2);
        assertTrue("SG3 received falsely", !sg_received3);
        
        ft.setErrorRate(100);
        l = Arrays.asList(tr1.request(
                new KeyRange<DoubleKey>(new DoubleKey(2.0), new DoubleKey(3.0)),
                key.getKey().toString(), 2000).getAllValues());
        sg_received2 = l.contains("recv2");
        sg_received3 = l.contains("recv3");
        assertTrue("SG2 received falsely", !sg_received2);
        assertTrue("SG3 received falsely", !sg_received3);
        } finally {
        p1.fin();
        p2.fin();
        p3.fin();
        }
	}
	
	@Test
    public void CSRunsOnFailureSimulationChannelTest() throws Exception {
    	// get peers
        Peer p1 = Peer.getInstance(new PeerId("p1"));
        Peer p2 = Peer.getInstance(new PeerId("p2"));
        Peer p3 = Peer.getInstance(new PeerId("p3"));
        try {
        PeerLocator loc;

        FailureSimulationChannelTransport<UdpLocator> ft;
        
        // top level
        Overlay<Destination, ComparableKey<?>> tr1, tr2, tr3;
        tr1 = new Suzaku<Destination, ComparableKey<?>>(
        				ft = new FailureSimulationChannelTransport<UdpLocator>(
        						p1.newBaseChannelTransport(loc = new UdpLocator(new InetSocketAddress("localhost", 12367)))));
        
        tr2 = new Suzaku<Destination, ComparableKey<?>>(
                        new FailureSimulationChannelTransport<UdpLocator>(
                        		p2.newBaseTransport(
                        				new UdpLocator(new InetSocketAddress("localhost", 12368)))));
        
        tr3 = new Suzaku<Destination, ComparableKey<?>>(
                        new FailureSimulationChannelTransport<UdpLocator>(
                        		p3.newBaseChannelTransport(
                        				new UdpLocator(new InetSocketAddress("localhost", 12369)))));

        sg_received2 = false;
        sg_received3 = false;
        
        tr2.setListener(new OverlayListener<Destination, ComparableKey<?>>() {
            public void onReceive(
                    Overlay<Destination, ComparableKey<?>> overlay,
                    OverlayReceivedMessage<ComparableKey<?>> rmsg) {
            }

            public FutureQueue<?> onReceiveRequest(
                    Overlay<Destination, ComparableKey<?>> overlay,
                    OverlayReceivedMessage<ComparableKey<?>> rmsg) {
                return overlay.singletonFutureQueue("recv2");
            }

            // unused
            public void onReceive(Transport<Destination> trans,
                    ReceivedMessage rmsg) {
            }
        });
        
        tr3.setListener(new OverlayListener<Destination, ComparableKey<?>>() {
            public void onReceive(
                    Overlay<Destination, ComparableKey<?>> overlay,
                    OverlayReceivedMessage<ComparableKey<?>> rmsg) {
            }

            public FutureQueue<?> onReceiveRequest(
                    Overlay<Destination, ComparableKey<?>> overlay,
                    OverlayReceivedMessage<ComparableKey<?>> rmsg) {
                return overlay.singletonFutureQueue("recv3");
            }

            /*
            public void onReceive(Transport<Destination> trans,
                    ReceivedMessage rmsg) {
            }*/
        });

        tr1.join(loc);
        tr2.join(loc);
        tr3.join(loc);
        Thread.sleep(500);
        tr1.addKey(new DoubleKey(1.0));
        tr2.addKey(new DoubleKey(2.0));
        tr3.addKey(new DoubleKey(3.0));
        Thread.sleep(500);
        
        DoubleKey key = null;
        for (Object obj : tr1.getKeys()) {
            if (obj instanceof DoubleKey) {
                key = (DoubleKey) obj;
            }
        }
        
        ft.setErrorRate(100);
        ft.upsetTransport();
        
        List<Object> l = Arrays.asList(tr2.request(
                new KeyRange<DoubleKey>(new DoubleKey(2.0), new DoubleKey(3.0)),
                key.getKey().toString(), 2000).getAllValues());
        sg_received2 = l.contains("recv2");
        sg_received3 = l.contains("recv3");
        
        assertTrue("SG2 receive failed", sg_received2);
        assertTrue("SG3 receive failed", sg_received3);
        
        ft.setErrorRate(100);
        l = Arrays.asList(tr1.request(
                new KeyRange<DoubleKey>(new DoubleKey(2.0), new DoubleKey(3.0)),
                key.getKey().toString(), 2000).getAllValues());
        sg_received2 = l.contains("recv2");
        sg_received3 = l.contains("recv3");
        assertTrue("SG2 received falsely", !sg_received2);
        assertTrue("SG3 received falsely", !sg_received3);
        
        Thread.sleep(20000);
        //ft.setErrorRate(0);
        ft.repairTransport();
        l = Arrays.asList(tr2.request(
                new KeyRange<DoubleKey>(new DoubleKey(1.0), new DoubleKey(3.0)),
                key.getKey().toString(), 2000).getAllValues());
        sg_received2 = l.contains("recv2");
        sg_received3 = l.contains("recv3");
        assertTrue("SG2 received", sg_received2);
        assertTrue("SG3 received", sg_received3);
        } finally {
        p1.fin();
        p2.fin();
        p3.fin();
        }
	}
	
	@Test
    public void UDPThroughTransportTest() throws Exception {
        // get peers
        Peer p1 = Peer.getInstance(new PeerId("p1"));
        Peer p2 = Peer.getInstance(new PeerId("p2"));
        try {
        // top level
        Transport<UdpLocator> tr1 = new ThroughTransport<UdpLocator>(
        				p1.newBaseChannelTransport(
        				new UdpLocator(
        						new InetSocketAddress("localhost", 12367))));
        
        Transport<UdpLocator> tr2 = new ThroughTransport<UdpLocator>(
        				p2.newBaseChannelTransport(
        				new UdpLocator(
        						new InetSocketAddress("localhost", 12368))));

        udp_received1 = false;
        udp_received2 = false;
                
        tr1.setListener(new TransportListener<UdpLocator>() {
            public void onReceive(Transport<UdpLocator> trans, ReceivedMessage msg) {
                udp_received1 = "654321".equals(msg.getMessage());
            }
        });
        
        tr2.setListener(new TransportListener<UdpLocator>() {
            public void onReceive(Transport<UdpLocator> trans, ReceivedMessage msg) {
                udp_received2 = "123456".equals(msg.getMessage());
            }
        });

        tr1.send((UdpLocator) tr2.getEndpoint(), "123456");
        tr2.send((UdpLocator) tr1.getEndpoint(), "654321");

        // Channel ch = tr1.newChannel(tr2.getMyEndpoint());
        // ch.send("abcdefg".getBytes());

        // ReceivedMessage b = ch.receive(1000);
        // ch.close();
        Thread.sleep(1000);
        assertTrue("UDP1 receive failed", udp_received1);
        assertTrue("UDP2 receive failed", udp_received2);
        } finally {
        p1.fin();
        p2.fin();
        }
        // assertTrue(ByteUtil.equals(b.getMessage(), "abcdefg".getBytes()));
    }
    
}
