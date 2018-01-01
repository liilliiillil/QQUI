import Client.TCPBunissness;
import Client.UDPBunissness;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/12/28.
 */
public class FriendList extends JFrame
{
    String ID;
    int nowFriendNum=0;
    static SQLiteManagement SQLM;
    JButton Addfriend;
    Container container;
    Thread AskMessage;
    public FriendList(String id)
    {
        super(id);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ID=id;
        container=getContentPane();
        try
        {
            SQLM=new SQLiteManagement(Integer.parseInt(id));
            setSize(100,1000);
            setLayout(new GridLayout(20,1,10,10));
            setAddfriendBu();
            container.add(Addfriend);
            setAskMessage(id);
            AskMessage.start();
        }
        catch (Exception e1)
        {
            //
        }
    }
    public void setAskMessage(String id)
    {
        AskMessage=new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    while (true)
                    {
                        TCPBunissness askmesLink=new TCPBunissness();
                        askmesLink.setType("Ask_Message");
                        askmesLink.setId(id);
                        askmesLink.sendData();
                        Map m=askmesLink.reciveOfAskMessage();
                        SQLM.updateChatMessage(m);
                        refreshFriendlist();
                        Thread.currentThread().sleep(500);
                    }

                }
                catch (Exception e1)
                {
                    e1.printStackTrace();
                }
            }
        });
    }
    public void refreshFriendlist() throws Exception
    {
        int FriendNum;
        List friendlist=SQLM.queryFriendList();
        FriendNum=friendlist.size();
        for(int i=nowFriendNum;i<FriendNum;i++)
        {
            Map friendM=(Map) friendlist.get(i);
            String friendid=(String)friendM.get("Id");
            String usern=(String) friendM.get("Username");
            JButton friendBut=new JButton(usern);
            friendBut.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new ChatFreame(ID,friendid,usern).setVisible(true);
                }
            });
            container.add(friendBut);
            FriendList.this.revalidate();
        }
        nowFriendNum=FriendNum;
    }
    public void setAddfriendBu()
    {
        Addfriend=new JButton("添加好友");
        Addfriend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddFriendDia addFriendDia=new AddFriendDia(FriendList.this,ID);
                addFriendDia.setVisible(true);
            }
        });
    }
}
class AddFriendDia extends JDialog
{
    Dialog dialog;
    JTextField Friendid;
    JButton Add;
    String FriendUN;
    public AddFriendDia(FriendList FL,String Id)
    {
        super(FL,"添加好友",true);
        dialog = this;
        setLayout(new GridLayout(2,2,10,10));
        setSize(400,300);
        Friendid=new JTextField();
        Add=new JButton("添加");
        Container container=getContentPane();
        container.add(new JLabel("FriendID"));
        container.add(Friendid);
        Add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!Friendid.getText().equals(Id)) {
                    Thread addthread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                UDPBunissness addfriend = new UDPBunissness();
                                addfriend.setType("User_Info");
                                addfriend.setId(Friendid.getText());
                                addfriend.sendData();
                                FriendUN = addfriend.receiveOfUserInfo();
                                if (FriendUN.equals("")) {
                                    JDialog jDialog = new JDialog(dialog);
                                    jDialog.requestFocus();
                                    jDialog.setSize(300,150);
                                    JLabel jLabel = new JLabel();
                                    jLabel.setText("不存在该用户");
                                    jLabel.setHorizontalAlignment(JLabel.CENTER);
                                    jDialog.getContentPane().add(jLabel);
                                    jDialog.setVisible(true);
                                } else {
                                    String SQL = "INSERT INTO UserInfo VALUES (" + Friendid.getText() + ",\"" + FriendUN + "\")";
                                    FriendList.SQLM.setSql(SQL);
                                    FriendList.SQLM.updateSql();
                                }
                            } catch (Exception e1) {
                                //
                            }
                        }
                    });
                    addthread.start();
                } else {
                    JDialog jDialog = new JDialog(dialog);
                    jDialog.requestFocus();
                    jDialog.setSize(300,150);
                    JLabel jLabel = new JLabel();
                    jLabel.setText("不能添加自己为好友");
                    jLabel.setHorizontalAlignment(JLabel.CENTER);
                    jDialog.getContentPane().add(jLabel);
                    jDialog.setVisible(true);
                }
            }
        });
        container.add(Add);
    }
}
