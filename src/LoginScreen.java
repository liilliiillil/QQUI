import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by admin on 2017/12/19.
 */
public class LoginScreen extends JFrame
{
    private JTextField username;
    private JPasswordField passwordField;
    private JButton SignIn;
    private JButton Register;
    public LoginScreen() //登录注册界面的构造
    {
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
        usernamepassword.add(new JLabel("Username",SwingConstants.RIGHT));
        username=new JTextField();
        usernamepassword.add(username);
        usernamepassword.add(new JLabel("Password",SwingConstants.RIGHT));
        passwordField=new JPasswordField();
        passwordField.setEchoChar('·');
        usernamepassword.add(passwordField);
        return usernamepassword;
    }
    public JPanel setSigninRegisterPanel() //登录和注册按钮
    {
        JPanel signinregister=new JPanel(new GridLayout(1,2,0,0));
        SignIn=new JButton("Sign in");
        Register=new JButton("Register");
        SignIn.addActionListener(new ActionListener() {  //等待代码
            @Override
            public void actionPerformed(ActionEvent e) {
                new ChatScreen().setVisible(true);
            }
        });
        signinregister.add(SignIn);
        Register.addActionListener(new ActionListener() { //等待代码
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterScreen().setVisible(true);
            }
        });
        signinregister.add(Register);
        return signinregister;
    }
    public static void main(String[] args)
    {
        new LoginScreen();
    }
}
