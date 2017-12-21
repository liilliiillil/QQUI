import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by admin on 2017/12/21.
 */
public class AddFriendScreen extends JFrame
{
    private JTextField FriendUsername;
    private JButton Add;
    public AddFriendScreen()
    {
        setSize(300,100);
        Container AddFriendScreencontainer=getContentPane();
        setLayout(new GridLayout(2,2,10,10));
        AddFriendScreencontainer.add(new JLabel("FriendUsername",SwingConstants.RIGHT));
        FriendUsername=new JTextField();
        AddFriendScreencontainer.add(FriendUsername);
        AddFriendScreencontainer.add(setAddButton());
    }
    public JButton setAddButton()
    {
        Add=new JButton("Add");
        Add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //等待代码
            }
        });
        return Add;
    }
}
