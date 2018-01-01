import Client.UDPBunissness;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by admin on 2017/12/19.
 */
public class LoginScreen extends JFrame
{
    private JTextField ID;
    private JPasswordField passwordField;
    private JButton SignIn;
    private JButton Register;
    private Thread signinThread;
    public LoginScreen() //登录注册界面的构造
    {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container LoginScreencontainer=getContentPane();
        setLayout(new GridLayout(3,1,0,10));
        JPanel NamePanel=setNamePanel();
        LoginScreencontainer.add(NamePanel);
        JPanel UsernamePassword=setUsernamePasswordPanel();
        LoginScreencontainer.add(UsernamePassword);
        LoginScreencontainer.add(setSigninRegisterPanel());
        setSize(400,300);
        setVisible(true);
    }
    public JPanel setNamePanel() //软件标题栏
    {
        JPanel namepanel=new JPanel(new GridLayout(1,1));
        namepanel.add(new JLabel("QQ",SwingConstants.CENTER));
        return namepanel;
    }
    public JPanel setUsernamePasswordPanel() //用户名密码输入栏
    {
        JPanel usernamepassword=new JPanel(new GridLayout(2,2,10,10));
        usernamepassword.add(new JLabel("ID",SwingConstants.RIGHT));
        ID =new JTextField();
        usernamepassword.add(ID);
        usernamepassword.add(new JLabel("Password",SwingConstants.RIGHT));
        passwordField=new JPasswordField();
        passwordField.setEchoChar('.');
        usernamepassword.add(passwordField);
        return usernamepassword;
    }

    public JPanel setSigninRegisterPanel() //登录和注册按钮
    {
        JPanel signinregister=new JPanel(new GridLayout(1,2,0,0));
        SignIn=new JButton("Sign in");
        Register=new JButton("Register");
        SignIn.addActionListener(new ActionListener() {  //等待代码1
            @Override
            public void actionPerformed(ActionEvent e) {
                signinThread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            UDPBunissness signinLink=new UDPBunissness();
                            signinLink.setType("Login");
                            signinLink.setId(ID.getText());
                            signinLink.setPassword(String.copyValueOf(passwordField.getPassword()));
                            signinLink.sendData();
                            if (signinLink.receiveOfLogin().equals("FALSE"))
                            {
                                new IDorpwWrong(LoginScreen.this).setVisible(true);
                            }
                            else
                            {
                                FriendList friendList=new FriendList(ID.getText());
                                friendList.setVisible(true);
                                LoginScreen.loginScreen.setVisible(false);
                                LoginScreen.loginScreen.dispose();
                            }
                        }
                        catch (Exception e1)
                        {
                            //
                        }
                    }
                });
                signinThread.start();
            }
        });
        signinregister.add(SignIn);
        Register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterScreen().setVisible(true);
            }
        });
        signinregister.add(Register);
        return signinregister;
    }
    static LoginScreen loginScreen;
    public static void main(String[] args)
    {
        loginScreen = new LoginScreen();
    }
}
class IDorpwWrong extends JDialog
{
    public IDorpwWrong(LoginScreen LS)
    {
        super(LS,"",true);
        Container container=getContentPane();
        container.add(new JLabel("ID或密码错误"));
        setBounds(120,120,100,100);
    }
}
