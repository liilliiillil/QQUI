import Client.ByteProcessingFunction;
import Client.TCPBunissness;
import Client.UDPBunissness;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by admin on 2017/12/29.
 */
public class ChatFreame extends JFrame
{
    private String ID;
    private String toID;
    private JScrollPane MessageSP;
    private JPanel MessageP;
    private JPanel ToolP;
    private JButton SendFile;
    private JButton SendPicture;
    private JButton Send;
    private JTextArea MessageArea;
    private Thread AskMes;
    private Thread CheckAskMe;
    private GBC gbc;
    private int nowMesNum;
    public ChatFreame(String ID,String toID,String toUsername)
    {
        super(toUsername);
        this.ID=ID;
        this.toID=toID;
        setSize(1200,900);
        this.setLayout(null);
        Container container=getContentPane();
        setMessageP();
        MessageSP=new JScrollPane(MessageP);
        MessageSP.setPreferredSize(new Dimension(1200,550));
        MessageSP.setBounds(0,0,1200,550);
        container.add(MessageSP);
        setSendFile();
        setSendPicture();
        ToolP = new JPanel();
        ToolP.add(SendFile);
        ToolP.add(SendPicture);
        ToolP.setLayout(new FlowLayout(FlowLayout.LEFT));
        ToolP.setBounds(0,550,1200,50);
        container.add(ToolP);
        MessageArea=new JTextArea();
        MessageArea.setBounds(0,600,1000,300);
        container.add(MessageArea);
        setSend(toID);
        Send.setBounds(1000,700,100,100);
        container.add(Send);
        setAskMessage(ID);
        AskMes.start();
    }
    private void setSendFile() {
        SendFile = new JButton();
        SendFile.setText("发送文件");
        SendFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setVisible(true);
                fileChooser.showDialog(null,"选择");
                File file = fileChooser.getSelectedFile();
                if (file != null) {
                    SendF sendF = new SendF(ID,toID,file);
                    sendF.start();
                }
            }
        });
    }
    private void setSendPicture() {
        SendPicture = new JButton("发送图片");
        SendPicture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.resetChoosableFileFilters();
                FileFilter[] fileFilters = fileChooser.getChoosableFileFilters();
                fileChooser.removeChoosableFileFilter(fileFilters[0]);
                fileChooser.setFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        if (f.isDirectory())
                            return true;
                        return f.getName().endsWith(".jpg") || f.getName().endsWith(".png") || f.getName().endsWith(".gif");
                    }

                    @Override
                    public String getDescription() {
                        return "图片(.jpg .png .gif)";
                    }
                });
                fileChooser.showDialog(null,"选择");
                File file = fileChooser.getSelectedFile();
                SendP sendP = new SendP(ID,toID,file);
                sendP.start();
            }
        });
    }
    public void setAskMessage(String Id)
    {
        AskMes=new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    while (true)
                    {
                        TCPBunissness askmesLink=new TCPBunissness();
                        askmesLink.setType("Ask_Message");
                        askmesLink.setId(Id);
                        askmesLink.sendData();
                        Map m=askmesLink.reciveOfAskMessage();
                        FriendList.SQLM.updateChatMessage(m);
                        addMessageP();
                        Thread.currentThread().sleep(500);
                    }
                }
                catch (Exception e1)
                {
                    //
                }
            }
        });
    }
    public void addMessageP()
    {
        try
        {
            java.util.List list=FriendList.SQLM.queryChatMessage(Integer.parseInt(toID));
            Component[] components = MessageP.getComponents();
            for (int i = 0; i < 13 && i < components.length / 2;i++) {
                Component component = MessageP.getComponent(i * 2);
                if (component instanceof JLabel) {
                    if (((JLabel) component).getText() == null) {
                        MessageP.remove(i * 2);
                        MessageP.remove(i * 2 + 1);
                    }
                }
            }
            for(int i=nowMesNum;i<list.size();i++)
            {
                Map mesmap=(Map) list.get(i);
                String message=(String) mesmap.get("Message");
                String type = (String) mesmap.get("MessageType");
                String date = (String) mesmap.get("Date");
                String subMessage = (String) mesmap.get("SubMessage");
                boolean whoM=mesmap.get("From").equals(ID);
                Color color = Color.GREEN;
                if(!whoM) {
                    color = Color.cyan;
                }
                gbc.gridy = nowMesNum++ * 2;
                switch (type) {
                    case "Text":{
                        JLabel messageA=new JLabel(date);
                        JTextArea messageB=new JTextArea(message);
                        messageB.setEditable(false);
                        messageA.setOpaque(true);
                        messageA.setBackground(color);
                        messageB.setBackground(color);
                        gbc.setInsets(0);
                        MessageP.add(messageA,gbc);
                        gbc.setInsets(0,0,10,0);
                        gbc.gridy++;
                        MessageP.add(messageB,gbc);
                    }
                    break;
                    case "File":{
                        JLabel messageA = new JLabel(date);
                        JButton messageB = new JButton(subMessage);
                        messageB.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                AskF askF = new AskF((String) mesmap.get("Id"));
                                askF.start();
                            }
                        });
                        messageA.setOpaque(true);
                        messageA.setBackground(color);
                        gbc.setInsets(0);
                        MessageP.add(messageA,gbc);
                        gbc.setInsets(0,0,10,0);
                        gbc.gridy++;
                        MessageP.add(messageB,gbc);
                    }
                    break;
                    case "Picture":{
                        JLabel messageA = new JLabel(date);
                        JLabel messageB = new JLabel();
                        messageA.setOpaque(true);
                        messageB.setOpaque(true);
                        messageA.setBackground(color);
                        messageB.setBackground(color);
                        gbc.setInsets(0);
                        MessageP.add(messageA,gbc);
                        gbc.setInsets(0,0,10,0);
                        gbc.gridy++;
                        MessageP.add(messageB,gbc);
                        AskP askP = new AskP((String) mesmap.get("Id"),messageB);
                        askP.start();
                    }
                }
            }
            components = MessageP.getComponents();
            if (components.length / 2 < 13) {
                for (int i = components.length / 2; i < 13;i++) {
                    gbc.gridy++;
                    JLabel messageA = new JLabel(" ");
                    JLabel messageB = new JLabel(" ");
                    gbc.setInsets(0);
                    MessageP.add(messageA,gbc);
                    gbc.setInsets(0,0,10,0);
                    gbc.gridy++;
                    MessageP.add(messageB,gbc);
                }
            }
            nowMesNum=list.size();
            MessageP.revalidate();
        }
        catch (Exception e1)
        {
            //
        }
    }
    public void setMessageP()
    {
        MessageP=new JPanel();
        GridBagLayout gridLayout = new GridBagLayout();
        gbc = new GBC(0,GridBagConstraints.RELATIVE);
        gbc.setFill(GridBagConstraints.HORIZONTAL);
        gbc.setAnchor(GridBagConstraints.NORTH);
        gbc.weightx = 100.0;
        gbc.insets = new Insets(0,0,10,0);
        gridLayout.setConstraints(MessageP,gbc);
        gridLayout.location(0,0);
        MessageP.setLayout(gridLayout);
        MessageP.setSize(1200,10);
        MessageP.setBounds(0,0,1200,10);
        //TODO 载入历史记录
    }
    public void setSend(String toID)
    {
        Send=new JButton("发送");
        Send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SendM sendM=new SendM(ID,toID,MessageArea.getText());
                sendM.start();
                MessageArea.setText("");
            }
        });
    }

}

