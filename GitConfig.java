import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;



public class GitConfig{

	private static StringBuffer sb=new StringBuffer();
	
	public static void main(String[] args) {
		
		try {
			BufferedReader strin = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Input your name:");
			String str1 = strin.readLine();
			System.out.print("Input your email:");
			String str2 = strin.readLine();
			config(str1,str2);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	


	// 相关全局配置
	private static void config(String str1, String str2) {
		
		if(str1.equals("")){
			execute("whoami");
			str1=sb.toString();
			sb=sb.delete(0,sb.length());
		}
		if(str2.equals(""))str2=null;
		String cmd1="git config --global user.name "+str1;
		String cmd2="git config --global user.email "+str2;
		String cmd3="git config --global color.ui true";
		execute(cmd1);
		execute(cmd2);
		execute(cmd3);
		String str=sb.toString();
		if(str.equals("")){
			System.out.println("Successful");
			sshconfig(str2);
		}else{
			System.out.println("Error:"+"\n"+str);
		}
	}
	
	
	//设置github的ssh key
	private static void sshconfig(String str) {
		String cmd1 = "whoami";
		execute(cmd1);
		String user = sb.toString();
		String path;
		String path2;
		if (user.equals("root")) {
			path = "/root/.ssh/id_rsa.pub";
			path2="/root/.ssh/id_rsa";
		} else {
			path = "/home/" + user + "/.ssh/id_rsa.pub";
			path2="/home/"+user+"/.ssh/id_rsa";
		}
		File file = new File(path);
		String cmd2 = "ssh-keygen -t rsa -P '' -C '"+ str +"' -f '"+ path2 +"'>>/dev/zero&&cat "+path;
		//如果有ssh文件存在可直接使用
		if (file.exists()) {
		FileInputStream input;
		BufferedInputStream br;
		try {
			input = new FileInputStream(file);
			br = new BufferedInputStream(input);
			byte[] text = new byte[1024];
			int len = 0;
			System.out.println("Copy it:");
			while ((len = br.read(text)) != -1)
				System.out.println(new String(text, 0, len));
		} catch (Exception e) {
			e.printStackTrace();
		}
		}else{
		System.out.println("Execute it:");
		System.out.println(cmd2);
		}	
	}

        // 执行bash命令，并把结果保存到sb变量中
	private static void execute(String cmd){
		InputStream in = null;
		try {
			Process	pro = Runtime.getRuntime().exec(cmd);// 执行cmd命令
			pro.waitFor();// 等待命令执行完
			in = pro.getInputStream();// 获取结果输出流
			BufferedReader read = new BufferedReader(new InputStreamReader(in));// 将输出流放入缓冲区中
			String result = null;
		while((result=read.readLine())!=null)
			sb.append(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
}

