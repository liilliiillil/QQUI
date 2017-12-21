import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

/**
 * Created by admin on 2017/12/20.
 */
public class ChatScreen extends JFrame
{
    private JTabbedPane FriendList;
    private JPanel ChatPanel;
    private JTextArea SendMessageArea;
    private JPanel MessagePanel;
    private JButton AddFriend;
    private JButton SendMessage;
    public ChatScreen()
    {
        setSize(1200,900);
        Container ChatScreencontainer=getContentPane();
        ChatScreencontainer.add(setFriendList());

    }
    public JTabbedPane setFriendList()//等待代码
    {
        FriendList=new JTabbedPane(SwingConstants.LEFT,JTabbedPane.SCROLL_TAB_LAYOUT);
        FriendList.addTab("好友1",setChatPanel());
        FriendList.addTab("好友2",setChatPanel());
        return FriendList;
    }
    public JPanel setChatPanel()
    {
        ChatPanel=new JPanel();
        ChatPanel.setLayout(null);
        setAddFriend().setBounds(1000,0,100,50);
        ChatPanel.add(AddFriend);
        setMessagePanel().setBounds(0,50,1000,550);
        ChatPanel.add(MessagePanel);
        setSendMessageArea().setBounds(0,600,800,300);
        ChatPanel.add(SendMessageArea);
        setSendMessage().setBounds(800,700,200,100);
        ChatPanel.add(SendMessage);
        return ChatPanel;
    }
    public JButton setAddFriend()
    {
        AddFriend=new JButton("AddFriend");
        AddFriend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddFriendScreen().setVisible(true);
            }
        });
        return AddFriend;
    }
    public JPanel setMessagePanel()
    {
        MessagePanel=new JPanel();
        //等待代码
        return MessagePanel;
    }
    public JTextArea setSendMessageArea()
    {
        SendMessageArea=new JTextArea("",10,10);
        SendMessageArea.setLineWrap(true);
        return SendMessageArea;
    }
    public JButton setSendMessage()
    {
        SendMessage=new JButton("Send");
        SendMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //等待代码
            }
        });
        return SendMessage;
    }
}