class SendM extends Thread
{
    String Id,toID,Text;
    SendM(String Id,String toID,String Text)
    {
        this.Id=Id;
        this.toID=toID;
        this.Text=Text;
    }

    @Override
    public void run() {
        try {
            UDPBunissness sendMesLink=new UDPBunissness();
            sendMesLink.setType("Send_Message");
            sendMesLink.setId(Id);
            sendMesLink.setTo(toID);
            sendMesLink.setMessage(Text);
            sendMesLink.sendData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class SendF extends Thread
{
    String Id,toID;
    File file;
    SendF(String Id,String toID,File file)
    {
        this.Id=Id;
        this.toID=toID;
        this.file=file;
    }

    @Override
    public void run() {
        try {
            TCPBunissness tcpBunissness = new TCPBunissness();
            tcpBunissness.setType("Send_File");
            tcpBunissness.setFile(file);
            tcpBunissness.setFrom(Id);
            tcpBunissness.setTo(toID);
            JDialog jDialog = new JDialog();
            jDialog.requestFocus();
            jDialog.setSize(300,150);
            JLabel jLabel = new JLabel();
            jLabel.setText("发送中请稍候");
            jLabel.setHorizontalAlignment(JLabel.CENTER);
            jDialog.getContentPane().add(jLabel);
            jDialog.setVisible(true);
            tcpBunissness.sendData();
            jLabel.setText("发送完毕");
            jDialog.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class SendP extends Thread
{
    String Id,toID;
    File file;
    SendP(String Id,String toID,File file)
    {
        this.Id=Id;
        this.toID=toID;
        this.file=file;
    }

    @Override
    public void run() {
        try {
            TCPBunissness tcpBunissness = new TCPBunissness();
            tcpBunissness.setType("Send_Picture");
            tcpBunissness.setFile(file);
            tcpBunissness.setFrom(Id);
            tcpBunissness.setTo(toID);
            tcpBunissness.sendData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class AskF extends Thread {
    String Id;
    AskF(String Id)
    {
        this.Id=Id;
    }

    @Override
    public void run() {
        try {
            TCPBunissness tcpBunissness = new TCPBunissness();
            tcpBunissness.setType("Ask_File");
            tcpBunissness.setId(Id);
            tcpBunissness.sendData();
            JDialog jDialog = new JDialog();
            jDialog.requestFocus();
            jDialog.setSize(300,150);
            JLabel jLabel = new JLabel();
            jLabel.setText("下载中请稍候。。。");
            jLabel.setHorizontalAlignment(JLabel.CENTER);
            jDialog.getContentPane().add(jLabel);
            jDialog.setVisible(true);
            Map map = tcpBunissness.reciveOfAskMessage();
            String fileName = (String) map.get("FileName");
            File dir = new File("ReceiveFile");
            if (!dir.isDirectory() || !dir.exists())
                dir.mkdirs();
            ByteProcessingFunction.saveFile((byte[]) map.get("Data"),"./ReceiveFile/" + fileName);
            jLabel.setText("下载完毕");
            jDialog.repaint();
        } catch (IOException | ClassNotFoundException e1) {
            e1.printStackTrace();
        }
    }
}

class AskP extends Thread {
    JLabel jLabel;
    String Id;
    AskP(String Id, JLabel jLabel)
    {
        this.jLabel = jLabel;
        this.Id=Id;
    }

    @Override
    public void run() {
        try {
            TCPBunissness tcpBunissness = new TCPBunissness();
            tcpBunissness.setType("Ask_Picture");
            tcpBunissness.setId(Id);
            tcpBunissness.sendData();
            Map map = tcpBunissness.reciveOfAskMessage();
            String fileName = (String) map.get("FileName");
            File dir = new File("cache");
            if (!dir.isDirectory() || !dir.exists())
                dir.mkdirs();
            ByteProcessingFunction.saveFile((byte[]) map.get("Data"),"./cache/" + Id + fileName);
            jLabel.setIcon(new ImageIcon("./cache/" + Id + fileName));
            jLabel.getParent().revalidate();
        } catch (IOException | ClassNotFoundException e1) {
            e1.printStackTrace();
        }
    }
}